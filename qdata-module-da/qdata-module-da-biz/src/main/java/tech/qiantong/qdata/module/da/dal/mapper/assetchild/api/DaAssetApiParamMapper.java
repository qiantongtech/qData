package tech.qiantong.qdata.module.da.dal.mapper.assetchild.api;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-外部API-参数Mapper接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface DaAssetApiParamMapper extends BaseMapperX<DaAssetApiParamDO> {

    default PageResult<DaAssetApiParamDO> selectPage(DaAssetApiParamPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetApiParamDO>()
                .eqIfPresent(DaAssetApiParamDO::getApiId, reqVO.getApiId())
                .eqIfPresent(DaAssetApiParamDO::getParentId, reqVO.getParentId())
                .likeIfPresent(DaAssetApiParamDO::getName, reqVO.getName())
                .eqIfPresent(DaAssetApiParamDO::getType, reqVO.getType())
                .eqIfPresent(DaAssetApiParamDO::getRequestFlag, reqVO.getRequestFlag())
                .eqIfPresent(DaAssetApiParamDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DaAssetApiParamDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetApiParamDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    void removeThemeRelByAssetApiId(Long id);
}
