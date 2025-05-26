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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit.DaAssetAuditAlertMapper;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditAlertService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据资产-质量预警Service业务层处理
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetAuditAlertServiceImpl  extends ServiceImpl<DaAssetAuditAlertMapper,DaAssetAuditAlertDO> implements IDaAssetAuditAlertService {
    @Resource
    private DaAssetAuditAlertMapper daAssetAuditAlertMapper;

    @Override
    public PageResult<DaAssetAuditAlertDO> getDaAssetAuditAlertPage(DaAssetAuditAlertPageReqVO pageReqVO) {
        return daAssetAuditAlertMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO createReqVO) {
        DaAssetAuditAlertDO dictType = BeanUtils.toBean(createReqVO, DaAssetAuditAlertDO.class);
        daAssetAuditAlertMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetAuditAlert(DaAssetAuditAlertSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-质量预警
        DaAssetAuditAlertDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetAuditAlertDO.class);
        return daAssetAuditAlertMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetAuditAlert(Collection<Long> idList) {
        // 批量删除数据资产-质量预警
        return daAssetAuditAlertMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetAuditAlertDO getDaAssetAuditAlertById(Long id) {
        return daAssetAuditAlertMapper.selectById(id);
    }

    @Override
    public List<DaAssetAuditAlertDO> getDaAssetAuditAlertList() {
        return daAssetAuditAlertMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetAuditAlertDO> getDaAssetAuditAlertMap() {
        List<DaAssetAuditAlertDO> daAssetAuditAlertList = daAssetAuditAlertMapper.selectList();
        return daAssetAuditAlertList.stream()
                .collect(Collectors.toMap(
                        DaAssetAuditAlertDO::getId,
                        daAssetAuditAlertDO -> daAssetAuditAlertDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产-质量预警数据
     *
     * @param importExcelList 数据资产-质量预警数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetAuditAlert(List<DaAssetAuditAlertRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetAuditAlertRespVO respVO : importExcelList) {
            try {
                DaAssetAuditAlertDO daAssetAuditAlertDO = BeanUtils.toBean(respVO, DaAssetAuditAlertDO.class);
                Long daAssetAuditAlertId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetAuditAlertId != null) {
                        DaAssetAuditAlertDO existingDaAssetAuditAlert = daAssetAuditAlertMapper.selectById(daAssetAuditAlertId);
                        if (existingDaAssetAuditAlert != null) {
                            daAssetAuditAlertMapper.updateById(daAssetAuditAlertDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetAuditAlertId + " 的数据资产-质量预警记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetAuditAlertId + " 的数据资产-质量预警记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetAuditAlertDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetAuditAlertId);
                    DaAssetAuditAlertDO existingDaAssetAuditAlert = daAssetAuditAlertMapper.selectOne(queryWrapper);
                    if (existingDaAssetAuditAlert == null) {
                        daAssetAuditAlertMapper.insert(daAssetAuditAlertDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetAuditAlertId + " 的数据资产-质量预警记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetAuditAlertId + " 的数据资产-质量预警记录已存在。");
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
