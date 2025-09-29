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
 * 数据质量任务Service业务层处理
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
        // 相关校验
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
        // 批量删除数据质量任务
        for (Long id : idList) {
            // 查询 DaDiscoveryTaskDO 详情
            DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);
            if (dppQualityTaskDO != null &&
                    (dppQualityTaskDO.getSystemJobId() != null || !StringUtils.equals("0",dppQualityTaskDO.getTaskCode())) ) {
                // 提取 systemJobId
                if(StringUtils.equals("0",dppQualityTaskDO.getStatus())){
                    throw new ServiceException("上线任务，不允删除，请先下线！");
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
            // 设置评分与问题数
            dppQualityTaskRespVO.setScore(0L);
            dppQualityTaskRespVO.setProblemData(0L);
            dppQualityTaskRespVO.setLogId(null);
            dppQualityTaskRespVO.setLastExecuteTime(null);
            return dppQualityTaskRespVO;
        }

        Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(log.getId()));

        // 获取总数与问题数（确保 null 安全）
        Long total = map.get("total") == null ? 0L : (Long) map.get("total");
        Long problemTotal = map.get("problemTotal") == null ? 0L : (Long) map.get("problemTotal");

        // 计算质量评分（百分比，保留两位小数）
        BigDecimal score = BigDecimal.ZERO;
        if (total > 0) {
            score = BigDecimal.valueOf(total - problemTotal)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }

        // 设置评分与问题数
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

        // 数据对象列表
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

        // 规则列表
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
//        //0:否，1：是
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据质量任务数据
         *
         * @param importExcelList 数据质量任务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
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
                                successMessages.add("数据更新成功，ID为 " + dppQualityTaskId + " 的数据质量任务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dppQualityTaskId + " 的数据质量任务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DppQualityTaskDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskId);
                        DppQualityTaskDO existingDppQualityTask = dppQualityTaskMapper.selectOne(queryWrapper);
                        if (existingDppQualityTask == null) {
                            dppQualityTaskMapper.insert(dppQualityTaskDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dppQualityTaskId + " 的数据质量任务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dppQualityTaskId + " 的数据质量任务记录已存在。");
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
    public String verifyInterfaceValue(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate) {
        // 处理正则
        JSONObject jsonObject = JSONObject.parseObject(dppQualityTaskEvaluate.getRule());
        List<String> lists = jsonObject.getList("allowedChars", String.class);
        String s = this.validateInputWithRegex(lists);
        if (dppQualityTaskEvaluate.getTitle().matches(s)) {
            return dppQualityTaskEvaluate.getTitle() + "，数据监测成功";
        }
        return  dppQualityTaskEvaluate.getTitle() + "，不符合规则";
    }

    @Override
    public AjaxResult startDppQualityTask(Long id) {
        DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);
        if(dppQualityTaskDO == null){
            return error("任务不存在，请刷新后重试！");
        }
        if (!StringUtils.equals("0",dppQualityTaskDO.getStatus())){
            return error("任务状态错误，请刷新后重试！");
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
        headers.add(headerEntity);  // 设置请求头
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(url + "/generateValidationErrorDataSql", objectObjectHashMap, headers);
            System.out.println(responseObject.toString());
            // 强转并解析为 JSONObject
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            // 提取 data
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
        headers.add(headerEntity);  // 设置请求头
        try {
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(url + "/generateValidationValidDataSql", objectObjectHashMap, headers);
            System.out.println(responseObject.toString());
            // 强转并解析为 JSONObject
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            // 提取 data
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
                //     * 创建调度器 (只有任务发布了才能调用该接口)
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
                throw new ServiceException("调度周期修改失败，请联系系统管理员！");

            }
        }

        // 更新数据发现任务
        DppQualityTaskDO updateObj = BeanUtils.toBean(daDiscoveryTask, DppQualityTaskDO.class);
        dppQualityTaskMapper.updateById(updateObj);
//        this.updateDaDiscoveryTask(daDiscoveryTask);
        return true;
    }

    private void validateTaskStatus(DppQualityTaskRespVO daDiscoveryTaskById, String daDiscoveryTaskStatus) {
        if (daDiscoveryTaskById == null || daDiscoveryTaskStatus == null) {
            throw new ServiceException("任务模版错误，未查询到调度信息！");
        }
    }

    private void handleOfflineTask(DppQualityTaskRespVO daDiscoveryTaskById, Long systemJobId, DppQualityTaskSaveReqVO daDiscoveryTask) {
        if(daDiscoveryTaskById.getSystemJobId() != null &&  systemJobId > 0){
            DsStatusRespDTO respDTO = dsEtlTaskService.releaseTask("OFFLINE", String.valueOf(projectCode), daDiscoveryTaskById.getTaskCode());
            if (respDTO == null || !respDTO.getSuccess()) {
                throw new ServiceException("发布或下线任务，失败！");
            }

            DsStatusRespDTO offlined = iDsEtlSchedulerService.offlineScheduler(projectCode, systemJobId);
            if (!offlined.getData()) {
                throw new ServiceException("下线调度器，失败！");
            }
        }

        // 更新数据发现任务
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
            throw new ServiceException("发布或下线任务，失败！");
        }

        if (systemJobId != null && systemJobId > 0) {
            updateExistingScheduler(daDiscoveryTask, systemJobId);
        } else {
            createNewScheduler(daDiscoveryTask);
        }

        DsStatusRespDTO dsStatusRespDTO1 = iDsEtlSchedulerService.onlineScheduler(projectCode, daDiscoveryTask.getSystemJobId());
        if (!dsStatusRespDTO1.getData()) {
            throw new ServiceException("上线调度器，失败！");
        }

        // 更新数据发现任务
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
            //     * 创建调度器 (只有任务发布了才能调用该接口)
            DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO = DppTaskConverter.createSchedulerRequest(daDiscoveryTask.getCycle(),daDiscoveryTask.getTaskCode());
            DsSchedulerRespDTO saveScheduler = iDsEtlSchedulerService.saveScheduler(dsSchedulerSaveReqDTO, String.valueOf(projectCode));
            if(saveScheduler == null || !saveScheduler.getSuccess()){
                throw new ServiceException("创建调度器，失败！");
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
            throw new ServiceException("更新调度器，失败！");
        }
    }

    public ProcessDefinition updateProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DppTaskConverter.stringToLong(projectCode));

        input.setNodeCode(DppTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DppTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.updateTask(dsTaskSaveReqDTO,projectCode,input.getTaskCode() );

        if (!task.getSuccess()) {
            throw new ServiceException("任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();
        return data; // 返回创建结果
    }

    public ProcessDefinition createProcessDefinition(TaskSaveReqInput input) {
        Long nodeUniqueKey = this.getNodeUniqueKey(DppTaskConverter.stringToLong(projectCode));

        input.setNodeCode(DppTaskConverter.longToString(nodeUniqueKey));

        DsTaskSaveReqDTO dsTaskSaveReqDTO = DppTaskConverter.buildDsTaskSaveReq(input);
        DsTaskSaveRespDTO task = dsEtlTaskService.createTask(dsTaskSaveReqDTO,DppTaskConverter.stringToLong(projectCode) );

        if (!task.getSuccess()) {
            throw new ServiceException("任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
        }
        ProcessDefinition data = task.getData();
        return data; // 返回创建结果
    }

    public Long getNodeUniqueKey(Long projectCode) {
        try {
            DsNodeGenCodeRespDTO dsNodeGenCodeRespDTO = dsEtlNodeService.genCode(projectCode);
            return dsNodeGenCodeRespDTO.getData().get(0);
        } catch (Exception e){
            throw new ServiceException("任务状态修改失败，请联系系统管理员"); // 抛出任务定义创建错误的异常
        }
    }



    /**
     * 拼接正则表达式
     * @param value
     * @return
     */
    public static String validateInputWithRegex(List<String> value) {
        Map<String, String> map = new HashMap<>();
        // 数字
        map.put("1", "0-9");
        // 字母
        map.put("2", "a-zA-Z");
        // 空格
        map.put("3", "\\s");
        // 特殊符号
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

        // 1. 数据源 ID
        paramMap.put("dataId", dppQualityTaskEvaluate.getDatasourceId());

        // 2. 表名
        paramMap.put("tableName", dppQualityTaskEvaluate.getTableName());

        // 3. 规则类型
        paramMap.put("ruleType", dppQualityTaskEvaluate.getRuleType());

        // 4. 分页信息（临时写死 ruleType，如后续有分页参数可调整）
        paramMap.put("pageNum", dppQualityTaskEvaluate.getPageNum());
        paramMap.put("pageSize", dppQualityTaskEvaluate.getPageSize());


        String stringObjectMap = buildCharacterValidationRule(dppQualityTaskEvaluate.getRule(), dppQualityTaskEvaluate.getRuleType());

        // 5. 规则配置
        paramMap.put("config",  JSONUtils.convertTaskDefinitionJsonMap(stringObjectMap));

        // 6. 评估字段
        paramMap.put("evaColumn", dppQualityTaskEvaluate.getEvaColumn());

        // 7. where 条件
        paramMap.put("whereClause", dppQualityTaskEvaluate.getWhereClause());

        return paramMap;
    }
    /**
     * 处理 CHARACTER_VALIDATION 规则
     * 兼容 SaveReqVO 与 DO 两种类型
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
     * 公共内部逻辑
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
