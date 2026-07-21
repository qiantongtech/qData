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

package tech.qiantong.qdata.module.dpp.service.etl.impl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlSqlTempMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSqlTempService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Integration SQL Template Service Business Layer Processing
 *
 * @author FXB
 * @date 2025-06-25
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlSqlTempServiceImpl extends ServiceImpl<DppEtlSqlTempMapper,DppEtlSqlTempDO> implements IDppEtlSqlTempService {
    @Resource
    private DppEtlSqlTempMapper dppEtlSqlTempMapper;

    @Override
    public PageResult<DppEtlSqlTempDO> getDppEtlSqlTempPage(DppEtlSqlTempPageReqVO pageReqVO) {
        return dppEtlSqlTempMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlSqlTemp(DppEtlSqlTempSaveReqVO createReqVO) {
        DppEtlSqlTempDO dictType = BeanUtils.toBean(createReqVO, DppEtlSqlTempDO.class);
        dppEtlSqlTempMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlSqlTemp(DppEtlSqlTempSaveReqVO updateReqVO) {
        // Related validation

        // Update data integration SQL template
        DppEtlSqlTempDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlSqlTempDO.class);
        return dppEtlSqlTempMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlSqlTemp(Collection<Long> idList) {
        // Batch delete data integration SQL template
        return dppEtlSqlTempMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlSqlTempDO getDppEtlSqlTempById(Long id) {
        return dppEtlSqlTempMapper.selectById(id);
    }

    @Override
    public List<DppEtlSqlTempDO> getDppEtlSqlTempList() {
        return dppEtlSqlTempMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlSqlTempDO> getDppEtlSqlTempMap() {
        List<DppEtlSqlTempDO> dppEtlSqlTempList = dppEtlSqlTempMapper.selectList();
        return dppEtlSqlTempList.stream()
                .collect(Collectors.toMap(
                        DppEtlSqlTempDO::getId,
                        dppEtlSqlTempDO -> dppEtlSqlTempDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data integration SQL template data
         *
         * @param importExcelList data integration SQL template data list
         * @param isUpdateSupport whether update is supported, if already exists, update the data
         * @param operName operator name
         * @return result
         */
        @Override
        public String importDppEtlSqlTemp(List<DppEtlSqlTempRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlSqlTempRespVO respVO : importExcelList) {
                try {
                    DppEtlSqlTempDO dppEtlSqlTempDO = BeanUtils.toBean(respVO, DppEtlSqlTempDO.class);
                    Long dppEtlSqlTempId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlSqlTempId != null) {
                            DppEtlSqlTempDO existingDppEtlSqlTemp = dppEtlSqlTempMapper.selectById(dppEtlSqlTempId);
                            if (existingDppEtlSqlTemp != null) {
                                dppEtlSqlTempMapper.updateById(dppEtlSqlTempDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppEtlSqlTempId, MessageUtils.messageWithFallback("dpp.entity.etl.sql.template", "Data integration SQL template")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppEtlSqlTempId, MessageUtils.messageWithFallback("dpp.entity.etl.sql.template", "Data integration SQL template")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppEtlSqlTempDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlSqlTempId);
                        DppEtlSqlTempDO existingDppEtlSqlTemp = dppEtlSqlTempMapper.selectOne(queryWrapper);
                        if (existingDppEtlSqlTemp == null) {
                            dppEtlSqlTempMapper.insert(dppEtlSqlTempDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppEtlSqlTempId, MessageUtils.messageWithFallback("dpp.entity.etl.sql.template", "Data integration SQL template")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppEtlSqlTempId, MessageUtils.messageWithFallback("dpp.entity.etl.sql.template", "Data integration SQL template")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
