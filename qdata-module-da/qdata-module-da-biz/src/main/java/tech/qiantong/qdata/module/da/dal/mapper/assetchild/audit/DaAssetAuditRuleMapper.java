package tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产质量结果记录Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetAuditRuleMapper extends BaseMapperX<DaAssetAuditRuleDO> {

    default PageResult<DaAssetAuditRuleDO> selectPage(DaAssetAuditRulePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetAuditRuleDO>()
                .eqIfPresent(DaAssetAuditRuleDO::getAssetId, reqVO.getAssetId())
                .likeIfPresent(DaAssetAuditRuleDO::getTableName, reqVO.getTableName())
                .likeIfPresent(DaAssetAuditRuleDO::getColumnName, reqVO.getColumnName())
                .eqIfPresent(DaAssetAuditRuleDO::getColumnComment, reqVO.getColumnComment())
                .likeIfPresent(DaAssetAuditRuleDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(DaAssetAuditRuleDO::getQualityDim, reqVO.getQualityDim())
                .eqIfPresent(DaAssetAuditRuleDO::getRuleType, reqVO.getRuleType())
                .eqIfPresent(DaAssetAuditRuleDO::getRuleLevel, reqVO.getRuleLevel())
                .eqIfPresent(DaAssetAuditRuleDO::getRuleDescription, reqVO.getRuleDescription())
                .eqIfPresent(DaAssetAuditRuleDO::getRuleConfig, reqVO.getRuleConfig())
                .eqIfPresent(DaAssetAuditRuleDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(DaAssetAuditRuleDO::getIssueCount, reqVO.getIssueCount())
                .eqIfPresent(DaAssetAuditRuleDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(DaAssetAuditRuleDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(DaAssetAuditRuleDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetAuditRuleDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
