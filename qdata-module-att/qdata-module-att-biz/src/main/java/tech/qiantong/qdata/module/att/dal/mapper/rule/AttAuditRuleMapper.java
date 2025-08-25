package tech.qiantong.qdata.module.att.dal.mapper.rule;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 稽查规则Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface AttAuditRuleMapper extends BaseMapperX<AttAuditRuleDO> {

    default PageResult<AttAuditRuleDO> selectPage(AttAuditRulePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttAuditRuleDO>()
                .likeIfPresent(AttAuditRuleDO::getName, reqVO.getName())
                .eqIfPresent(AttAuditRuleDO::getQualityDim, reqVO.getQualityDim())
                .eqIfPresent(AttAuditRuleDO::getType, reqVO.getType())
                .eqIfPresent(AttAuditRuleDO::getLevel, reqVO.getLevel())
                .eqIfPresent(AttAuditRuleDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttAuditRuleDO::getCode, reqVO.getCode())
                .likeIfPresent(AttAuditRuleDO::getUseCase, reqVO.getUseCase())
                .likeIfPresent(AttAuditRuleDO::getExample, reqVO.getExample())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttAuditRuleDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
//                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
         .orderByDesc(AttAuditRuleDO::getCreateTime));

    }

    List<AttAuditRuleDO> selectAttAuditRuleList(@Param("dataElemId") Long dataElemId);
}
