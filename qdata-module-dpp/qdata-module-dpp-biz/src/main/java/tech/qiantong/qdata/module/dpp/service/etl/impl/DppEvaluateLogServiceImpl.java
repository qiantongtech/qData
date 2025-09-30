package tech.qiantong.qdata.module.dpp.service.etl.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEvaluateLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEvaluateLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskEvaluateService;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 评测规则结果Service业务层处理
 *
 * @author qdata
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEvaluateLogServiceImpl  extends ServiceImpl<DppEvaluateLogMapper,DppEvaluateLogDO> implements IDppEvaluateLogService {

    @Value("${path.quality_url}")
    private String url;
    @Resource
    private DppEvaluateLogMapper dppEvaluateLogMapper;

    @Resource
    @Lazy
    private IDppQualityLogService dppQualityLogService;
    @Resource
    @Lazy
    private IDppQualityTaskEvaluateService dppQualityTaskEvaluateService;
    @Resource
    @Lazy
    private IDppQualityTaskObjService dppQualityTaskObjService;
    @Resource
    @Lazy
    private IDaDatasourceApiService daDatasourceApiService;

    @Override
    public PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO) {
        return dppEvaluateLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO) {
        DppEvaluateLogDO dictType = BeanUtils.toBean(createReqVO, DppEvaluateLogDO.class);
        dppEvaluateLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新评测规则结果
        DppEvaluateLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEvaluateLogDO.class);
        return dppEvaluateLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEvaluateLog(Collection<Long> idList) {
        // 批量删除评测规则结果
        return dppEvaluateLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEvaluateLogDO getDppEvaluateLogById(Long id) {
        return dppEvaluateLogMapper.selectById(id);
    }

    @Override
    public List<DppEvaluateLogDO> getDppEvaluateLogList() {
        return dppEvaluateLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap() {
        List<DppEvaluateLogDO> dppEvaluateLogList = dppEvaluateLogMapper.selectList();
        return dppEvaluateLogList.stream()
                .collect(Collectors.toMap(
                        DppEvaluateLogDO::getId,
                        dppEvaluateLogDO -> dppEvaluateLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }

    @Override
    public Map<String, Object> sumTotalAndProblemTotalByTaskLogId(String taskLogId) {
        List<DppEvaluateLogDO> list = dppEvaluateLogMapper.selectList(new LambdaQueryWrapperX<DppEvaluateLogDO>()
                .eq(DppEvaluateLogDO::getTaskLogId, taskLogId)
                .eq(DppEvaluateLogDO::getValidFlag, "1")); // 如有需要加条件

        Long total = list.stream()
                .mapToLong(log -> log.getTotal() == null ? 0L : log.getTotal())
                .sum();

        Long problemTotal = list.stream()
                .mapToLong(log -> log.getProblemTotal() == null ? 0L : log.getProblemTotal())
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("total", total);
        summary.put("problemTotal", problemTotal);
        return summary;
    }


    /**
     * 导入评测规则结果数据
     *
     * @param importExcelList 评测规则结果数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEvaluateLogRespVO respVO : importExcelList) {
            try {
                DppEvaluateLogDO dppEvaluateLogDO = BeanUtils.toBean(respVO, DppEvaluateLogDO.class);
                Long dppEvaluateLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEvaluateLogId != null) {
                        DppEvaluateLogDO existingDppEvaluateLog = dppEvaluateLogMapper.selectById(dppEvaluateLogId);
                        if (existingDppEvaluateLog != null) {
                            dppEvaluateLogMapper.updateById(dppEvaluateLogDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dppEvaluateLogId + " 的评测规则结果记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppEvaluateLogId + " 的评测规则结果记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppEvaluateLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEvaluateLogId);
                    DppEvaluateLogDO existingDppEvaluateLog = dppEvaluateLogMapper.selectOne(queryWrapper);
                    if (existingDppEvaluateLog == null) {
                        dppEvaluateLogMapper.insert(dppEvaluateLogDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppEvaluateLogId + " 的评测规则结果记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppEvaluateLogId + " 的评测规则结果记录已存在。");
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
    public List<DppEvaluateLogStatisticsVO> statisticsEvaluateOne(Long id) {
        List<DppEvaluateLogStatisticsVO> dppEvaluateDimStatVOS = dppEvaluateLogMapper.selectDimStatsByTaskLogId(id);
        if(dppEvaluateDimStatVOS.isEmpty()){
            return new ArrayList<>();
        }
        DppQualityLogDO dppQualityLogDO = dppQualityLogService.selectPrevLogByIdWithWrapper(id);
        if(dppQualityLogDO == null){
            for (DppEvaluateLogStatisticsVO vo : dppEvaluateDimStatVOS) {
                vo.setTrendType(3L);
            }
            return dppEvaluateDimStatVOS;
        }
        List<DppEvaluateLogStatisticsVO> prevList = dppEvaluateLogMapper.selectDimStatsByTaskLogId(dppQualityLogDO.getId());
        if(prevList == null || prevList.isEmpty()){
            for (DppEvaluateLogStatisticsVO vo : dppEvaluateDimStatVOS) {
                vo.setTrendType(3L);
            }
            return dppEvaluateDimStatVOS;
        }

        // 4) 以维度为 Key 的上次“问题占比”基线
        Map<String, BigDecimal> prevProportionMap = new HashMap<>(prevList.size() * 2);
        for (DppEvaluateLogStatisticsVO vo : prevList) {
            BigDecimal val = vo.getProportion() == null ? BigDecimal.ZERO : vo.getProportion();
            prevProportionMap.put(String.valueOf(vo.getDimensionType()), val);
        }

        for (DppEvaluateLogStatisticsVO vo : dppEvaluateDimStatVOS) {
            String dim = String.valueOf(vo.getDimensionType());
            BigDecimal curProportion = vo.getProportion() == null ? BigDecimal.ZERO : vo.getProportion();
            BigDecimal prevProportion = prevProportionMap.get(dim);

            if (prevProportion == null) {
                vo.setTrendType(3L);
                continue;
            }

            int cmp = curProportion.compareTo(prevProportion);
            if (cmp > 0) {
                vo.setTrendType(1L);
            } else if (cmp < 0) {
                vo.setTrendType(2L);
            } else {
                vo.setTrendType(3L);
            }
        }
        return dppEvaluateDimStatVOS;
    }

//    @Override
//    public List<DppEvaluateLogStatisticsVO> statisticsEvaluateOne(Long id) {
//            List<DppEvaluateLogStatisticsVO> voList = new ArrayList<>();
//        DppQualityLogDO dppQualityLogById = dppQualityLogService.getDppQualityLogById(id);
//        DppQualityLogPageReqVO dppQualityLogPageReqVO = new DppQualityLogPageReqVO();
//        dppQualityLogPageReqVO.setQualityId(dppQualityLogById.getQualityId());
//        PageResult<DppQualityLogDO> dppQualityLogPage = dppQualityLogService.getDppQualityLogPage(dppQualityLogPageReqVO);
//        List<DppQualityLogDO> rows =  (List<DppQualityLogDO>) dppQualityLogPage.getRows();
//        DppQualityLogPageReqVO old = new DppQualityLogPageReqVO();
//        for (DppQualityLogDO row : rows) {
//            if (row.getCreateTime().getTime() <= dppQualityLogById.getCreateTime().getTime()) {
//                old = BeanUtils.toBean(row , DppQualityLogPageReqVO.class);
//                break;
//            }
//        }
//        // 最新的
//        Long problemTotalAll = 0L;
//        List<DppEvaluateLogDO> taskLogId = dppEvaluateLogMapper.selectList("task_log_id", id);
//        Map<String, List<DppEvaluateLogDO>> collect = taskLogId.stream().collect(Collectors.groupingBy(s -> s.getDimensionType()));
//        for (DppEvaluateLogDO aDo : taskLogId) {
//            problemTotalAll += aDo.getProblemTotal();
//        }
//
//        // 老的
//        List<DppEvaluateLogDO> oldTaskLogId = dppEvaluateLogMapper.selectList("task_log_id", old.getId());
//        Map<String, List<DppEvaluateLogDO>> oldCollect = oldTaskLogId.stream().collect(Collectors.groupingBy(s -> s.getDimensionType()));
//        for (DppEvaluateLogDO aDo : oldTaskLogId) {
//
//        }
//        for (Map.Entry<String, List<DppEvaluateLogDO>> entry : collect.entrySet()) {
//            DppEvaluateLogStatisticsVO vo = new DppEvaluateLogStatisticsVO();
//            List<DppEvaluateLogDO> value = entry.getValue();
//            Long problemTotal = 0L;
//            for (DppEvaluateLogDO dppEvaluateLogDO : value) {
//                problemTotal += dppEvaluateLogDO.getProblemTotal();
//            }
//            vo.setDimensionType(entry.getKey());
//            vo.setProblemTotal(problemTotal);
//            vo.setSuccesTotal(Convert.toLong(value.size()));
//            vo.setProportion(BigDecimal.ZERO);
//            if (!problemTotal.equals(0L) && !problemTotalAll.equals(0L)) {
//                BigDecimal bigDecimal = new BigDecimal(problemTotal).divide(new BigDecimal(problemTotalAll) , 5, RoundingMode.HALF_UP);
//                BigDecimal subtract = bigDecimal.multiply(new BigDecimal(100));
//                vo.setProportion(subtract.setScale(2 , RoundingMode.HALF_UP));
//            }
//            List<DppEvaluateLogDO> dppEvaluateLogDOS = oldCollect.get(entry.getKey());
//            Long oldProblemTotal = 0L;
//            if (dppEvaluateLogDOS != null) {
//                for (DppEvaluateLogDO dppEvaluateLogDO : dppEvaluateLogDOS) {
//                    oldProblemTotal += dppEvaluateLogDO.getProblemTotal();
//                }
//                // 趋势 0：下降，1：上升
//                if (problemTotal > oldProblemTotal) {
//                    vo.setTrendType(0L);
//                } else {
//                    vo.setTrendType(1L);
//                }
//            }
//
//            voList.add(vo);
//        }
//
//        return voList;
//    }

    @Override
    public JSONObject statisticsEvaluateTow(Long id,  Date deDate , Date oldDate , int type) {
        DppQualityLogDO dppQualityLogById = dppQualityLogService.getDppQualityLogById(id);
        LambdaQueryWrapperX<DppQualityLogDO> objectLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        objectLambdaQueryWrapperX.betweenIfPresent(DppQualityLogDO::getCreateTime , oldDate , deDate);
        objectLambdaQueryWrapperX.eqIfPresent(DppQualityLogDO::getQualityId , dppQualityLogById.getQualityId());
        List<DppQualityLogDO> list = dppQualityLogService.list(objectLambdaQueryWrapperX);

        LocalDate today = LocalDate.now();
        List<String> lastSevenDays = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        switch (type) {
            case 0:
                // 循环生成最近 7 天的日期
                for (int i = 0; i < 7; i++) {
                    LocalDate date = today.minusDays(i);
                    // 格式化日期并添加到列表中
                    lastSevenDays.add(date.format(formatter));
                }
                break;
            case 1:
                // 循环生成最近 15 天的日期
                for (int i = 0; i < 15; i++) {
                    LocalDate date = today.minusDays(i);
                    // 格式化日期并添加到列表中
                    lastSevenDays.add(date.format(formatter));
                }
                break;
            case 2:
                // 循环生成最近 30 天的日期
                for (int i = 0; i < 30; i++) {
                    LocalDate date = today.minusDays(i);
                    // 格式化日期并添加到列表中
                    lastSevenDays.add(date.format(formatter));
                }
                break;
        }
        Map<String , Integer> map = new HashMap<>();
        for (DppQualityLogDO aDo : list) {
            String s = DateUtils.parseDateToStr("yyyy-MM-dd", aDo.getCreateTime());
            for (String lastSevenDay : lastSevenDays) {
                if (s.equals(lastSevenDay)) {
                    Integer i = map.get(lastSevenDay);
                    if (i == null) {
                        i = 0;
                    }
                    if (aDo.getScore() != null) {
                        i += Convert.toInt(aDo.getScore());
                    }
                    map.put("lastSevenDay" , i);
                }
            }
        }
        List<Integer> value = new ArrayList<>();
        for (String string : lastSevenDays) {
            Integer i = map.get(string);
            if (i == null) {
                i = 0;
            }
            value.add(i);
        }

        JSONObject json = new JSONObject();
        json.put("title" , lastSevenDays);
        json.put("value" , value);
        return json;
    }

    public List<DppQualityTaskObjRespVO> buildTaskObjRespList(DppQualityLogDO dppQualityLogById) {
        if (dppQualityLogById == null || dppQualityLogById.getQualityId() == null) {
            return Collections.emptyList();
        }

        DppQualityTaskObjPageReqVO reqVO = new DppQualityTaskObjPageReqVO();
        reqVO.setTaskId(JSONUtils.convertToLong(dppQualityLogById.getQualityId()));

        List<DppQualityTaskObjDO> lists = dppQualityTaskObjService.getDppQualityTaskObjList(reqVO);
        List<DppQualityTaskObjRespVO> result = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(lists)) {
            for (DppQualityTaskObjDO objDO : lists) {
                DppQualityTaskObjRespVO bean = BeanUtils.toBean(objDO, DppQualityTaskObjRespVO.class);

                DaDatasourceRespDTO ds = daDatasourceApiService.getDatasourceById(objDO.getDatasourceId());
                if (ds != null) {
                    bean.setDatasourceType(ds.getDatasourceType());
                    bean.setDatasourceName(ds.getDatasourceName());
                }
                result.add(bean);
            }
        }
        return result;
    }

    @Override
    public List<DppEvaluateLogRespVO> statisticsEvaluateTable(Long id) {
        DppQualityLogDO dppQualityLogById = dppQualityLogService.getDppQualityLogById(id);
        if(dppQualityLogById == null){
            return new ArrayList<>();
        }
        List<DppEvaluateLogDO> taskLogId = dppEvaluateLogMapper.selectList("task_log_id", id);

        List<DppQualityTaskObjRespVO> newList = this.buildTaskObjRespList(dppQualityLogById);

        List<DppEvaluateLogRespVO> list = new ArrayList<>();
        Map<Long, DppQualityTaskObjRespVO> collect = newList.stream().collect(Collectors.toMap(s -> s.getId(), Function.identity()));
        for (DppEvaluateLogDO dppEvaluateLogDO : taskLogId) {
            DppQualityTaskEvaluateDO dppQualityTaskEvaluateById = dppQualityTaskEvaluateService.getDppQualityTaskEvaluateById(Convert.toLong(dppEvaluateLogDO.getEvaluateId()));
            DppQualityTaskObjRespVO dppQualityTaskObjRespVO = collect.get(dppQualityTaskEvaluateById.getObjId());

            DppEvaluateLogRespVO bean = BeanUtils.toBean(dppEvaluateLogDO, DppEvaluateLogRespVO.class);
            Long total = bean.getTotal();
            Long problemTotal = bean.getProblemTotal();
            bean.setProportion(BigDecimal.ZERO);
            if (!total.equals(0L)) {
                BigDecimal bigDecimal = new BigDecimal(problemTotal).divide(new BigDecimal(total) , 5, RoundingMode.HALF_UP);
                BigDecimal subtract = bigDecimal.multiply(new BigDecimal(100));
                bean.setProportion(subtract.setScale(2 , RoundingMode.HALF_UP));
            }

//            JSONObject jsonObject = JSONObject.parseObject(bean.getRule());
//            List<JSONObject> list1 = jsonObject.getList("evaColumns", JSONObject.class);
//            if (list1 != null) {
//                for (JSONObject object : list1) {
//                    String string = object.getString("name");
//                    if (bean.getColumnName().equals(string)) {
//                        bean.setColumnName(object.getString("label"));
//                    }
//                }
//            }
            if (dppQualityTaskObjRespVO != null) {
                bean.setDatasourceName(dppQualityTaskObjRespVO.getDatasourceName());
                bean.setDatasourceType(dppQualityTaskObjRespVO.getDatasourceType());
                bean.setDatasourceId(dppQualityTaskObjRespVO.getDatasourceId());
            }
            list.add(bean);
        }
        return list;
    }

    @Override
    public JSONObject pageErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO) {
        List<HeaderEntity> headers = new ArrayList<>();
        HeaderEntity headerEntity = new HeaderEntity();
        headerEntity.setKey("Content-Type");
        headerEntity.setValue("application/json");
        headers.add(headerEntity);  // 设置请求头
        try {
            String fullUrl = url + "/pageErrorData";

            // 将对象转为 JSON Map 发送 POST 请求（RequestBody）
            Map<String, Object> paramMap = JSONObject.parseObject(JSONObject.toJSONString(checkErrorDataReqDTO), Map.class);

            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(fullUrl, paramMap, headers);

            System.out.println(responseObject.toString());
            JSONObject json = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            JSONObject data = json.getJSONObject("data");
            System.out.println(data.toString());
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO) {
        List<HeaderEntity> headers = new ArrayList<>();
        HeaderEntity header = new HeaderEntity();
        header.setKey("Content-Type");
        header.setValue("application/json");
        headers.add(header);

        try {
            String fullUrl = url + "/updateErrorData";
            Map<String, Object> paramMap = JSONObject.parseObject(JSONObject.toJSONString(checkErrorDataReqDTO), Map.class);

            // 发送 POST 请求（带 JSON 请求体）
            HttpUtils.ResponseObject responseObject = HttpUtils.sendPost(fullUrl, paramMap, headers);
            System.out.println("修改响应：" + responseObject);

            JSONObject result = JSONObject.parseObject(String.valueOf(responseObject.getBody()));
            return result.getBoolean("data"); // CommonResult.data 为 true/false
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
