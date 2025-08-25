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
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityTaskMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskEvaluateService;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据质量任务Service业务层处理
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskServiceImpl  extends ServiceImpl<DppQualityTaskMapper, DppQualityTaskDO> implements IDppQualityTaskService {
    @Resource
    private DppQualityTaskMapper dppQualityTaskMapper;

    @Override
    public PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO) {
        return dppQualityTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO) {
        DppQualityTaskDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskDO.class);
        dppQualityTaskMapper.insert(dictType);


        return dictType.getId();
    }

    @Override
    public int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据质量任务
        DppQualityTaskDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskDO.class);
        return dppQualityTaskMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTask(Collection<Long> idList) {
        // 批量删除数据质量任务
        return dppQualityTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskRespVO getDppQualityTaskById(Long id) {
        DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);

        DppQualityTaskRespVO bean = BeanUtils.toBean(dppQualityTaskDO, DppQualityTaskRespVO.class);
        return bean;
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
}
