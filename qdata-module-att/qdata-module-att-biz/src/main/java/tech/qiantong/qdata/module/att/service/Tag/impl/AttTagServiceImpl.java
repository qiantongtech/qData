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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.att.service.Tag.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.Tag.dto.AttTagRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.tag.IAttTagApiService;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.Tag.AttTagMapper;
import tech.qiantong.qdata.module.att.service.Rel.IAttTagAssetRelService;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;
import tech.qiantong.qdata.module.att.service.cat.IAttTagCatService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 标签管理Service业务层处理
 *
 * @author qdata
 * @date 2025-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTagServiceImpl extends ServiceImpl<AttTagMapper, AttTagDO> implements IAttTagService, IAttTagApiService {
    @Resource
    private AttTagMapper attTagMapper;
    @Resource
    private IAttTagAssetRelService attTagAssetRelService;
    @Resource
    private IAttTagCatService attTagCatService;

    @Override
    public PageResult<AttTagDO> getAttTagPage(AttTagPageReqVO pageReqVO) {
        // 资产打标过滤该资产已经打过标的信息
        if (pageReqVO.getAeestId() != null) {
            LambdaQueryWrapperX<AttTagAssetRelDO> attTagAssetRelDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
            attTagAssetRelDOLambdaQueryWrapperX.eqIfPresent(AttTagAssetRelDO::getAssetId, pageReqVO.getAeestId());
            attTagAssetRelDOLambdaQueryWrapperX.eqIfPresent(AttTagAssetRelDO::getDelFlag, 0);
            List<AttTagAssetRelDO> list = attTagAssetRelService.list(attTagAssetRelDOLambdaQueryWrapperX);
            Long[] tagIds = new Long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                AttTagAssetRelDO attTagAssetRelDO = list.get(i);
                tagIds[i] = Convert.toLong(attTagAssetRelDO.getTagId());
            }
            pageReqVO.setIds(tagIds);
        }
        PageResult<AttTagDO> attTagDOPageResult = attTagMapper.selectPage(pageReqVO);

        return attTagDOPageResult;
    }

    @Override
    public Long createAttTag(AttTagSaveReqVO createReqVO) {
        AttTagDO dictType = BeanUtils.toBean(createReqVO, AttTagDO.class);
        Map<String, AttTagCatDO> collect2 = attTagCatService.list().stream().collect(Collectors.toMap(s -> s.getCode(), Function.identity()));
        AttTagCatDO attTagCatDO = collect2.get(dictType.getCatCode());
        if (attTagCatDO == null) {
            throw new ServiceException("标签类目不存在");
        }
        dictType.setCatName(attTagCatDO.getName());
        attTagMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTag(AttTagSaveReqVO updateReqVO) {
        // 相关校验

        // 更新标签管理
        AttTagDO updateObj = BeanUtils.toBean(updateReqVO, AttTagDO.class);
        String catCode = updateObj.getCatCode();
        if(StringUtils.isNotEmpty(catCode)){
            Map<String, AttTagCatDO> collect2 = attTagCatService.list().stream().collect(Collectors.toMap(s -> s.getCode(), Function.identity()));
            AttTagCatDO attTagCatDO = collect2.get(catCode);
            if (attTagCatDO == null) {
                throw new ServiceException("标签类目不存在");
            }
            if (attTagCatDO != null) {
                updateObj.setCatName(attTagCatDO.getName());
            }
        }
        return attTagMapper.updateById(updateObj);
    }

    @Override
    public int removeAttTag(Collection<Long> idList) {
        // 批量删除标签管理
        for (Long l : idList) {
            LambdaQueryWrapperX<AttTagAssetRelDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eqIfPresent(AttTagAssetRelDO::getTagId, l);
            queryWrapperX.eqIfPresent(AttTagAssetRelDO::getDelFlag, 0);
            List<AttTagAssetRelDO> collect = attTagAssetRelService.list(queryWrapperX);
            if (collect != null && collect.size() > 0) {
                throw new ServiceException("存在资产信息，不允许删除");
            }
        }

        return attTagMapper.deleteBatchIds(idList);
    }

    @Override
    public AttTagRespVO getAttTagById(Long id) {
        AttTagDO attTagDO = attTagMapper.selectById(id);
        if (attTagDO == null) {
            return new AttTagRespVO();
        }
        LambdaQueryWrapperX<AttTagAssetRelDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(AttTagAssetRelDO::getTagId, id);
        queryWrapperX.eqIfPresent(AttTagAssetRelDO::getDelFlag, 0);
        List<AttTagAssetRelDO> collect = attTagAssetRelService.list(queryWrapperX);
        List<Long> list = new ArrayList<>();
        for (AttTagAssetRelDO attTagAssetRelDO : collect) {
            list.add(Convert.toLong(attTagAssetRelDO.getAssetId()));
        }
        AttTagRespVO bean = BeanUtils.toBean(attTagDO, AttTagRespVO.class);
        bean.setAeestId(list);
        return bean;
    }

    @Override
    public List<AttTagDO> getAttTagList() {
        return attTagMapper.selectList();
    }

    @Override
    public Map<Long, AttTagDO> getAttTagMap() {
        List<AttTagDO> attTagList = attTagMapper.selectList();
        return attTagList.stream()
                .collect(Collectors.toMap(
                        AttTagDO::getId,
                        attTagDO -> attTagDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入标签管理数据
     *
     * @param importExcelList 标签管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttTag(List<AttTagRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttTagRespVO respVO : importExcelList) {
            try {
                AttTagDO attTagDO = BeanUtils.toBean(respVO, AttTagDO.class);
                Long attTagId = respVO.getId();
                if (isUpdateSupport) {
                    if (attTagId != null) {
                        AttTagDO existingAttTag = attTagMapper.selectById(attTagId);
                        if (existingAttTag != null) {
                            attTagMapper.updateById(attTagDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attTagId + " 的标签管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attTagId + " 的标签管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttTagDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attTagId);
                    AttTagDO existingAttTag = attTagMapper.selectOne(queryWrapper);
                    if (existingAttTag == null) {
                        attTagMapper.insert(attTagDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attTagId + " 的标签管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attTagId + " 的标签管理记录已存在。");
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
    public Long getCountByCatCode(String catCode) {
        return attTagMapper.selectCount(Wrappers.lambdaQuery(AttTagDO.class)
                .likeRight(AttTagDO::getCatCode, catCode));
    }

    @Override
    public List<AttTagRespDTO> getApiList() {
        List<AttTagDO> attTagDOS = attTagMapper.selectList();
        return BeanUtils.toBean(attTagDOS, AttTagRespDTO.class);
    }

    @Override
    public int updateCatCode(String oldCatCode, String newCatCode) {
        return attTagMapper.updateCatCode(oldCatCode,newCatCode);
    }
}
