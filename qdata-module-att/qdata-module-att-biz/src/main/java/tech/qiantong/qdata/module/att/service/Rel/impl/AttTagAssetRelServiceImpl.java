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

package tech.qiantong.qdata.module.att.service.Rel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelReqDTO;
import tech.qiantong.qdata.module.att.api.Rel.dto.AttTagAssetRelRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.tagRel.IAttTagAssetRelApiService;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tag.vo.AttTagSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;
import tech.qiantong.qdata.module.att.dal.dataobject.Tag.AttTagDO;
import tech.qiantong.qdata.module.att.dal.mapper.Rel.AttTagAssetRelMapper;
import tech.qiantong.qdata.module.att.service.Rel.IAttTagAssetRelService;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Tag and asset association relationship Service business layer processing
 *
 * @author qdata
 * @date 2025-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTagAssetRelServiceImpl  extends ServiceImpl<AttTagAssetRelMapper,AttTagAssetRelDO> implements IAttTagAssetRelService , IAttTagAssetRelApiService {
    @Resource
    private AttTagAssetRelMapper attTagAssetRelMapper;

    @Resource
    private IAttTagService attTagService;

    @Override
    public PageResult<AttTagAssetRelDO> getAttTagAssetRelPage(AttTagAssetRelPageReqVO pageReqVO) {
        return attTagAssetRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTagAssetRel(AttTagAssetRelSaveReqVO createReqVO) {

        attTagAssetRelMapper.delete("asset_id", createReqVO.getAssetId());
        List<String> tagIds = createReqVO.getTagIds();
        // Add relation table, update tag asset count
        for (String tagId : tagIds) {
            AttTagAssetRelDO relDo = new AttTagAssetRelDO();
            relDo.setAssetId(createReqVO.getAssetId());
            relDo.setTagId(tagId);
            attTagAssetRelMapper.insert(relDo);
            Long l = attTagAssetRelMapper.selectCount("tag_id", tagId);
            AttTagDO attTagDO = new AttTagDO();
            attTagDO.setId(Convert.toLong(tagId));
            attTagDO.setAeestCount(l);
            AttTagSaveReqVO bean = BeanUtils.toBean(attTagDO, AttTagSaveReqVO.class);
            attTagService.updateAttTag(bean);
        }
        return 1L;
    }

    @Override
    public int updateAttTagAssetRel(AttTagAssetRelSaveReqVO updateReqVO) {
        // Validation

        // Update tag and asset association relationship
        AttTagAssetRelDO updateObj = BeanUtils.toBean(updateReqVO, AttTagAssetRelDO.class);
        return attTagAssetRelMapper.updateById(updateObj);
    }
    @Override
    public int removeAttTagAssetRel(Collection<Long> idList) {
        // Batch delete tag and asset association relationship
        return attTagAssetRelMapper.deleteBatchIds(idList);
    }

    @Override
    public AttTagAssetRelDO getAttTagAssetRelById(Long id) {
        return attTagAssetRelMapper.selectById(id);
    }

    @Override
    public List<AttTagAssetRelDO> getAttTagAssetRelList() {
        return attTagAssetRelMapper.selectList();
    }

    @Override
    public Map<Long, AttTagAssetRelDO> getAttTagAssetRelMap() {
        List<AttTagAssetRelDO> attTagAssetRelList = attTagAssetRelMapper.selectList();
        return attTagAssetRelList.stream()
                .collect(Collectors.toMap(
                        AttTagAssetRelDO::getId,
                        attTagAssetRelDO -> attTagAssetRelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import tag and asset association relationship data
         *
         *  importExcelList tag and asset association relationship data list
         * @param isUpdateSupport Whether to support update; if already exists, update the data
         *  operName Operator
         *  Result
         */
        @Override
        public String importAttTagAssetRel(List<AttTagAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttTagAssetRelRespVO respVO : importExcelList) {
                try {
                    AttTagAssetRelDO attTagAssetRelDO = BeanUtils.toBean(respVO, AttTagAssetRelDO.class);
                    Long attTagAssetRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attTagAssetRelId != null) {
                            AttTagAssetRelDO existingAttTagAssetRel = attTagAssetRelMapper.selectById(attTagAssetRelId);
                            if (existingAttTagAssetRel != null) {
                                attTagAssetRelMapper.updateById(attTagAssetRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "Update succeeded, record with ID " + attTagAssetRelId + " (tag and asset association).", attTagAssetRelId, "tag and asset association"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "Update failed, record with ID " + attTagAssetRelId + " (tag and asset association) does not exist.", attTagAssetRelId, "tag and asset association"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "Update failed, the ID of some record does not exist."));
                        }
                    } else {
                        QueryWrapper<AttTagAssetRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attTagAssetRelId);
                        AttTagAssetRelDO existingAttTagAssetRel = attTagAssetRelMapper.selectOne(queryWrapper);
                        if (existingAttTagAssetRel == null) {
                            attTagAssetRelMapper.insert(attTagAssetRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "Insert succeeded, record with ID " + attTagAssetRelId + " (tag and asset association).", attTagAssetRelId, "tag and asset association"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "Insert failed, record with ID " + attTagAssetRelId + " (tag and asset association) already exists.", attTagAssetRelId, "tag and asset association"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "Import failed, error: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                        "Import failed! " + failureNum + " records have incorrect format. Errors:<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                        "All data imported successfully! Total: " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public int removeAttTagAssetRel(Long id, AttTagAssetRelPageReqVO attTagAssetRel) {
        AttTagRespVO attTagById = attTagService.getAttTagById(Convert.toLong(attTagAssetRel.getTagId()));
        attTagById.setAeestCount(attTagById.getAeestCount() - 1);
        AttTagSaveReqVO bean = BeanUtils.toBean(attTagById, AttTagSaveReqVO.class);
        attTagService.updateAttTag(bean);
        return attTagAssetRelMapper.deleteById(id);
    }

    @Override
    public List<AttTagAssetRelRespDTO> getApiList(AttTagAssetRelReqDTO attApiCatReqDTO) {
        List<AttTagAssetRelDO> attTagAssetRelDOS = attTagAssetRelMapper.selectList();
        return BeanUtils.toBean(attTagAssetRelDOS, AttTagAssetRelRespDTO.class);
    }

    @Override
    public void deleteRelByUpdateTag(Long assetId) {
        List<AttTagAssetRelDO> attTagAssetRelDOS = attTagAssetRelMapper.selectList("asset_id", assetId);
        Map<Long, AttTagDO> collect = attTagService.list().stream().collect(Collectors.toMap(s -> s.getId(), Function.identity()));
        for (AttTagAssetRelDO attTagAssetRelDO : attTagAssetRelDOS) {
            AttTagDO attTagDO1 = collect.get(Convert.toLong(attTagAssetRelDO.getTagId()));
            attTagDO1.setAeestCount(attTagDO1.getAeestCount() - 1);
            AttTagSaveReqVO bean = BeanUtils.toBean(attTagDO1, AttTagSaveReqVO.class);
            attTagService.updateAttTag(bean);
        }
    }
}
