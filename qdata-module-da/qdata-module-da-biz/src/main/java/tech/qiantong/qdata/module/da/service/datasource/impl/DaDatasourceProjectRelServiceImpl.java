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

package tech.qiantong.qdata.module.da.service.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;
import tech.qiantong.qdata.module.da.dal.mapper.datasource.DaDatasourceProjectRelMapper;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceProjectRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service business layer processing for datasource-project association relationships
 *
 * @author qdata
 * @date 2025-03-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDatasourceProjectRelServiceImpl extends ServiceImpl<DaDatasourceProjectRelMapper, DaDatasourceProjectRelDO> implements IDaDatasourceProjectRelService {
    @Resource
    private DaDatasourceProjectRelMapper daDatasourceProjectRelMapper;

    @Override
    public PageResult<DaDatasourceProjectRelDO> getDaDatasourceProjectRelPage(DaDatasourceProjectRelPageReqVO pageReqVO) {
        return daDatasourceProjectRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO createReqVO) {
        DaDatasourceProjectRelDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceProjectRelDO.class);
        daDatasourceProjectRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO updateReqVO) {
        // Related validation

        // Update datasource-project association relationship
        DaDatasourceProjectRelDO updateObj = BeanUtils.toBean(updateReqVO, DaDatasourceProjectRelDO.class);
        return daDatasourceProjectRelMapper.updateById(updateObj);
    }

    @Override
    public int removeDaDatasourceProjectRel(Collection<Long> idList) {
        // Batch delete datasource-project association relationships
        return daDatasourceProjectRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDatasourceProjectRelDO getDaDatasourceProjectRelById(Long id) {
        return daDatasourceProjectRelMapper.selectById(id);
    }

    @Override
    public List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList() {
        return daDatasourceProjectRelMapper.selectList();
    }

    @Override
    public List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList(DaDatasourceProjectRelDO daDatasourceProjectRelDO) {
        LambdaQueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(daDatasourceProjectRelDO.getDatasourceId() != null, DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO.getDatasourceId());
        queryWrapper.eq(daDatasourceProjectRelDO.getProjectId() != null, DaDatasourceProjectRelDO::getProjectId, daDatasourceProjectRelDO.getProjectId());
        queryWrapper.eq(StringUtils.isNotEmpty(daDatasourceProjectRelDO.getProjectCode()), DaDatasourceProjectRelDO::getProjectCode, daDatasourceProjectRelDO.getProjectCode());
        return daDatasourceProjectRelMapper.selectList(queryWrapper);
    }

    @Override
    public List<DaDatasourceProjectRelDO> getJoinProjectAndDatasource(DaDatasourceProjectRelDO daDatasourceProjectRelDO) {
        MPJLambdaWrapper<DaDatasourceProjectRelDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaDatasourceProjectRelDO.class)
                .select("u.datasource_name as datasourceName,d.name as projectName")
                .leftJoin("DA_DATASOURCE u on t.DATASOURCE_ID = u.id")
                .leftJoin("ATT_PROJECT d on t.PROJECT_ID = d.id")
                .eq("u.del_flag", "0")
                .eq("d.del_flag", "0")
                .eq(daDatasourceProjectRelDO.getDatasourceId() != null, DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO.getDatasourceId())
                .eq(daDatasourceProjectRelDO.getProjectId() != null, DaDatasourceProjectRelDO::getProjectId, daDatasourceProjectRelDO.getProjectId())
                .eq(StringUtils.isNotEmpty(daDatasourceProjectRelDO.getProjectCode()), DaDatasourceProjectRelDO::getProjectCode, daDatasourceProjectRelDO.getProjectCode());
        return daDatasourceProjectRelMapper.selectList(lambdaWrapper);
    }

    @Override
    public Map<Long, DaDatasourceProjectRelDO> getDaDatasourceProjectRelMap() {
        List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelMapper.selectList();
        return daDatasourceProjectRelList.stream()
                .collect(Collectors.toMap(
                        DaDatasourceProjectRelDO::getId,
                        daDatasourceProjectRelDO -> daDatasourceProjectRelDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import datasource-project association relationship data
     *
     * @param importExcelList list of datasource-project association relationship data
     * @param isUpdateSupport whether to support update; if already exists, then update the data
     * @param operName        operating user
     * @return result
     */
    @Override
    public String importDaDatasourceProjectRel(List<DaDatasourceProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDatasourceProjectRelRespVO respVO : importExcelList) {
            try {
                DaDatasourceProjectRelDO daDatasourceProjectRelDO = BeanUtils.toBean(respVO, DaDatasourceProjectRelDO.class);
                Long daDatasourceProjectRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceProjectRelId != null) {
                        DaDatasourceProjectRelDO existingDaDatasourceProjectRel = daDatasourceProjectRelMapper.selectById(daDatasourceProjectRelId);
                        if (existingDaDatasourceProjectRel != null) {
                            daDatasourceProjectRelMapper.updateById(daDatasourceProjectRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daDatasourceProjectRelId, MessageUtils.messageWithFallback("da.entity.datasource.project.relation", "Data source-project relation")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daDatasourceProjectRelId, MessageUtils.messageWithFallback("da.entity.datasource.project.relation", "Data source-project relation")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceProjectRelId);
                    DaDatasourceProjectRelDO existingDaDatasourceProjectRel = daDatasourceProjectRelMapper.selectOne(queryWrapper);
                    if (existingDaDatasourceProjectRel == null) {
                        daDatasourceProjectRelMapper.insert(daDatasourceProjectRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daDatasourceProjectRelId, MessageUtils.messageWithFallback("da.entity.datasource.project.relation", "Data source-project relation")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daDatasourceProjectRelId, MessageUtils.messageWithFallback("da.entity.datasource.project.relation", "Data source-project relation")));
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
