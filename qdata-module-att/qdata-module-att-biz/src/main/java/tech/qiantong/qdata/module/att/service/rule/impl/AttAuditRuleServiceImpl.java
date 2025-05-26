package tech.qiantong.qdata.module.att.service.rule.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.enums.RuleTypeEnum;
import tech.qiantong.qdata.module.att.dal.mapper.rule.AttAuditRuleMapper;
import tech.qiantong.qdata.module.att.service.rule.IAttAuditRuleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 稽查规则Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttAuditRuleServiceImpl extends ServiceImpl<AttAuditRuleMapper, AttAuditRuleDO>
        implements IAttAuditRuleService {
    @Resource
    private AttAuditRuleMapper attAuditRuleMapper;

    @Override
    public PageResult<AttAuditRuleDO> getAttAuditRulePage(AttAuditRulePageReqVO pageReqVO) {
        return attAuditRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttAuditRule(AttAuditRuleSaveReqVO createReqVO) {
        AttAuditRuleDO dictType = BeanUtils.toBean(createReqVO, AttAuditRuleDO.class);
        attAuditRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttAuditRule(AttAuditRuleSaveReqVO updateReqVO) {
        // 相关校验

        // 更新稽查规则
        AttAuditRuleDO updateObj = BeanUtils.toBean(updateReqVO, AttAuditRuleDO.class);
        return attAuditRuleMapper.updateById(updateObj);
    }

    @Override
    public int removeAttAuditRule(Collection<Long> idList) {
        // 批量删除稽查规则
        return attAuditRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public AttAuditRuleDO getAttAuditRuleById(Long id) {
        return attAuditRuleMapper.selectById(id);
    }

    @Override
    public List<AttAuditRuleDO> getAttAuditRuleList() {
        return attAuditRuleMapper.selectList();
    }

    @Override
    public Map<Long, AttAuditRuleDO> getAttAuditRuleMap() {
        List<AttAuditRuleDO> attAuditRuleList = attAuditRuleMapper.selectList();
        return attAuditRuleList.stream()
                .collect(Collectors.toMap(
                        AttAuditRuleDO::getId,
                        attAuditRuleDO -> attAuditRuleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing));
    }

    @Override
    public List<AttAuditRuleRespVO> getAttAuditRuleTree(Long dataElemId) {
        // 1. 获取所有稽查规则列表
        List<AttAuditRuleDO> list = attAuditRuleMapper.selectAttAuditRuleList(dataElemId);
        // 2. 转换为VO对象
        List<AttAuditRuleRespVO> voList = BeanUtils.toBean(list, AttAuditRuleRespVO.class);
        // 3. 构建树形结构
        return buildTreeByType(voList);
    }

    /**
     * 构建树形结构 - 以type字段作为父节点
     *
     * @param list 规则列表
     * @return 树形结构列表
     */
    private List<AttAuditRuleRespVO> buildTreeByType(List<AttAuditRuleRespVO> list) {
        List<AttAuditRuleRespVO> resultList = new ArrayList<>();
        // 创建type映射，用于存储相同type的节点
        Map<String, List<AttAuditRuleRespVO>> typeMap = list.stream()
                .collect(Collectors.groupingBy(AttAuditRuleRespVO::getType));

        // 遍历每个type分组
        for (Map.Entry<String, List<AttAuditRuleRespVO>> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            List<AttAuditRuleRespVO> typeNodes = entry.getValue();
            for (AttAuditRuleRespVO typeNode : typeNodes) {
                typeNode.setDataType("2");
            }
            // 创建父节点
            AttAuditRuleRespVO parentNode = new AttAuditRuleRespVO();
            parentNode.setId(0L); // 设置一个特殊的ID
            parentNode.setType(type);
            parentNode.setDataType("1");
            // 使用枚举获取类型名称
            String typeName = RuleTypeEnum.getNameByType(type);
            parentNode.setName(typeName); // 设置父节点名称
            parentNode.setChildren(new ArrayList<>(typeNodes));

            resultList.add(parentNode);
        }

        return resultList;
    }

    /**
     * 导入稽查规则数据
     *
     * @param importExcelList 稽查规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttAuditRule(List<AttAuditRuleRespVO> importExcelList, boolean isUpdateSupport,
                                     String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttAuditRuleRespVO respVO : importExcelList) {
            try {
                AttAuditRuleDO attAuditRuleDO = BeanUtils.toBean(respVO, AttAuditRuleDO.class);
                Long attAuditRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (attAuditRuleId != null) {
                        AttAuditRuleDO existingAttAuditRule = attAuditRuleMapper.selectById(attAuditRuleId);
                        if (existingAttAuditRule != null) {
                            attAuditRuleMapper.updateById(attAuditRuleDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attAuditRuleId + " 的稽查规则记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attAuditRuleId + " 的稽查规则记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttAuditRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attAuditRuleId);
                    AttAuditRuleDO existingAttAuditRule = attAuditRuleMapper.selectOne(queryWrapper);
                    if (existingAttAuditRule == null) {
                        attAuditRuleMapper.insert(attAuditRuleDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attAuditRuleId + " 的稽查规则记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attAuditRuleId + " 的稽查规则记录已存在。");
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
