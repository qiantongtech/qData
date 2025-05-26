package tech.qiantong.qdata.module.ds.service.apiLog.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.dal.mapper.apiLog.DsApiLogMapper;
import tech.qiantong.qdata.module.ds.service.apiLog.IDsApiLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * API服务调用日志Service业务层处理
 *
 * @author lhs
 * @date 2025-02-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DsApiLogServiceImpl  extends ServiceImpl<DsApiLogMapper,DsApiLogDO> implements IDsApiLogService {
    @Resource
    private DsApiLogMapper dsApiLogMapper;

    @Override
    public PageResult<DsApiLogDO> getDsApiLogPage(DsApiLogPageReqVO pageReqVO) {
        return dsApiLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDsApiLog(DsApiLogSaveReqVO createReqVO) {
        DsApiLogDO dictType = BeanUtils.toBean(createReqVO, DsApiLogDO.class);
        dsApiLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDsApiLog(DsApiLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新API服务调用日志
        DsApiLogDO updateObj = BeanUtils.toBean(updateReqVO, DsApiLogDO.class);
        return dsApiLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDsApiLog(Collection<Long> idList) {
        // 批量删除API服务调用日志
        return dsApiLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DsApiLogDO getDsApiLogById(Long id) {
        return dsApiLogMapper.selectDsApiLogByID(id);
    }

    @Override
    public List<DsApiLogDO> getDsApiLogList() {
        return dsApiLogMapper.selectList();
    }

    @Override
    public Map<Long, DsApiLogDO> getDsApiLogMap() {
        List<DsApiLogDO> dsApiLogList = dsApiLogMapper.selectList();
        return dsApiLogList.stream()
                .collect(Collectors.toMap(
                        DsApiLogDO::getId,
                        dsApiLogDO -> dsApiLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入API服务调用日志数据
         *
         * @param importExcelList API服务调用日志数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDsApiLog(List<DsApiLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DsApiLogRespVO respVO : importExcelList) {
                try {
                    DsApiLogDO dsApiLogDO = BeanUtils.toBean(respVO, DsApiLogDO.class);
                    Long dsApiLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dsApiLogId != null) {
                            DsApiLogDO existingDsApiLog = dsApiLogMapper.selectById(dsApiLogId);
                            if (existingDsApiLog != null) {
                                dsApiLogMapper.updateById(dsApiLogDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dsApiLogId + " 的API服务调用日志记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dsApiLogId + " 的API服务调用日志记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DsApiLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dsApiLogId);
                        DsApiLogDO existingDsApiLog = dsApiLogMapper.selectOne(queryWrapper);
                        if (existingDsApiLog == null) {
                            dsApiLogMapper.insert(dsApiLogDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dsApiLogId + " 的API服务调用日志记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dsApiLogId + " 的API服务调用日志记录已存在。");
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
