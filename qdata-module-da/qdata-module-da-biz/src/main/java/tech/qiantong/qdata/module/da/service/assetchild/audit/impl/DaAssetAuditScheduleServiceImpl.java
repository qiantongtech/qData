package tech.qiantong.qdata.module.da.service.assetchild.audit.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit.DaAssetAuditScheduleMapper;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditScheduleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 资产稽查调度Service业务层处理
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetAuditScheduleServiceImpl  extends ServiceImpl<DaAssetAuditScheduleMapper,DaAssetAuditScheduleDO> implements IDaAssetAuditScheduleService {
    @Resource
    private DaAssetAuditScheduleMapper daAssetAuditScheduleMapper;

    @Override
    public PageResult<DaAssetAuditScheduleDO> getDaAssetAuditSchedulePage(DaAssetAuditSchedulePageReqVO pageReqVO) {
        return daAssetAuditScheduleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO createReqVO) {
        DaAssetAuditScheduleDO dictType = BeanUtils.toBean(createReqVO, DaAssetAuditScheduleDO.class);
        daAssetAuditScheduleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO updateReqVO) {
        // 相关校验

        // 更新资产稽查调度
        DaAssetAuditScheduleDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetAuditScheduleDO.class);
        return daAssetAuditScheduleMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetAuditSchedule(Collection<Long> idList) {
        // 批量删除资产稽查调度
        return daAssetAuditScheduleMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetAuditScheduleDO getDaAssetAuditScheduleById(Long id) {
        return daAssetAuditScheduleMapper.selectById(id);
    }

    @Override
    public List<DaAssetAuditScheduleDO> getDaAssetAuditScheduleList() {
        return daAssetAuditScheduleMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetAuditScheduleDO> getDaAssetAuditScheduleMap() {
        List<DaAssetAuditScheduleDO> daAssetAuditScheduleList = daAssetAuditScheduleMapper.selectList();
        return daAssetAuditScheduleList.stream()
                .collect(Collectors.toMap(
                        DaAssetAuditScheduleDO::getId,
                        daAssetAuditScheduleDO -> daAssetAuditScheduleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入资产稽查调度数据
     *
     * @param importExcelList 资产稽查调度数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetAuditSchedule(List<DaAssetAuditScheduleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetAuditScheduleRespVO respVO : importExcelList) {
            try {
                DaAssetAuditScheduleDO daAssetAuditScheduleDO = BeanUtils.toBean(respVO, DaAssetAuditScheduleDO.class);
                Long daAssetAuditScheduleId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetAuditScheduleId != null) {
                        DaAssetAuditScheduleDO existingDaAssetAuditSchedule = daAssetAuditScheduleMapper.selectById(daAssetAuditScheduleId);
                        if (existingDaAssetAuditSchedule != null) {
                            daAssetAuditScheduleMapper.updateById(daAssetAuditScheduleDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetAuditScheduleId + " 的资产稽查调度记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetAuditScheduleId + " 的资产稽查调度记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetAuditScheduleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetAuditScheduleId);
                    DaAssetAuditScheduleDO existingDaAssetAuditSchedule = daAssetAuditScheduleMapper.selectOne(queryWrapper);
                    if (existingDaAssetAuditSchedule == null) {
                        daAssetAuditScheduleMapper.insert(daAssetAuditScheduleDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetAuditScheduleId + " 的资产稽查调度记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetAuditScheduleId + " 的资产稽查调度记录已存在。");
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
