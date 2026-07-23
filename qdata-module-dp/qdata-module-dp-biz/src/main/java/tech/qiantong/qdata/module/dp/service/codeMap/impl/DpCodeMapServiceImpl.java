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

package tech.qiantong.qdata.module.dp.service.codeMap.impl;

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
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;
import tech.qiantong.qdata.module.dp.dal.mapper.codeMap.DpCodeMapMapper;
import tech.qiantong.qdata.module.dp.service.codeMap.IDpCodeMapService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Element Code Map Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpCodeMapServiceImpl  extends ServiceImpl<DpCodeMapMapper,DpCodeMapDO> implements IDpCodeMapService {
    @Resource
    private DpCodeMapMapper dpCodeMapMapper;

    @Override
    public PageResult<DpCodeMapDO> getDpCodeMapPage(DpCodeMapPageReqVO pageReqVO) {
        return dpCodeMapMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpCodeMap(DpCodeMapSaveReqVO createReqVO) {
        DpCodeMapDO dictType = BeanUtils.toBean(createReqVO, DpCodeMapDO.class);
        dpCodeMapMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpCodeMap(DpCodeMapSaveReqVO updateReqVO) {
        // Related validation

        // Update data element code map
        DpCodeMapDO updateObj = BeanUtils.toBean(updateReqVO, DpCodeMapDO.class);
        return dpCodeMapMapper.updateById(updateObj);
    }
    @Override
    public int removeDpCodeMap(Collection<Long> idList) {
        // Batch delete data element code map
        return dpCodeMapMapper.deleteBatchIds(idList);
    }

    @Override
    public DpCodeMapDO getDpCodeMapById(Long id) {
        return dpCodeMapMapper.selectById(id);
    }

    @Override
    public List<DpCodeMapDO> getDpCodeMapList() {
        return dpCodeMapMapper.selectList();
    }

    @Override
    public Map<Long, DpCodeMapDO> getDpCodeMapMap() {
        List<DpCodeMapDO> dpCodeMapList = dpCodeMapMapper.selectList();
        return dpCodeMapList.stream()
                .collect(Collectors.toMap(
                        DpCodeMapDO::getId,
                        dpCodeMapDO -> dpCodeMapDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data element code map data
         *
         * @param importExcelList Data element code map data list
         * @param isUpdateSupport Whether to support update, if exists then update the data
         * @param operName Operator
         * @return Result
         */
        @Override
        public String importDpCodeMap(List<DpCodeMapRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpCodeMapRespVO respVO : importExcelList) {
                try {
                    DpCodeMapDO dpCodeMapDO = BeanUtils.toBean(respVO, DpCodeMapDO.class);
                    Long dpCodeMapId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpCodeMapId != null) {
                            DpCodeMapDO existingDpCodeMap = dpCodeMapMapper.selectById(dpCodeMapId);
                            if (existingDpCodeMap != null) {
                                dpCodeMapMapper.updateById(dpCodeMapDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dpCodeMapId, MessageUtils.messageWithFallback("dp.entity.code.mapping", "Data element code mapping")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dpCodeMapId, MessageUtils.messageWithFallback("dp.entity.code.mapping", "Data element code mapping")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DpCodeMapDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpCodeMapId);
                        DpCodeMapDO existingDpCodeMap = dpCodeMapMapper.selectOne(queryWrapper);
                        if (existingDpCodeMap == null) {
                            dpCodeMapMapper.insert(dpCodeMapDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dpCodeMapId, MessageUtils.messageWithFallback("dp.entity.code.mapping", "Data element code mapping")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dpCodeMapId, MessageUtils.messageWithFallback("dp.entity.code.mapping", "Data element code mapping")));
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
