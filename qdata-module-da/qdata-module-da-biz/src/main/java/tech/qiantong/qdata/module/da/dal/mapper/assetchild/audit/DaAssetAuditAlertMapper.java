package tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-质量预警Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetAuditAlertMapper extends BaseMapperX<DaAssetAuditAlertDO> {

    default PageResult<DaAssetAuditAlertDO> selectPage(DaAssetAuditAlertPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetAuditAlertDO>()
                .eqIfPresent(DaAssetAuditAlertDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetAuditAlertDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(DaAssetAuditAlertDO::getAuditTime, reqVO.getAuditTime())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertTime, reqVO.getAlertTime())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertMessage, reqVO.getAlertMessage())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertChannels, reqVO.getAlertChannels())
                .eqIfPresent(DaAssetAuditAlertDO::getAlertChannelResult, reqVO.getAlertChannelResult())
                .eqIfPresent(DaAssetAuditAlertDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetAuditAlertDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
