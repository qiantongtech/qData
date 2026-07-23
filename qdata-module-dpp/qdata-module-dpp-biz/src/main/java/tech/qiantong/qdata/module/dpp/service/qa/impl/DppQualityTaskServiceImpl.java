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

package tech.qiantong.qdata.module.dpp.service.qa.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.*;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessDefinition;
import tech.qiantong.qdata.api.ds.api.etl.ds.Schedule;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskDefinition;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.dpp.api.service.qa.DppQualityTaskApiService;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEvaluateLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskEvaluateService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskService;
import tech.qiantong.qdata.module.dpp.utils.DppTaskConverter;
import tech.qiantong.qdata.module.dpp.utils.model.TaskSaveReqInput;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tech.qiantong.qdata.common.core.domain.AjaxResult.error;
import static tech.qiantong.qdata.common.core.domain.AjaxResult.success;

/**
 * Data Quality Task Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskServiceImpl  extends ServiceImpl<DppQualityTaskMapper,DppQualityTaskDO> implements IDppQualityTaskService, DppQualityTaskApiService {

    private static String projectCode;

    @Value("${path.quality_url}")
    private String url;

    @Value("${ds.http_quality_projectCode}")
    private void setDefaultProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    @Resource
    private DppQualityTaskMapper dppQualityTaskMapper;

    @Resource
    private IDppQualityTaskEvaluateService dppQualityTaskEvaluateService;
    @Resource
    private IDppQualityTaskObjService dppQualityTaskObjService;
    @Resource
    private IDaDatasourceApiService daDatasourceApiService;
    @Resource
    private IDsEtlTaskService dsEtlTaskService;


    @Resource
    private IDsEtlSchedulerService iDsEtlSchedulerService;


    @Resource
    private IDsEtlNodeService dsEtlNodeService;

    @Resource
    private IDppQualityLogService dppQualityLogService;

    @Resource
    private IDppEvaluateLogService dppEvaluateLogService;

    @Override
    public PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO) {
        return dppQualityTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO) {
        String assetFlag = createReqVO.getAssetFlag();
        if(StringUtils.equals("1",assetFlag)){
            MPJLambdaWrapper<DppQualityTaskDO> wrapper = new MPJLambdaWrapper<>();
            wrapper.selectAll(DppQualityTaskDO.class)
                    .eq(DppQualityTaskDO::getAssetFlag,"1")
                    .eq(DppQualityTaskDO::getAssetId,createReqVO.getAssetId());
            List<DppQualityTaskDO> taskDO = dppQualityTaskMapper.selectList(wrapper);
            if(CollectionUtils.isNotEmpty(taskDO)){
                return taskDO.get(0).getId();
            }
        }

        DppQualityTaskDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskDO.class);
        dppQualityTaskMapper.insert(dictType);
        List<DppQualityTaskObjSaveReqVO> dppQualityTaskObjSaveReqVO = createReqVO.getDppQualityTaskObjSaveReqVO();
        for (DppQualityTaskObjSaveReqVO qualityTaskObjSaveReqVO : dppQualityTaskObjSaveReqVO) {
            qualityTaskObjSaveReqVO.setTaskId(dictType.getId());
            Long dppQualityTaskObj = dppQualityTaskObjService.createDppQualityTaskObj(qualityTaskObjSaveReqVO);
            qualityTaskObjSaveReqVO.setId(dppQualityTaskObj);
        }
        Map<String, DppQualityTaskObjSaveReqVO> collect = dppQualityTaskObjSaveReqVO.stream().collect(Collectors.toMap(s -> s.getDatasourceId() + s.getTableName(), Function.identity()));
        List<DppQualityTaskEvaluateSaveReqVO> dppQualityTaskEvaluateSaveReqVO = createReqVO.getDppQualityTaskEvaluateSaveReqVO();
        if (dppQualityTaskEvaluateSaveReqVO != null) {
            for (DppQualityTaskEvaluateSaveReqVO qualityTaskEvaluateSaveReqVO : dppQualityTaskEvaluateSaveReqVO) {
                DppQualityTaskObjSaveReqVO dppQualityTaskObjSaveReqVO1 = collect.get(qualityTaskEvaluateSaveReqVO.getDatasourceId() + qualityTaskEvaluateSaveReqVO.getTableName());
                if (dppQualityTaskObjSaveReqVO1 != null) {
                    qualityTaskEvaluateSaveReqVO.setTaskId(dictType.getId());
                    qualityTaskEvaluateSaveReqVO.setObjId(dppQualityTaskObjSaveReqVO1.getId());
                    qualityTaskEvaluateSaveReqVO.setObjName(dppQualityTaskObjSaveReqVO1.getName());
                    handleCharacterValidationRule(qualityTaskEvaluateSaveReqVO);
                    dppQualityTaskEvaluateService.createDppQualityTaskEvaluate(qualityTaskEvaluateSaveReqVO);
                }
            }
        }

        return dictType.getId();
    }

    @Override
    public int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO) {
        // Validate
        DppQualityTaskDO dictType = BeanUtils.toBean(updateReqVO, DppQualityTaskDO.class);
        List<DppQualityTaskObjSaveReqVO> dppQualityTaskObjSaveReqVO = updateReqVO.getDppQualityTaskObjSaveReqVO();
        for (DppQualityTaskObjSaveReqVO qualityTaskObjSaveReqVO : dppQualityTaskObjSaveReqVO) {
            qualityTaskObjSaveReqVO.setTaskId(dictType.getId());
            if (qualityTaskObjSaveReqVO.getId() != null) {
                dppQualityTaskObjService.updateDppQualityTaskObj(qualityTaskObjSaveReqVO);
            } else {
                Long dppQualityTaskObj = dppQualityTaskObjService.createDppQualityTaskObj(qualityTaskObjSaveReqVO);
                qualityTaskObjSaveReqVO.setId(dppQualityTaskObj);
            }
        }
        Map<String, DppQualityTaskObjSaveReqVO> collect = dppQualityTaskObjSaveReqVO.stream().collect(Collectors.toMap(s -> s.getDatasourceId() + s.getTableName(), Function.identity()));
        List<DppQualityTaskEvaluateSaveReqVO> dppQualityTaskEvaluateSaveReqVO = updateReqVO.getDppQualityTaskEvaluateSaveReqVO();
        if (dppQualityTaskEvaluateSaveReqVO != null) {
            for (DppQualityTaskEvaluateSaveReqVO qualityTaskEvaluateSaveReqVO : dppQualityTaskEvaluateSaveReqVO) {
                DppQualityTaskObjSaveReqVO dppQualityTaskObjSaveReqVO1 = collect.get(qualityTaskEvaluateSaveReqVO.getDatasourceId() + qualityTaskEvaluateSaveReqVO.getTableName());
                if (dppQualityTaskObjSaveReqVO1 != null) {
                    qualityTaskEvaluateSaveReqVO.setObjId(dppQualityTaskObjSaveReqVO1.getId());
                    qualityTaskEvaluateSaveReqVO.setObjName(dppQualityTaskObjSaveReqVO1.getName());
                }
                handleCharacterValidationRule(qualityTaskEvaluateSaveReqVO);
                if (qualityTaskEvaluateSaveReqVO.getId() != null) {
                    dppQualityTaskEvaluateService.updateDppQualityTaskEvaluate(qualityTaskEvaluateSaveReqVO);
                } else {
                    qualityTaskEvaluateSaveReqVO.setTaskId(dictType.getId());
                    dppQualityTaskEvaluateService.createDppQualityTaskEvaluate(qualityTaskEvaluateSaveReqVO);
                }
            }
        }
        return dppQualityTaskMapper.updateById(dictType);
    }
    @Override
    public int removeDppQualityTask(Collection<Long> idList) {
        // Batch delete data quality tasks
        for (Long id : idList) {
            // Query DaDiscoveryTaskDO details
            DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);
            if (dppQualityTaskDO != null &&
                    (dppQualityTaskDO.getSystemJobId() != null || !StringUtils.equals("0",dppQualityTaskDO.getTaskCode())) ) {
                // Extract systemJobId
                if(StringUtils.equals("0",dppQualityTaskDO.getStatus())){
                    throw new ServiceException("dpp.error.task.online.delete", "Online task cannot be deleted, please offline first!");
                }
                DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.deleteTask(projectCode, dppQualityTaskDO.getTaskCode());
            }
        }
        return dppQualityTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskRespVO getQualityTaskAsset(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO) {
        MPJLambdaWrapper<DppQualityTaskDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppQualityTaskDO.class)
                .eq(DppQualityTaskDO::getAssetFlag,"1")
                .eq(DppQualityTaskDO::getAssetId,dppQualityTaskAssetReqVO.getAssetId());
        DppQualityTaskDO taskDO = dppQualityTaskMapper.selectOne(wrapper);
        if(taskDO == null){
            return null;
        }
        DppQualityTaskRespVO dppQualityTaskRespVO = buildQualityTaskDetail(taskDO);
        dppQualityTaskAssetReqVO.setId(taskDO.getId());
        DppQualityLogDO log = dppQualityLogService.getDppQualityLogById(dppQualityTaskAssetReqVO);
        if(log == null){
            // Set score and problem count
            dppQualityTaskRespVO.setScore(0L);
            dppQualityTaskRespVO.setProblemData(0L);
            dppQualityTaskRespVO.setLogId(null);
            dppQualityTaskRespVO.setLastExecuteTime(null);
            return dppQualityTaskRespVO;
        }

        Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(log.getId()));

        // Get total and problem count (ensure null safety)
        Long total = map.get("total") == null ? 0L : (Long) map.get("total");
        Long problemTotal = map.get("problemTotal") == null ? 0L : (Long) map.get("problemTotal");

        // Calculate quality score (percentage, two decimal places)
        BigDecimal score = BigDecimal.ZERO;
        if (total > 0) {
            score = BigDecimal.valueOf(total - problemTotal)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }

        // Set score and problem count
        dppQualityTaskRespVO.setScore(score.longValue());
        dppQualityTaskRespVO.setProblemData(problemTotal);
        dppQualityTaskRespVO.setLogId(log.getId());
        dppQualityTaskRespVO.setLastExecuteTime(log.getStartTime());
        return dppQualityTaskRespVO;
    }

    @Override
    public DppQualityTaskRespVO getDppQualityTaskById(Long id) {
        DppQualityTaskDO taskDO = dppQualityTaskMapper.selectById(id);
        return taskDO != null ? buildQualityTaskDetail(taskDO) : null;
    }

    private DppQualityTaskRespVO buildQualityTaskDetail(DppQualityTaskDO dppQualityTaskDO) {
        DppQualityTaskRespVO bean = BeanUtils.toBean(dppQualityTaskDO, DppQualityTaskRespVO.class);

        // Data object list
        LambdaQueryWrapperX<DppQualityTaskObjDO> objectLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        objectLambdaQueryWrapperX.eq(DppQualityTaskObjDO::getTaskId , dppQualityTaskDO.getId());
        List<DppQualityTaskObjDO> list = dppQualityTaskObjService.list(objectLambdaQueryWrapperX);

        List<DppQualityTaskObjRespVO> newList = new ArrayList<>();
        for (DppQualityTaskObjDO obj : list) {
            DaDatasourceRespDTO ds = daDatasourceApiService.getDatasourceById(obj.getDatasourceId());
            DppQualityTaskObjRespVO vo = BeanUtils.toBean(obj, DppQualityTaskObjRespVO.class);
            if (ds != null) {
                vo.setDatasourceType(ds.getDatasourceType());
                vo.setDatasourceConfig(ds.getDatasourceConfig());
            }
            newList.add(vo);
        }

        // Rule list
        LambdaQueryWrapperX<DppQualityTaskEvaluateDO> evaWrapper = new LambdaQueryWrapperX<>();
        evaWrapper.eq(DppQualityTaskEvaluateDO::getTaskId , dppQualityTaskDO.getId());
        List<DppQualityTaskEvaluateDO> evaList = dppQualityTaskEvaluateService.list(evaWrapper);

        List<DppQualityTaskEvaluateRespVO> evaRespList = new ArrayList<>();
        for (DppQualityTaskEvaluateDO eva : evaList) {
            handleCharacterValidationRule(eva);
            evaRespList.add(BeanUtils.toBean(eva, DppQualityTaskEvaluateRespVO.class));
        }

        bean.setDppQualityTaskObjSaveReqVO(newList);
        bean.setDppQualityTaskEvaluateRespVOS(evaRespList);
        return bean;
    }

    public DppQualityTaskRespVO getDaDiscoveryTaskById(Long id) {

        MPJLambdaWrapper<DppQualityTaskDO> mpjLambdaWrapper = new MPJLambdaWrapper();
        mpjLambdaWrapper.selectAll(DppQualityTaskDO.class)
                .select("t2.name AS catName")
                .leftJoin("ATT_QUALITY_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .eq(DppQualityTaskDO::getId, id);
        DppQualityTaskDO daDiscoveryTaskDO =  dppQualityTaskMapper.selectJoinOne(DppQualityTaskDO.class, mpjLambdaWrapper);

        DppQualityTaskRespVO bean = BeanUtils.toBean(daDiscoveryTaskDO, DppQualityTaskRespVO.class);


//        DaDatasourceRespDTO daDatasourceById = daDatasourceApiService.getDatasourceById(bean.getDatasourceId());
//        daDatasourceById = daDatasourceById == null ? new DaDatasourceRespDTO():daDatasourceById;
//        bean.setDatasourceName(daDatasourceById.getDatasourceName());
//        bean.setDatasourceType(daDatasourceById.getDatasourceType());
//        bean.setIp(daDatasourceById.getIp());
//
//        List<DppQualityTaskObjDO> daDiscoveryTableDOList = fetchDiscoveryTableList(bean);
//        daDiscoveryTableDOList = daDiscoveryTableDOList == null ? new ArrayList<>():daDiscoveryTableDOList;


//        long countPending = daDiscoveryTableDOList.stream()
//                .filter(item -> StringUtils.equals("1",item.get()))
//                .count();
//
//        long countSubmitted = daDiscoveryTableDOList.stream()
//                .filter(item -> StringUtils.equals("2",item.getStatus()))
//                .count();
//
//        //0:No, 1:Yes
//        long countIgnoreFlag = daDiscoveryTableDOList.stream()
//                .filter(item -> StringUtils.equals("1",item.getIgnoreFlag()))
//                .count();
//        bean.setCountPending(countPending);
//        bean.setCountSubmitted(countSubmitted);
//        bean.setCountIgnoreFlag(countIgnoreFlag);


//        Long systemJobId = bean.getSystemJobId();
//        SysJob sysJob = iSysJobService.selectJobById(systemJobId);
//        sysJob = sysJob == null ? new SysJob():sysJob;
//        bean.setMisfirePolicy(sysJob.getMisfirePolicy());
//        bean.setJobGroup(sysJob.getJobGroup());
//        bean.setConcurrent(sysJob.getConcurrent());


        return bean;
    }

    private List<DppQualityTaskObjDO> fetchDiscoveryTableList(DppQualityTaskRespVO daDiscoveryTaskDO) {
        LambdaQueryWrapperX<DppQualityTaskObjDO> objectLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        objectLambdaQueryWrapperX.eqIfPresent(DppQualityTaskObjDO::getTaskId , daDiscoveryTaskDO.getId());
        return dppQualityTaskObjService.list(objectLambdaQueryWrapperX);
    }

    @Override
    public List<DppQualityTaskDO> getDppQualityTaskList() {
        return dppQualityTaskMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityTaskDO> getDppQualityTaskMap() {
        List<DppQualityTaskDO> dppQualityTaskList = dppQualityTaskMapper.selectList();
        return dppQualityTaskList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskDO::getId,
                        dppQualityTaskDO -> dppQualityTaskDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data quality task data
         *
         * @param importExcelList data quality task data list
         * @param isUpdateSupport whether to support update; if already exists, update the data
         * @param operName operator user
         * @return result
         */
        @Override
        public String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppQualityTaskRespVO respVO : importExcelList) {
                try {
                    DppQualityTaskDO dppQualityTaskDO = BeanUtils.toBean(respVO, DppQualityTaskDO.class);
                    Long dppQualityTaskId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppQualityTaskId != null) {
                            DppQualityTaskDO existingDppQualityTask = dppQualityTaskMapper.selectById(dppQualityTaskId);
                            if (existingDppQualityTask != null) {
                                dppQualityTaskMapper.updateById(dppQualityTaskDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppQualityTaskId, MessageUtils.messageWithFallback("dpp.entity.quality.task", "Data quality task")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppQualityTaskId, MessageUtils.messageWithFallback("dpp.entity.quality.task", "Data quality task")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppQualityTaskDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskId);
                        DppQualityTaskDO existingDppQualityTask = dppQualityTaskMapper.selectOne(queryWrapper);
                        if (existingDppQualityTask == null) {
                            dppQualityTaskMapper.insert(dppQualityTaskDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppQualityTaskId, MessageUtils.messageWithFallback("dpp.entity.quality.task", "Data quality task")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppQualityTaskId, MessageUtils.messageWithFallback("dpp.entity.quality.task", "Data quality task")));
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

    @Override
    public String verifyInterfaceValue(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        // Handle regex
//        JSONObject jsonObject = JSONObject.parseObject(dppQualityTaskEvaluate.getRule());
//        List<String> lists = jsonObject.getList("allowedChars", String.class);
//        String s = this.validateInputWithRegex(lists);

        Map<String, Object> map = this.buildRuleParamMap(dppQualityTaskEvaluate);
        map.put("dataId", dppQualityTaskEvaluate.getDatasourceId());
        map.put("inputValue", dppQualityTaskEvaluate.getTitle());
        List<HeaderEntity> headers = new ArrayList<>();
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setKey("Content-Type");
        headerEntity.setValue("application/json");
        headers.add(headerEntity);  // Set request headers
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(url + "/generateDataCheck", map, headers);
            System.out.println(responseObject.toString());
            // Cast and parse to JSONObject
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            // Extract data
            String data = json.getString("data");
            if (StringUtils.equals("1",data)) {
                return MessageUtils.messageWithFallback("dpp.quality.monitor.success",
                        "Data monitoring succeeded for {0}", dppQualityTaskEvaluate.getTitle());
            }
            return MessageUtils.messageWithFallback("dpp.quality.monitor.rule.mismatch",
                    "{0} does not comply with the rule", dppQualityTaskEvaluate.getTitle());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AjaxResult startDppQualityTask(Long id) {
        DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);
        if(dppQualityTaskDO == null){
            return error(MessageUtils.messageWithFallback(
                    "dpp.error.task.notfound.refresh", "Task does not exist; refresh and try again"));
        }
        if (!StringUtils.equals("0",dppQualityTaskDO.getStatus())){
            return error(MessageUtils.messageWithFallback(
                    "dpp.error.task.status.invalid", "The task status is invalid; refresh and try again"));
        }

        DsStartTaskReqDTO dsStartTaskReqDTO = DppTaskConverter.createDsStartTaskReqDTO(dppQualityTaskDO.getTaskCode());

        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.startTask(dsStartTaskReqDTO, projectCode);

        return dsStatusRespDTO.getSuccess() ? success() : error(dsStatusRespDTO.getMsg());
    }

    @Override
    public boolean updateDppQualityTaskStatus(DppQualityTaskSaveReqVO daDiscoveryTask) {
        DppQualityTaskRespVO dppQualityTaskById = this.getDaDiscoveryTaskById(daDiscoveryTask.getId());
        String daDiscoveryTaskStatus = daDiscoveryTask.getStatus();

        validateTaskStatus(dppQualityTaskById, daDiscoveryTaskStatus);

        daDiscoveryTask.setCycle(dppQualityTaskById.getCycle());
        Long systemJobId = dppQualityTaskById.getSystemJobId();
        if (StringUtils.equals(daDiscoveryTaskStatus, dppQualityTaskById.getStatus())) {
            return true;
        }

        if (StringUtils.equals("1", daDiscoveryTaskStatus)) {
            handleOfflineTask(dppQualityTaskById, systemJobId, daDiscoveryTask);
            return true;
        }

        handleOnlineTask(dppQualityTaskById, systemJobId, daDiscoveryTask);

        updateTaskStatusAndScheduler(daDiscoveryTask, systemJobId);

        return true;
    }

    @Override
    public JSONObject validationErrorDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        Map<String, Object> objectObjectHashMap =  this.buildRuleParamMap(dppQualityTaskEvaluate);
        List<HeaderEntity> headers = new ArrayList<>();
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setKey("Content-Type");
        headerEntity.setValue("application/json");
        headers.add(headerEntity);  // Set request headers
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(url + "/generateValidationErrorDataSql", objectObjectHashMap, headers);
            System.out.println(responseObject.toString());
            // Cast and parse to JSONObject
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            // Extract data
            JSONObject data = json.getJSONObject("data");
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject validationValidDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        Map<String, Object> objectObjectHashMap =  this.buildRuleParamMap(dppQualityTaskEvaluate);
        List<HeaderEntity> headers = new ArrayList<>();
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setKey("Content-Type");
        headerEntity.setValue("application/json");
        headers.add(headerEntity);  // Set request headers
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(url + "/generateValidationValidDataSql", objectObjectHashMap, headers);
            System.out.println(responseObject.toString());
            // Cast and parse to JSONObject
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            // Extract data
            JSONObject data = json.getJSONObject("data");
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateDaDiscoveryTaskCronExpression(DppQualityTaskSaveReqVO daDiscoveryTask) {
        DppQualityTaskRespVO dppQualityTaskById = this.getDppQualityTaskById(daDiscoveryTask.getId());
        Long systemJobId = dppQualityTaskById.getSystemJobId();
        if(systemJobId != null){
            try {
                // Create scheduler (only available after task is published)
                DsSchedulerUpdateReqDTO schedulerUpdateRequest = DppTaskConverter.createSchedulerUpdateRequest(systemJobId, daDiscoveryTask.getCycle(), dppQualityTaskById.getTaskCode());
                DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
                if(dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()){
                    daDiscoveryTask.setTaskId(dppQualityTaskById.getTaskId());
                    daDiscoveryTask.setTaskCode(String.valueOf(dppQualityTaskById.getTaskCode()));
                    daDiscoveryTask.setNodeId(dppQualityTaskById.getNodeId());
                    daDiscoveryTask.setNodeCode(String.valueOf(dppQualityTaskById.getNodeCode()));
                    createSchedulerIfNeeded(daDiscoveryTask);
                }else {
                    Schedule schedule = dsSchedulerRespDTO.getData();
                    daDiscoveryTask.setSystemJobId(schedule.getId());
                }
            } catch (Exception e){
                throw new ServiceException("dpp.error.schedule.period.update", "Failed to modify schedule period, please contact admin!");

            }
        }

        // Update data discovery task
        DppQualityTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DppQualityTaskDO.class);
        dppQualityTaskMapper.updateById(updateObj);
//        this.updateDaDiscoveryTask(daDiscoveryTask);
        return true;
    }

    private void validateTaskStatus(DppQualityTaskRespVO daDiscoveryTaskById, String daDiscoveryTaskStatus) {
        if (daDiscoveryTaskById == null || daDiscoveryTaskStatus == null) {
            throw new ServiceException("dpp.error.template.scheduler.missing", "Task template error, scheduler info not found!");
        }
    }

    private void handleOfflineTask(DppQualityTaskRespVO daDiscoveryTaskById, Long systemJobId, DppQualityTaskSaveReqVO daDiscoveryTask) {
        if(daDiscoveryTaskById.getSystemJobId() != null &&  systemJobId > 0){
            DsStatusRespDTO respDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(projectCode), daDiscoveryTaskById.getTaskCode());
            if (respDTO == null || !respDTO.getSuccess()) {
                throw new ServiceException("dpp.error.task.publish.fail", "Failed to publish or offline task!");
            }

            DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(projectCode, systemJobId);
            if (!offlined.getData()) {
                throw new ServiceException("dpp.error.scheduler.offline", "Failed to offline scheduler!");
            }
        }

        // Update data discovery task
        DppQualityTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DppQualityTaskDO.class);
        dppQualityTaskMapper.updateById(updateObj);
    }

    private void handleOnlineTask(DppQualityTaskRespVO daDiscoveryTaskById, Long systemJobId, DppQualityTaskSaveReqVO daDiscoveryTask) {
        if (systemJobId == null || systemJobId < 1) {
            createNewProcessDefinition(daDiscoveryTaskById, daDiscoveryTask);
        } else if (daDiscoveryTaskById.getId() != null) {
            updateExistingProcessDefinition(daDiscoveryTaskById, daDiscoveryTask);
        }
    }

    private void createNewProcessDefinition(DppQualityTaskRespVO daDiscoveryTaskById, DppQualityTaskSaveReqVO daDiscoveryTask) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(daDiscoveryTaskById.getTaskName() + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", daDiscoveryTaskById.getId());
        input.setId(daDiscoveryTaskById.getId());
        ProcessDefinition definition = this.createProcessDefinition(input);
        TaskDefinition firstTaskDefinition = DppTaskConverter.getFirstTaskDefinition(definition);

        daDiscoveryTask.setTaskId(definition.getId());
        daDiscoveryTask.setTaskCode(String.valueOf(definition.getCode()));
        daDiscoveryTask.setNodeId(firstTaskDefinition.getId());
        daDiscoveryTask.setNodeCode(String.valueOf(firstTaskDefinition.getCode()));
    }

    private void updateExistingProcessDefinition(DppQualityTaskRespVO daDiscoveryTaskById, DppQualityTaskSaveReqVO daDiscoveryTask) {
        TaskSaveReqInput input = new TaskSaveReqInput();
        input.setName(daDiscoveryTaskById.getTaskName() + StringUtils.generateRandomString());
        input.addHttpParam("id", "BODY", daDiscoveryTaskById.getId());
        input.setId(daDiscoveryTaskById.getId());

        input.setTaskId(daDiscoveryTaskById.getTaskId());
        input.setTaskCode(String.valueOf(daDiscoveryTaskById.getTaskCode()));
        input.setNodeId(daDiscoveryTaskById.getNodeId());
        input.setNodeCode(String.valueOf(daDiscoveryTaskById.getNodeCode()));

        ProcessDefinition definition = this.updateProcessDefinition(input);
        TaskDefinition firstTaskDefinition = DppTaskConverter.getFirstTaskDefinition(definition);

        daDiscoveryTask.setTaskId(definition.getId());
        daDiscoveryTask.setTaskCode(String.valueOf(definition.getCode()));
        daDiscoveryTask.setNodeId(firstTaskDefinition.getId());
        daDiscoveryTask.setNodeCode(String.valueOf(firstTaskDefinition.getCode()));
    }


    private void updateTaskStatusAndScheduler(DppQualityTaskSaveReqVO daDiscoveryTask, Long systemJobId) {
        DsStatusRespDTO dsStatusRespDTO = dsEtlTaskService.releaseTask("ONLINE", String.valueOf(projectCode), daDiscoveryTask.getTaskCode());
        if (dsStatusRespDTO == null || !dsStatusRespDTO.getSuccess()) {
            throw new ServiceException("dpp.error.task.publish.fail", "Failed to publish or offline task!");
        }

        if (systemJobId != null && systemJobId > 0) {
            updateExistingScheduler(daDiscoveryTask, systemJobId);
        } else {
            createNewScheduler(daDiscoveryTask);
        }

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(projectCode, daDiscoveryTask.getSystemJobId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("dpp.error.scheduler.online", "Failed to online scheduler!");
        }

        // Update data discovery task
        DppQualityTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DppQualityTaskDO.class);
        dppQualityTaskMapper.updateById(updateObj);
    }


    private void updateExistingScheduler(DppQualityTaskSaveReqVO daDiscoveryTask, Long systemJobId) {
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = DppTaskConverter.createSchedulerUpdateRequest(systemJobId, daDiscoveryTask.getCycle(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            createSchedulerIfNeeded(daDiscoveryTask);
        } else {
            Schedule schedule = dsSchedulerRespDTO.getData();
            daDiscoveryTask.setSystemJobId(schedule.getId());
        }
    }

    private void createNewScheduler(DppQualityTaskSaveReqVO daDiscoveryTask) {
        DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DppTaskConverter.createSchedulerRequest(daDiscoveryTask.getCycle(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO dsSchedulerRespDTO = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
        if (dsSchedulerRespDTO == null || !dsSchedulerRespDTO.getSuccess()) {
            createSchedulerIfNeeded(daDiscoveryTask);
        } else {
            Schedule schedule = dsSchedulerRespDTO.getData();
            daDiscoveryTask.setSystemJobId(schedule.getId());
        }
    }


    private void createSchedulerIfNeeded(DppQualityTaskSaveReqVO daDiscoveryTask) {
        DsSchedulerRespDTO byTaskCode = iDsEtlSchedulerService.getByTaskCode(String.valueOf(projectCode), daDiscoveryTask.getTaskCode());
        if (byTaskCode == null || !byTaskCode.getSuccess()) {
            // Create scheduler (only available after task is published)
            DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DppTaskConverter.createSchedulerRequest(daDiscoveryTask.getCycle(),daDiscoveryTask.getTaskCode());
            DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
            if(saveScheduler == null || !saveScheduler.getSuccess()){
                throw new ServiceException("dpp.error.scheduler.create", "Failed to create scheduler!");
            }
            Schedule schedule = saveScheduler.getData();

            daDiscoveryTask.setSystemJobId(schedule.getId());
            return;
        }
        Schedule schedule = byTaskCode.getData();
        daDiscoveryTask.setSystemJobId(schedule.getId());
        DsSchedulerUpdateReqDTO schedulerUpdateRequest = DppTaskConverter.createSchedulerUpdateRequest(schedule.getId(), daDiscoveryTask.getCycle(), daDiscoveryTask.getTaskCode());
        DsSchedulerRespDTO updated = iDsEtlSchedulerService.updateScheduler(schedulerUpdateRequest, String.valueOf(projectCode));
        if (updated == null || !updated.getSuccess()) {
            throw new ServiceException("dpp.error.scheduler.update", "Failed to update scheduler!");
        }
    }

    public ProcessDefinition updateProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DppTaskConverter.stringToLong(projectCode));

        input.setNodeCode(DppTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DppTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO,projectCode,input.getTaskCode() );

        if (!task.getSuccess()) {
            throw new ServiceException("dpp.error.task.status.update", "Failed to update task status, please contact admin"); // Throw task definition creation error exception
        }
        ProcessDefinition data = task.getData();
        return data; // Return creation result
    }

    public ProcessDefinition createProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DppTaskConverter.stringToLong(projectCode));

        input.setNodeCode(DppTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DppTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO,DppTaskConverter.stringToLong(projectCode) );

        if (!task.getSuccess()) {
            throw new ServiceException("dpp.error.task.status.update", "Failed to update task status, please contact admin"); // Throw task definition creation error exception
        }
        ProcessDefinition data = task.getData();
        return data; // Return creation result
    }

    public Long getNodeUniqueKey(Long projectCode) {
        try {
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(projectCode);
            return dsNodeGenCodeRespDTO.getData().get(0);
        } catch (Exception e){
            throw new ServiceException("dpp.error.task.status.update", "Failed to update task status, please contact admin"); // Throw task definition creation error exception
        }
    }



    /**
     * Concatenate regex expression
     * @param value
     * @return
     */
    public static String validateInputWithRegex(List<String> value) {
        Map<String, String> map = new HashMap<>();
        // Digits
        map.put("1", "0-9");
        // Letters
        map.put("2", "a-zA-Z");
        // Whitespace
        map.put("3", "\\s");
        // Special symbols
//        map.put("4", "!@#$%^&*(),.?" +'"' +":{}|<>");
//        map.put("4", "!\"#$%&'()*+,\\-./:;<=>?@[\\\\]^_`{|}~");
//        map.put("4", "!\"#$%&'()*+,\\-./:;<=>?@\\[\\]\\^_`{|}~");
        map.put("4", "[:punct:]");
//        map.put("4", "\\p{P}\\p{S}");
        // !@#$%^&*(),.?":{}|<>
        String s1 = "";
        for (String s : value) {
            s1 += map.get(s);

        }
        s1 = "^[" + s1 + "]+$";
        return s1;
    }

    /**
     * @param dppQualityTaskEvaluate
     * @return
     */
    public static Map<String, Object> buildRuleParamMap(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        Map<String, Object> paramMap = new HashMap<>();

        // 1. Datasource ID
        paramMap.put("dataId", dppQualityTaskEvaluate.getDatasourceId());

        // 2. Table name
        paramMap.put("tableName", dppQualityTaskEvaluate.getTableName());

        // 3. Rule type
        paramMap.put("ruleType", dppQualityTaskEvaluate.getRuleType());

        // 4. Pagination info (temporarily hardcoded, can be adjusted if pagination params are added later)
        paramMap.put("pageNum", dppQualityTaskEvaluate.getPageNum());
        paramMap.put("pageSize", dppQualityTaskEvaluate.getPageSize());


        String stringObjectMap = buildCharacterValidationRule(dppQualityTaskEvaluate.getRule(), dppQualityTaskEvaluate.getRuleType());

        // 5. Rule config
        paramMap.put("config",  JSONUtils.convertTaskDefinitionJsonMap(stringObjectMap));

        // 6. Evaluation field
        paramMap.put("evaColumn", dppQualityTaskEvaluate.getEvaColumn());

        // 7. Where clause
        paramMap.put("whereClause", dppQualityTaskEvaluate.getWhereClause());

        return paramMap;
    }
    /**
     * Handle CHARACTER_VALIDATION rule
     * Compatible with both SaveReqVO and DO types
     */
    public static void handleCharacterValidationRule(DppQualityTaskEvaluateSaveReqVO qualityTaskEvaluateSaveReqVO) {
        if (qualityTaskEvaluateSaveReqVO == null) {
            return;
        }
        String newRule = buildCharacterValidationRule(
                qualityTaskEvaluateSaveReqVO.getRule(),
                qualityTaskEvaluateSaveReqVO.getRuleType()
        );
        if (newRule != null) {
            qualityTaskEvaluateSaveReqVO.setRule(newRule);
        }
    }

    public static void handleCharacterValidationRule(DppQualityTaskEvaluateDO evaluateDO) {
        if (evaluateDO == null) {
            return;
        }
        String newRule = buildCharacterValidationRule(
                evaluateDO.getRule(),
                evaluateDO.getRuleType()
        );
        if (newRule != null) {
            evaluateDO.setRule(newRule);
        }
    }

    /**
     * Common internal logic
     */
    private static String buildCharacterValidationRule(String ruleJson, String ruleType) {
        if (StringUtils.isBlank(ruleJson) || !"CHARACTER_VALIDATION".equals(ruleType)) {
            return ruleJson;
        }

        JSONObject jsonObject = JSONObject.parseObject(ruleJson);
        String useRegexFlag = MapUtils.getString(jsonObject, "useRegexFlag", "0");

        if (StringUtils.equals("0",useRegexFlag)) {
            List<String> lists = jsonObject.getJSONArray("allowedChars").toJavaList(String.class);
            String regex = validateInputWithRegex(lists);

            jsonObject.put("regex", regex);
            jsonObject.put("allowedCalue", regex);

            return jsonObject.toJSONString();
        }
        return jsonObject.toJSONString();
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DppQualityTaskDO.class)
                .likeRight(DppQualityTaskDO::getCatCode, catCode));
    }

}
