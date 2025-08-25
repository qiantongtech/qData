package tech.qiantong.qdata.quality.service.qa.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppEvaluateLogDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppEvaluateLogMapper;
import tech.qiantong.qdata.quality.service.qa.IDppEvaluateLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 评测规则结果Service业务层处理
 *
 * @author qdata
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEvaluateLogServiceImpl  extends ServiceImpl<DppEvaluateLogMapper, DppEvaluateLogDO> implements IDppEvaluateLogService {
    @Resource
    private DppEvaluateLogMapper dppEvaluateLogMapper;

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
}
