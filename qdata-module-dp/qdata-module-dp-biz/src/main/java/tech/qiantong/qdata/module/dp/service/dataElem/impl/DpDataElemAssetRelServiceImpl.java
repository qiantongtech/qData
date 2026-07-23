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

package tech.qiantong.qdata.module.dp.service.dataElem.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemAssetRelMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemAssetRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Element Asset Relation Information Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemAssetRelServiceImpl  extends ServiceImpl<DpDataElemAssetRelMapper, DpDataElemAssetRelDO> implements IDpDataElemAssetRelService {
    @Resource
    private DpDataElemAssetRelMapper dpDataElemAssetRelMapper;

    @Override
    public PageResult<DpDataElemAssetRelDO> getDpDataElemAssetRelPage(DpDataElemAssetRelPageReqVO pageReqVO) {
        return dpDataElemAssetRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO createReqVO) {
        DpDataElemAssetRelDO dictType = BeanUtils.toBean(createReqVO, DpDataElemAssetRelDO.class);
        dpDataElemAssetRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemAssetRel(DpDataElemAssetRelSaveReqVO updateReqVO) {
        // Related validation

        // Update data element asset relation information
        DpDataElemAssetRelDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemAssetRelDO.class);
        return dpDataElemAssetRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDpDataElemAssetRel(Collection<Long> idList) {
        // Batch delete data element asset relation information
        return dpDataElemAssetRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemAssetRelDO getDpDataElemAssetRelById(Long id) {
        return dpDataElemAssetRelMapper.selectById(id);
    }

    @Override
    public List<DpDataElemAssetRelDO> getDpDataElemAssetRelList() {
        return dpDataElemAssetRelMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemAssetRelDO> getDpDataElemAssetRelMap() {
        List<DpDataElemAssetRelDO> dpDataElemAssetRelList = dpDataElemAssetRelMapper.selectList();
        return dpDataElemAssetRelList.stream()
                .collect(Collectors.toMap(
                        DpDataElemAssetRelDO::getId,
                        dpDataElemAssetRelDO -> dpDataElemAssetRelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data element asset relation information data
         *
         * @param importExcelList Data element asset relation information data list
         * @param isUpdateSupport Whether to support update, if exists then update the data
         * @param operName Operator
         * @return Result
         */
        @Override
        public String importDpDataElemAssetRel(List<DpDataElemAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpDataElemAssetRelRespVO respVO : importExcelList) {
                try {
                    DpDataElemAssetRelDO dpDataElemAssetRelDO = BeanUtils.toBean(respVO, DpDataElemAssetRelDO.class);
                    Long dpDataElemAssetRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpDataElemAssetRelId != null) {
                            DpDataElemAssetRelDO existingDpDataElemAssetRel = dpDataElemAssetRelMapper.selectById(dpDataElemAssetRelId);
                            if (existingDpDataElemAssetRel != null) {
                                dpDataElemAssetRelMapper.updateById(dpDataElemAssetRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dpDataElemAssetRelId, MessageUtils.messageWithFallback("dp.entity.element.asset.relation", "Data element-asset relation")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dpDataElemAssetRelId, MessageUtils.messageWithFallback("dp.entity.element.asset.relation", "Data element-asset relation")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DpDataElemAssetRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpDataElemAssetRelId);
                        DpDataElemAssetRelDO existingDpDataElemAssetRel = dpDataElemAssetRelMapper.selectOne(queryWrapper);
                        if (existingDpDataElemAssetRel == null) {
                            dpDataElemAssetRelMapper.insert(dpDataElemAssetRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dpDataElemAssetRelId, MessageUtils.messageWithFallback("dp.entity.element.asset.relation", "Data element-asset relation")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dpDataElemAssetRelId, MessageUtils.messageWithFallback("dp.entity.element.asset.relation", "Data element-asset relation")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("dp.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
