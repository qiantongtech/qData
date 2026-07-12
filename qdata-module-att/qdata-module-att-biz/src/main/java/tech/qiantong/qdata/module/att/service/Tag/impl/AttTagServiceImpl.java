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
import tech.qiantong.qdata.common.utils.MessageUtils;
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
 * Tag Management Service business layer processing
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
        // Asset tagging - filter out assets already tagged
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
            throw new ServiceException("att.error.tag.cat.notfound", "标签类目不存在");
        }
        dictType.setCatName(attTagCatDO.getName());
        attTagMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTag(AttTagSaveReqVO updateReqVO) {
        // Validate

        // Update Tag Management
        AttTagDO updateObj = BeanUtils.toBean(updateReqVO, AttTagDO.class);
        String catCode = updateObj.getCatCode();
        if(StringUtils.isNotEmpty(catCode)){
            Map<String, AttTagCatDO> collect2 = attTagCatService.list().stream().collect(Collectors.toMap(s -> s.getCode(), Function.identity()));
            AttTagCatDO attTagCatDO = collect2.get(catCode);
            if (attTagCatDO == null) {
                throw new ServiceException("att.error.tag.cat.notfound", "标签类目不存在");
            }
            if (attTagCatDO != null) {
                updateObj.setCatName(attTagCatDO.getName());
            }
        }
        return attTagMapper.updateById(updateObj);
    }

    @Override
    public int removeAttTag(Collection<Long> idList) {
        // Batch Delete Tag Management
        for (Long l : idList) {
            LambdaQueryWrapperX<AttTagAssetRelDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eqIfPresent(AttTagAssetRelDO::getTagId, l);
            queryWrapperX.eqIfPresent(AttTagAssetRelDO::getDelFlag, 0);
            List<AttTagAssetRelDO> collect = attTagAssetRelService.list(queryWrapperX);
            if (collect != null && collect.size() > 0) {
                throw new ServiceException("att.error.tag.asset.exists", "存在资产信息，不允许Delete ");
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
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Tag Management data
     *
     *  importExcelList Tag Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  @return Result
     */
    @Override
    public String importAttTag(List<AttTagRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
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
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "数据Update 成功，ID为 " + attTagId + " 的标签管理记录。", attTagId, "标签管理"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "数据Update 失败，ID为 " + attTagId + " 的标签管理记录不存在。", attTagId, "标签管理"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "数据Update 失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<AttTagDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attTagId);
                    AttTagDO existingAttTag = attTagMapper.selectOne(queryWrapper);
                    if (existingAttTag == null) {
                        attTagMapper.insert(attTagDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "数据插入成功，ID为 " + attTagId + " 的标签管理记录。", attTagId, "标签管理"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "数据插入失败，ID为 " + attTagId + " 的标签管理记录已存在。", attTagId, "标签管理"));
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
