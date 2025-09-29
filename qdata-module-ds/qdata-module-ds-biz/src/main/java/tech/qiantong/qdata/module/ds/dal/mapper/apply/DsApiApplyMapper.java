package tech.qiantong.qdata.module.ds.dal.mapper.apply;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyPageReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apply.DsApiApplyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * API服务-申请Mapper接口
 *
 * @author qdata
 * @date 2025-04-22
 */
public interface DsApiApplyMapper extends BaseMapperX<DsApiApplyDO> {

    default PageResult<DsApiApplyDO> selectPage(DsApiApplyPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DsApiApplyDO>()
                .eqIfPresent(DsApiApplyDO::getApiId, reqVO.getApiId())
                .likeIfPresent(DsApiApplyDO::getApplyBy, reqVO.getApplyBy())
                .inIfPresent(DsApiApplyDO::getId,reqVO.getIdList())
                .eqIfPresent(DsApiApplyDO::getApplyTime, reqVO.getApplyTime())
                .eqIfPresent(DsApiApplyDO::getApplyReason, reqVO.getApplyReason())
                .eqIfPresent(DsApiApplyDO::getApproverBy, reqVO.getApproverBy())
                .eqIfPresent(DsApiApplyDO::getApprovalTime, reqVO.getApprovalTime())
                .eqIfPresent(DsApiApplyDO::getApprovalReason, reqVO.getApprovalReason())
                .eqIfPresent(DsApiApplyDO::getValidType, reqVO.getValidType())
                .eqIfPresent(DsApiApplyDO::getValidStartTime, reqVO.getValidStartTime())
                .eqIfPresent(DsApiApplyDO::getValidEndTime, reqVO.getValidEndTime())
                .eqIfPresent(DsApiApplyDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DsApiApplyDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DsApiApplyDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
