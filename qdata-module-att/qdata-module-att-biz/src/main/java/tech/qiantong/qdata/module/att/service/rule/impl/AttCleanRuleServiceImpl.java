package tech.qiantong.qdata.module.att.service.rule.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.enums.CleanRuleTypeEnum;
import tech.qiantong.qdata.module.att.dal.mapper.rule.AttCleanRuleMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttCleanCatService;
import tech.qiantong.qdata.module.att.service.rule.IAttCleanRuleService;

/**
 * 清洗规则Service业务层处理
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttCleanRuleServiceImpl extends ServiceImpl<AttCleanRuleMapper, AttCleanRuleDO>
        implements IAttCleanRuleService {
    @Resource
    private AttCleanRuleMapper attCleanRuleMapper;
    @Resource
    private IAttCleanCatService attCleanCatService;

    @Override
    public PageResult<AttCleanRuleDO> getAttCleanRulePage(AttCleanRulePageReqVO pageReqVO) {
        return attCleanRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttCleanRule(AttCleanRuleSaveReqVO createReqVO) {
        List<AttCleanRuleDO> code = attCleanRuleMapper.selectList("code", createReqVO.getCode());
        if (code.size() > 0) {
            throw new RuntimeException("规则编码重复请重新输入");
        }
        AttCleanRuleDO dictType = BeanUtils.toBean(createReqVO, AttCleanRuleDO.class);
        attCleanRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttCleanRule(AttCleanRuleSaveReqVO updateReqVO) {
        // 相关校验
        List<AttCleanRuleDO> code = attCleanRuleMapper.selectList("code", updateReqVO.getCode());
        if (code.size() > 0) {
            throw new RuntimeException("规则编码重复请重新输入");
        }
        // 更新清洗规则
        AttCleanRuleDO updateObj = BeanUtils.toBean(updateReqVO, AttCleanRuleDO.class);
        return attCleanRuleMapper.updateById(updateObj);
    }

    @Override
    public int removeAttCleanRule(Collection<Long> idList) {
        // 批量删除清洗规则
        return attCleanRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public AttCleanRuleDO getAttCleanRuleById(Long id) {
        return attCleanRuleMapper.selectById(id);
    }

    @Override
    public List<AttCleanRuleDO> getAttCleanRuleList() {
        return attCleanRuleMapper.selectList();
    }

    @Override
    public List<AttCleanRuleRespVO> getAttCleanRuleList(AttCleanRulePageReqVO attCleanRule) {

        MPJLambdaWrapper<AttCleanRuleDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttCleanRuleDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_CLEAN_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .likeRight(org.apache.commons.lang3.StringUtils.isNotBlank(attCleanRule.getCatCode()), AttCleanRuleDO::getCatCode, attCleanRule.getCatCode());
//        LambdaQueryWrapperX<AttCleanRuleDO> x = new LambdaQueryWrapperX<>();
//        x.eqIfPresent(AttCleanRuleDO::getType , attCleanRule.getType());
//        x.eqIfPresent(AttCleanRuleDO::getValidFlag , attCleanRule.getValidFlag());
        List<AttCleanRuleDO> attCleanRuleDOS = attCleanRuleMapper.selectList(lambdaWrapper);
        List<AttCleanRuleRespVO> bean = BeanUtils.toBean(attCleanRuleDOS, AttCleanRuleRespVO.class);
        for (AttCleanRuleRespVO respVO : bean) {

            respVO.setParentType(Convert.toStr(respVO.getCatID()));
            respVO.setParentName(respVO.getCatName());
        }
        return bean;
    }

    @Override
    public Map<Long, AttCleanRuleDO> getAttCleanRuleMap() {
        List<AttCleanRuleDO> attCleanRuleList = attCleanRuleMapper.selectList();
        return attCleanRuleList.stream()
                .collect(Collectors.toMap(
                        AttCleanRuleDO::getId,
                        attCleanRuleDO -> attCleanRuleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing));
    }

    /**
     * 导入清洗规则数据
     *
     * @param importExcelList 清洗规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAttCleanRule(List<AttCleanRuleRespVO> importExcelList, boolean isUpdateSupport,
                                     String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttCleanRuleRespVO respVO : importExcelList) {
            try {
                AttCleanRuleDO attCleanRuleDO = BeanUtils.toBean(respVO, AttCleanRuleDO.class);
                Long attCleanRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (attCleanRuleId != null) {
                        AttCleanRuleDO existingAttCleanRule = attCleanRuleMapper.selectById(attCleanRuleId);
                        if (existingAttCleanRule != null) {
                            attCleanRuleMapper.updateById(attCleanRuleDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + attCleanRuleId + " 的清洗规则记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + attCleanRuleId + " 的清洗规则记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<AttCleanRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attCleanRuleId);
                    AttCleanRuleDO existingAttCleanRule = attCleanRuleMapper.selectOne(queryWrapper);
                    if (existingAttCleanRule == null) {
                        attCleanRuleMapper.insert(attCleanRuleDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + attCleanRuleId + " 的清洗规则记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + attCleanRuleId + " 的清洗规则记录已存在。");
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
    public List<AttCleanRuleRespVO> getAttCleanRuleTree(Long dataElemId) {
        // 1. 获取所有清洗规则列表
        List<AttCleanRuleDO> list = attCleanRuleMapper.selectAttCleanRuleList(dataElemId);
        // 2. 转换为VO对象
        List<AttCleanRuleRespVO> voList = BeanUtils.toBean(list, AttCleanRuleRespVO.class);
        // 3. 构建树形结构
        return buildTreeByType(voList);
    }

    @Override
    public List<AttCleanRuleRespVO> getCleaningRuleTree(Long[] dataElemId) {
        List<AttCleanRuleDO> list =null;
        if (dataElemId == null || dataElemId.length == 0) {
            // 数组为空或未初始化
            list = attCleanRuleMapper.selectList();
        }else {
            list = attCleanRuleMapper.getCleaningRuleTreeIds(dataElemId);
        }
        // 2. 转换为VO对象
        List<AttCleanRuleRespVO> voList = BeanUtils.toBean(list, AttCleanRuleRespVO.class);
        // 3. 构建树形结构
        return buildTreeByType(voList);
    }

    @Override
    public Long getCount(String catCode) {
        return attCleanRuleMapper.selectCount(Wrappers.lambdaQuery(AttCleanRuleDO.class)
                .likeRight(AttCleanRuleDO::getCatCode, catCode));
    }

    /**
     * 构建树形结构 - 以type字段作为父节点
     *
     * @param list 规则列表
     * @return 树形结构列表
     */
    private List<AttCleanRuleRespVO> buildTreeByType(List<AttCleanRuleRespVO> list) {
        List<AttCleanRuleRespVO> resultList = new ArrayList<>();
        // 创建type映射，用于存储相同type的节点
        Map<String, List<AttCleanRuleRespVO>> typeMap = list.stream()
                .collect(Collectors.groupingBy(AttCleanRuleRespVO::getType));

        // 遍历每个type分组
        for (Map.Entry<String, List<AttCleanRuleRespVO>> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            List<AttCleanRuleRespVO> typeNodes = entry.getValue();
            for (AttCleanRuleRespVO typeNode : typeNodes) {
                typeNode.setDataType("2");
            }
            // 创建父节点
            AttCleanRuleRespVO parentNode = new AttCleanRuleRespVO();
            parentNode.setId(0L); // 设置一个特殊的ID
            parentNode.setType(type);
            parentNode.setDataType("1");
            String typeName = CleanRuleTypeEnum.getNameByType(type);
            parentNode.setName(typeName); // 设置父节点名称
            parentNode.setChildren(new ArrayList<>(typeNodes));

            resultList.add(parentNode);
        }

        return resultList;
    }
}
