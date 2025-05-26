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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit.DaAssetAuditRuleMapper;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditRuleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据资产质量结果记录Service业务层处理
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetAuditRuleServiceImpl  extends ServiceImpl<DaAssetAuditRuleMapper,DaAssetAuditRuleDO> implements IDaAssetAuditRuleService {
    @Resource
    private DaAssetAuditRuleMapper daAssetAuditRuleMapper;

    @Override
    public PageResult<DaAssetAuditRuleDO> getDaAssetAuditRulePage(DaAssetAuditRulePageReqVO pageReqVO) {
        return daAssetAuditRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetAuditRule(DaAssetAuditRuleSaveReqVO createReqVO) {
        DaAssetAuditRuleDO dictType = BeanUtils.toBean(createReqVO, DaAssetAuditRuleDO.class);
        daAssetAuditRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetAuditRule(DaAssetAuditRuleSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产质量结果记录
        DaAssetAuditRuleDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetAuditRuleDO.class);
        return daAssetAuditRuleMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetAuditRule(Collection<Long> idList) {
        // 批量删除数据资产质量结果记录
        return daAssetAuditRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetAuditRuleDO getDaAssetAuditRuleById(Long id) {
        return daAssetAuditRuleMapper.selectById(id);
    }

    @Override
    public List<DaAssetAuditRuleDO> getDaAssetAuditRuleList() {
        return daAssetAuditRuleMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetAuditRuleDO> getDaAssetAuditRuleMap() {
        List<DaAssetAuditRuleDO> daAssetAuditRuleList = daAssetAuditRuleMapper.selectList();
        return daAssetAuditRuleList.stream()
                .collect(Collectors.toMap(
                        DaAssetAuditRuleDO::getId,
                        daAssetAuditRuleDO -> daAssetAuditRuleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产质量结果记录数据
     *
     * @param importExcelList 数据资产质量结果记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetAuditRule(List<DaAssetAuditRuleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetAuditRuleRespVO respVO : importExcelList) {
            try {
                DaAssetAuditRuleDO daAssetAuditRuleDO = BeanUtils.toBean(respVO, DaAssetAuditRuleDO.class);
                Long daAssetAuditRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetAuditRuleId != null) {
                        DaAssetAuditRuleDO existingDaAssetAuditRule = daAssetAuditRuleMapper.selectById(daAssetAuditRuleId);
                        if (existingDaAssetAuditRule != null) {
                            daAssetAuditRuleMapper.updateById(daAssetAuditRuleDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetAuditRuleId + " 的数据资产质量结果记录记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetAuditRuleId + " 的数据资产质量结果记录记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetAuditRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetAuditRuleId);
                    DaAssetAuditRuleDO existingDaAssetAuditRule = daAssetAuditRuleMapper.selectOne(queryWrapper);
                    if (existingDaAssetAuditRule == null) {
                        daAssetAuditRuleMapper.insert(daAssetAuditRuleDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetAuditRuleId + " 的数据资产质量结果记录记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetAuditRuleId + " 的数据资产质量结果记录记录已存在。");
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
