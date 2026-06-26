/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.att.service.cat.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttTagCatMapper;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;
import tech.qiantong.qdata.module.att.service.cat.IAttTagCatService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签类目管理Service业务层处理
 *
 * @author qdata
 * @date 2025-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTagCatServiceImpl extends ServiceImpl<AttTagCatMapper,AttTagCatDO> implements IAttTagCatService {
    @Resource
    private AttTagCatMapper attTagCatMapper;

    @Resource
    private IAttTagService attTagService;

    @Override
    public PageResult<AttTagCatDO> getAttTagCatPage(AttTagCatPageReqVO pageReqVO) {
        return attTagCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTagCat(AttTagCatSaveReqVO createReqVO) {
        AttTagCatDO dictType = BeanUtils.toBean(createReqVO, AttTagCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attTagCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTagCat(AttTagCatSaveReqVO updateReqVO) {
        AttTagCatDO catDO = attTagCatMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        //判断是否选择了他自己
        if (catDO.getId().equals(updateReqVO.getParentId())){
            throw new ServiceException("att.error.parent.self", "切换上级不能选择自身作为上级类目");
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = attTagService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.tag", "存在标签，不允许禁用");
            }
            attTagCatMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttTagCatDO parent = attTagCatMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "须先启用父级");
            }
        }

        //修改上下级判断
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId()) ) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // 更新标签类目管理
        AttTagCatDO updateObj = BeanUtils.toBean(updateReqVO, AttTagCatDO.class);
        int i = attTagCatMapper.updateById(updateObj);

        attTagService.updateCatCode(catDO.getCode(),updateObj.getCode());
        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<AttTagCatDO> list = baseMapper.selectList(Wrappers.lambdaQuery(AttTagCatDO.class)
                .eq(AttTagCatDO::getParentId, pid)
                .orderByAsc(AttTagCatDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                attTagService.updateCatCode(codeOld,codeNew);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }


    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<AttTagCatDO> query = new LambdaQueryWrapper<AttTagCatDO>()
                .eq(AttTagCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttTagCatDO::getCode, parentCode)
                .isNotNull(AttTagCatDO::getCode)
                .orderByDesc(AttTagCatDO::getCode);
        List<AttTagCatDO> list = attTagCatMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                AttTagCatDO parent = attTagCatMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public Integer removeAttTagCat(Long id) {
        int count = 0;
        AttTagCatDO cat = attTagCatMapper.selectById(id);
        //判断是否存在数据
        if (attTagService.getCountByCatCode(cat.getCode()) > 0) {
            throw new ServiceException("att.error.delete.tag", "存在标签，不允许删除");
        }
        if (cat != null) {
            count += attTagCatMapper.delete(Wrappers.lambdaQuery(AttTagCatDO.class)
                    .likeRight(AttTagCatDO::getCode, cat.getCode()));
        }
        return count;
    }

//    @Override
//    public int removeAttTagCat(Collection<Long> idList) {
//        // 批量删除标签类目管理
//        return attTagCatMapper.deleteBatchIds(idList);
//    }

    @Override
    public AttTagCatDO getAttTagCatById(Long id) {
        return attTagCatMapper.selectById(id);
    }

    @Override
    public List<AttTagCatDO> getAttTagCatList() {
        return attTagCatMapper.selectList();
    }

    @Override
    public Map<Long, AttTagCatDO> getAttTagCatMap() {
        List<AttTagCatDO> attTagCatList = attTagCatMapper.selectList();
        return attTagCatList.stream()
                .collect(Collectors.toMap(
                        AttTagCatDO::getId,
                        attTagCatDO -> attTagCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入标签类目管理数据
         *
         * @param importExcelList 标签类目管理数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttTagCat(List<AttTagCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttTagCatRespVO respVO : importExcelList) {
                try {
                    AttTagCatDO attTagCatDO = BeanUtils.toBean(respVO, AttTagCatDO.class);
                    Long attTagCatId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attTagCatId != null) {
                            AttTagCatDO existingAttTagCat = attTagCatMapper.selectById(attTagCatId);
                            if (existingAttTagCat != null) {
                                attTagCatMapper.updateById(attTagCatDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "数据更新成功，ID为 " + attTagCatId + " 的标签类目管理记录。", attTagCatId, "标签类目管理"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "数据更新失败，ID为 " + attTagCatId + " 的标签类目管理记录不存在。", attTagCatId, "标签类目管理"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<AttTagCatDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attTagCatId);
                        AttTagCatDO existingAttTagCat = attTagCatMapper.selectOne(queryWrapper);
                        if (existingAttTagCat == null) {
                            attTagCatMapper.insert(attTagCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "数据插入成功，ID为 " + attTagCatId + " 的标签类目管理记录。", attTagCatId, "标签类目管理"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "数据插入失败，ID为 " + attTagCatId + " 的标签类目管理记录已存在。", attTagCatId, "标签类目管理"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public List<AttTagCatDO> getAttTagCatLIst(AttTagCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttTagCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttTagCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTagCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTagCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttTagCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTagCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttTagCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTagCatDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(AttTagCatDO::getSortOrder);
        return attTagCatMapper.selectList(queryWrapperX);
    }
}
