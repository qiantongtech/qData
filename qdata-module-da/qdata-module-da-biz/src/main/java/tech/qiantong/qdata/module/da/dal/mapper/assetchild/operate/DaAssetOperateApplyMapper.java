package tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产操作申请Mapper接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface DaAssetOperateApplyMapper extends BaseMapperX<DaAssetOperateApplyDO> {

    default PageResult<DaAssetOperateApplyDO> selectPage(DaAssetOperateApplyPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetOperateApplyDO>()
                .eqIfPresent(DaAssetOperateApplyDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetOperateApplyDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(DaAssetOperateApplyDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DaAssetOperateApplyDO::getTableComment, reqVO.getTableComment())
                .eqIfPresent(DaAssetOperateApplyDO::getOperateType, reqVO.getOperateType())
                .eqIfPresent(DaAssetOperateApplyDO::getOperateJson, reqVO.getOperateJson())
                .eqIfPresent(DaAssetOperateApplyDO::getOperateTime, reqVO.getOperateTime())
                .eqIfPresent(DaAssetOperateApplyDO::getExecuteFlag, reqVO.getExecuteFlag())
                .eqIfPresent(DaAssetOperateApplyDO::getExecuteTime, reqVO.getExecuteTime())
                .eqIfPresent(DaAssetOperateApplyDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetOperateApplyDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
