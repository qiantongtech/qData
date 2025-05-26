package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据集成任务-日志Service业务层处理
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskLogServiceImpl  extends ServiceImpl<DppEtlTaskLogMapper,DppEtlTaskLogDO> implements IDppEtlTaskLogService {
    @Resource
    private DppEtlTaskLogMapper dppEtlTaskLogMapper;

    @Override
    public PageResult<DppEtlTaskLogDO> getDppEtlTaskLogPage(DppEtlTaskLogPageReqVO pageReqVO) {
        return dppEtlTaskLogMapper.selectPage(pageReqVO);
    }

    @Override
    public DppEtlTaskLogRespVO getDppEtlTaskLogById(DppEtlTaskLogPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskLogDO.class)
                .eq(StringUtils.isNotEmpty(reqVO.getCode()) , DppEtlTaskLogDO::getCode, reqVO.getCode())
                .eq(reqVO.getVersion() != null, DppEtlTaskLogDO::getVersion, reqVO.getVersion());
        DppEtlTaskLogDO dppEtlNodeLogDO = dppEtlTaskLogMapper.selectOne(wrapper);
        return BeanUtils.toBean(dppEtlNodeLogDO, DppEtlTaskLogRespVO.class);
    }

    @Override
    public Long createDppEtlTaskLog(DppEtlTaskLogSaveReqVO createReqVO) {
        DppEtlTaskLogDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskLogDO.class);
        dppEtlTaskLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlTaskLog(DppEtlTaskLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据集成任务-日志
        DppEtlTaskLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskLogDO.class);
        return dppEtlTaskLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlTaskLog(Collection<Long> idList) {
        // 批量删除数据集成任务-日志
        return dppEtlTaskLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskLogDO getDppEtlTaskLogById(Long id) {
        return dppEtlTaskLogMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskLogDO> getDppEtlTaskLogList() {
        return dppEtlTaskLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskLogDO> getDppEtlTaskLogMap() {
        List<DppEtlTaskLogDO> dppEtlTaskLogList = dppEtlTaskLogMapper.selectList();
        return dppEtlTaskLogList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskLogDO::getId,
                        dppEtlTaskLogDO -> dppEtlTaskLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据集成任务-日志数据
         *
         * @param importExcelList 数据集成任务-日志数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDppEtlTaskLog(List<DppEtlTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlTaskLogRespVO respVO : importExcelList) {
                try {
                    DppEtlTaskLogDO dppEtlTaskLogDO = BeanUtils.toBean(respVO, DppEtlTaskLogDO.class);
                    Long dppEtlTaskLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlTaskLogId != null) {
                            DppEtlTaskLogDO existingDppEtlTaskLog = dppEtlTaskLogMapper.selectById(dppEtlTaskLogId);
                            if (existingDppEtlTaskLog != null) {
                                dppEtlTaskLogMapper.updateById(dppEtlTaskLogDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dppEtlTaskLogId + " 的数据集成任务-日志记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dppEtlTaskLogId + " 的数据集成任务-日志记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DppEtlTaskLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlTaskLogId);
                        DppEtlTaskLogDO existingDppEtlTaskLog = dppEtlTaskLogMapper.selectOne(queryWrapper);
                        if (existingDppEtlTaskLog == null) {
                            dppEtlTaskLogMapper.insert(dppEtlTaskLogDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dppEtlTaskLogId + " 的数据集成任务-日志记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dppEtlTaskLogId + " 的数据集成任务-日志记录已存在。");
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
    public Integer queryMaxVersionByCode(String taskCode) {
        return baseMapper.queryMaxVersionByCode(taskCode);
    }
}
