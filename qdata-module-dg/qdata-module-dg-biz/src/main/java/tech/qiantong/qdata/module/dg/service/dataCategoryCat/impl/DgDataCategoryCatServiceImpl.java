/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dg.service.dataCategoryCat.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategoryCat.DgDataCategoryCatMapper;
import tech.qiantong.qdata.module.dg.service.dataCategory.IDgDataCategoryService;
import tech.qiantong.qdata.module.dg.service.dataCategoryCat.IDgDataCategoryCatService;

/**
 * 数据分类-类目Service业务层处理
 *
 * @author FXB
 * @date 2026-04-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDataCategoryCatServiceImpl extends ServiceImpl<DgDataCategoryCatMapper, DgDataCategoryCatDO> implements IDgDataCategoryCatService {
    @Resource
    private DgDataCategoryCatMapper dgDataCategoryCatMapper;

    @Resource
    private IDgDataCategoryService dgDataCategoryService;

    @Override
    public PageResult<DgDataCategoryCatDO> getDgDataCategoryCatPage(DgDataCategoryCatPageReqVO pageReqVO) {
        return dgDataCategoryCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDataCategoryCat(DgDataCategoryCatSaveReqVO createReqVO) {
        DgDataCategoryCatDO dictType = BeanUtils.toBean(createReqVO, DgDataCategoryCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dgDataCategoryCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataCategoryCat(DgDataCategoryCatSaveReqVO updateReqVO) {
        DgDataCategoryCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        //判断是否选择了他自己
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("切换上级不能选择自身作为上级类目");
        }
        //修改上下级判断
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // 更新数据服务类目管理
        DgDataCategoryCatDO updateObj = BeanUtils.toBean(updateReqVO, DgDataCategoryCatDO.class);
        int i = dgDataCategoryCatMapper.updateById(updateObj);

        dgDataCategoryService.updateCatCode(catDO.getCode(), updateObj.getCode());
        //判断上下级是否发生了改变
        if (flag) {
            //更改所有下级
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }
        return i;
    }

    @Override
    public int removeDgDataCategoryCat(Collection<Long> idList) {
        List<DgDataCategoryCatDO> attApiCatDOS = baseMapper.selectBatchIds(idList);
        for (DgDataCategoryCatDO catDO : attApiCatDOS) {
            Long countData = dgDataCategoryService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("存在分类，不允许删除");
            }
        }
        // 批量删除数据分类-类目
        return dgDataCategoryCatMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataCategoryCatDO getDgDataCategoryCatById(Long id) {
        return dgDataCategoryCatMapper.selectById(id);
    }

    @Override
    public List<DgDataCategoryCatDO> getDgDataCategoryCatList() {
        return dgDataCategoryCatMapper.selectList();
    }

    @Override
    public Map<Long, DgDataCategoryCatDO> getDgDataCategoryCatMap() {
        List<DgDataCategoryCatDO> dgDataCategoryCatList = dgDataCategoryCatMapper.selectList();
        return dgDataCategoryCatList.stream()
                .collect(Collectors.toMap(
                        DgDataCategoryCatDO::getId,
                        dgDataCategoryCatDO -> dgDataCategoryCatDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据分类-类目数据
     *
     * @param importExcelList 数据分类-类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDgDataCategoryCat(List<DgDataCategoryCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DgDataCategoryCatRespVO respVO : importExcelList) {
            try {
                DgDataCategoryCatDO dgDataCategoryCatDO = BeanUtils.toBean(respVO, DgDataCategoryCatDO.class);
                Long dgDataCategoryCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (dgDataCategoryCatId != null) {
                        DgDataCategoryCatDO existingDgDataCategoryCat = dgDataCategoryCatMapper.selectById(dgDataCategoryCatId);
                        if (existingDgDataCategoryCat != null) {
                            dgDataCategoryCatMapper.updateById(dgDataCategoryCatDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DgDataCategoryCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dgDataCategoryCatId);
                    DgDataCategoryCatDO existingDgDataCategoryCat = dgDataCategoryCatMapper.selectOne(queryWrapper);
                    if (existingDgDataCategoryCat == null) {
                        dgDataCategoryCatMapper.insert(dgDataCategoryCatDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
        }
        return resultMsg.toString();
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
        LambdaQueryWrapper<DgDataCategoryCatDO> query = new LambdaQueryWrapper<DgDataCategoryCatDO>()
                .eq(DgDataCategoryCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DgDataCategoryCatDO::getCode, parentCode)
                .isNotNull(DgDataCategoryCatDO::getCode)
                .orderByDesc(DgDataCategoryCatDO::getCode);
        List<DgDataCategoryCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                DgDataCategoryCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DgDataCategoryCatDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DgDataCategoryCatDO.class)
                .eq(DgDataCategoryCatDO::getParentId, pid)
                .orderByAsc(DgDataCategoryCatDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                dgDataCategoryService.updateCatCode(codeOld, codeNew);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }
}
