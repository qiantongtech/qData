package tech.qiantong.qdata.module.dpp.service.etl.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppQualityLogMapper;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEvaluateLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;
import tech.qiantong.qdata.module.system.api.message.dto.MessageSaveReqDTO;
import tech.qiantong.qdata.module.system.service.ISysMessageService;

import static tech.qiantong.qdata.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

/**
 * 数据质量日志Service业务层处理
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DppQualityLogServiceImpl  extends ServiceImpl<DppQualityLogMapper,DppQualityLogDO> implements IDppQualityLogService {
    private final DppQualityLogMapper dppQualityLogMapper;
    private final DppQualityTaskMapper qualityTaskMapper;
    private final IDppEvaluateLogService dppEvaluateLogService;
    private final ISysMessageService messageService;

    @Override
    public PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO) {
        PageResult<DppQualityLogDO> dppQualityLogDOPageResult = dppQualityLogMapper.selectPage(pageReqVO);
        List<DppQualityLogDO> rows = (List<DppQualityLogDO>)dppQualityLogDOPageResult.getRows();
        List<DppQualityLogDO> dppQualityLogDOS = new ArrayList<>();
        for (DppQualityLogDO row : rows) {
            Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(row.getId()));

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
            row.setScore(score.longValue());
            row.setProblemData(problemTotal);
            dppQualityLogDOS.add(row);
        }
        dppQualityLogDOPageResult.setRows(dppQualityLogDOS);
        return dppQualityLogDOPageResult;
    }

    @Override
    public Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO) {
        DppQualityLogDO dictType = BeanUtils.toBean(createReqVO, DppQualityLogDO.class);
        dppQualityLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据质量日志
        DppQualityLogDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityLogDO.class);
        return dppQualityLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityLog(Collection<Long> idList) {
        // 批量删除数据质量日志
        return dppQualityLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(Long id) {
        return dppQualityLogMapper.selectById(id);
    }

    @Override
    public DppQualityLogDO selectPrevLogByIdWithWrapper(Long id) {
        return dppQualityLogMapper.selectPrevLogByIdWithWrapper(String.valueOf(id));
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(DppQualityTaskAssetReqVO reqVO) {
        LambdaQueryWrapper<DppQualityLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DppQualityLogDO::getQualityId, reqVO.getId())
                .orderByDesc(DppQualityLogDO::getStartTime);

        Page<DppQualityLogDO> page = new Page<>(1, 1);
        IPage<DppQualityLogDO> resultPage = dppQualityLogMapper.selectPage(page, wrapper);

        return resultPage.getRecords().isEmpty() ? null : resultPage.getRecords().get(0);
    }

    @Override
    public List<DppQualityLogDO> getDppQualityLogList() {
        return dppQualityLogMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityLogDO> getDppQualityLogMap() {
        List<DppQualityLogDO> dppQualityLogList = dppQualityLogMapper.selectList();
        return dppQualityLogList.stream()
                .collect(Collectors.toMap(
                        DppQualityLogDO::getId,
                        dppQualityLogDO -> dppQualityLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据质量日志数据
     *
     * @param importExcelList 数据质量日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppQualityLogRespVO respVO : importExcelList) {
            try {
                DppQualityLogDO dppQualityLogDO = BeanUtils.toBean(respVO, DppQualityLogDO.class);
                Long dppQualityLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppQualityLogId != null) {
                        DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectById(dppQualityLogId);
                        if (existingDppQualityLog != null) {
                            dppQualityLogMapper.updateById(dppQualityLogDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dppQualityLogId + " 的数据质量日志记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dppQualityLogId + " 的数据质量日志记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DppQualityLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppQualityLogId);
                    DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectOne(queryWrapper);
                    if (existingDppQualityLog == null) {
                        dppQualityLogMapper.insert(dppQualityLogDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dppQualityLogId + " 的数据质量日志记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dppQualityLogId + " 的数据质量日志记录已存在。");
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
    public void sendMessage(Long id) {
        DppQualityLogDO dppQualityLogDO = dppQualityLogMapper.selectById(id);
        DppQualityTaskDO dppQualityTaskDO = qualityTaskMapper.selectById(dppQualityLogDO.getQualityId());
        Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(dppQualityLogDO.getId()));
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
        MessageSaveReqDTO messageSaveReqDTO = new MessageSaveReqDTO();
        messageSaveReqDTO.setReceiverId(Long.valueOf(dppQualityTaskDO.getContactId()));
        HashMap<String, Object> messageMeta = new HashMap<>();
        messageMeta.put("taskName", dppQualityTaskDO.getTaskName());
        messageMeta.put("executionTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS,dppQualityLogDO.getEndTime()));
        messageMeta.put("qualityScore", score);
        messageMeta.put("totalNumber", total);
        messageMeta.put("errorNumber", problemTotal);
        messageService.send(7L, messageSaveReqDTO, messageMeta);
    }
}
