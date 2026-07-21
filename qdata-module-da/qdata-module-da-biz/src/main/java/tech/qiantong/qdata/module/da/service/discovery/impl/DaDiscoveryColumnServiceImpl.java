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

package tech.qiantong.qdata.module.da.service.discovery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryColumnMapper;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryColumnService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Discovery Column Service business layer processing
 *
 * @author qdata
 * @date 2025-02-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryColumnServiceImpl  extends ServiceImpl<DaDiscoveryColumnMapper,DaDiscoveryColumnDO> implements IDaDiscoveryColumnService {
    @Resource
    private DaDiscoveryColumnMapper daDiscoveryColumnMapper;

    @Override
    public PageResult<DaDiscoveryColumnDO> getDaDiscoveryColumnPage(DaDiscoveryColumnPageReqVO pageReqVO) {
        return daDiscoveryColumnMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaDiscoveryColumnDO> getDaDiscoveryColumnList(DaDiscoveryColumnPageReqVO reqVO) {
        MPJLambdaWrapper<DaDiscoveryColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DaDiscoveryColumnDO.class)
                .eq(reqVO.getTaskId() != null, DaDiscoveryColumnDO::getTaskId, reqVO.getTaskId())
                .eq( reqVO.getTableId() != null, DaDiscoveryColumnDO::getTableId, reqVO.getTableId())
                .like(StringUtils.isNotBlank(reqVO.getColumnName()), DaDiscoveryColumnDO::getColumnName, reqVO.getColumnName())
                .eq(StringUtils.isNotBlank(reqVO.getColumnComment()), DaDiscoveryColumnDO::getColumnComment, reqVO.getColumnComment())
                .eq(StringUtils.isNotBlank(reqVO.getColumnType()), DaDiscoveryColumnDO::getColumnType, reqVO.getColumnType())
                .eq(StringUtils.isNotBlank(reqVO.getNullableFlag()), DaDiscoveryColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eq(StringUtils.isNotBlank(reqVO.getPkFlag()), DaDiscoveryColumnDO::getPkFlag, reqVO.getPkFlag())
                .eq(StringUtils.isNotBlank(reqVO.getDefaultValue()), DaDiscoveryColumnDO::getDefaultValue, reqVO.getDefaultValue());

        return daDiscoveryColumnMapper.selectList(wrapper);
    }

    @Override
    public Long createDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO createReqVO) {
        DaDiscoveryColumnDO dictType = BeanUtils.toBean(createReqVO, DaDiscoveryColumnDO.class);
        daDiscoveryColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public Long createDaDiscoveryColumn(DaDiscoveryColumnDO createReqVO) {
        daDiscoveryColumnMapper.insert(createReqVO);
        return createReqVO.getId();
    }

    @Override
    public int updateDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO updateReqVO) {
        // Related validation

        // Update data discovery column
        DaDiscoveryColumnDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryColumnDO.class);
        return daDiscoveryColumnMapper.updateById(updateObj);
    }

    @Override
    public int updateDaDiscoveryColumn(DaDiscoveryColumnDO updateReqVO) {
        // Related validation

        // Update data discovery column
        return daDiscoveryColumnMapper.updateById(updateReqVO);
    }
    @Override
    public int removeDaDiscoveryColumn(Collection<Long> idList) {
        // Batch delete data discovery columns
        return daDiscoveryColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDiscoveryColumnDO getDaDiscoveryColumnById(Long id) {
        return daDiscoveryColumnMapper.selectById(id);
    }

    @Override
    public List<DaDiscoveryColumnDO> getDaDiscoveryColumnList() {
        return daDiscoveryColumnMapper.selectList();
    }

    @Override
    public Map<Long, DaDiscoveryColumnDO> getDaDiscoveryColumnMap() {
        List<DaDiscoveryColumnDO> daDiscoveryColumnList = daDiscoveryColumnMapper.selectList();
        return daDiscoveryColumnList.stream()
                .collect(Collectors.toMap(
                        DaDiscoveryColumnDO::getId,
                        daDiscoveryColumnDO -> daDiscoveryColumnDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data discovery column data
         *
         * @param importExcelList Data discovery column data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operating user
         * @return result
         */
        @Override
        public String importDaDiscoveryColumn(List<DaDiscoveryColumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaDiscoveryColumnRespVO respVO : importExcelList) {
                try {
                    DaDiscoveryColumnDO daDiscoveryColumnDO = BeanUtils.toBean(respVO, DaDiscoveryColumnDO.class);
                    Long daDiscoveryColumnId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daDiscoveryColumnId != null) {
                            DaDiscoveryColumnDO existingDaDiscoveryColumn = daDiscoveryColumnMapper.selectById(daDiscoveryColumnId);
                            if (existingDaDiscoveryColumn != null) {
                                daDiscoveryColumnMapper.updateById(daDiscoveryColumnDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data update successful, ID {0} {1} record.", daDiscoveryColumnId, MessageUtils.messageWithFallback("da.entity.discovery.column", "Data discovery column")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", daDiscoveryColumnId, MessageUtils.messageWithFallback("da.entity.discovery.column", "Data discovery column")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DaDiscoveryColumnDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daDiscoveryColumnId);
                        DaDiscoveryColumnDO existingDaDiscoveryColumn = daDiscoveryColumnMapper.selectOne(queryWrapper);
                        if (existingDaDiscoveryColumn == null) {
                            daDiscoveryColumnMapper.insert(daDiscoveryColumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", daDiscoveryColumnId, MessageUtils.messageWithFallback("da.entity.discovery.column", "Data discovery column")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", daDiscoveryColumnId, MessageUtils.messageWithFallback("da.entity.discovery.column", "Data discovery column")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
