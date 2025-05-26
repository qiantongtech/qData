package tech.qiantong.qdata.module.da.dal.mapper.assetchild.gis;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-地理空间服务Mapper接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface DaAssetGisMapper extends BaseMapperX<DaAssetGisDO> {

    default PageResult<DaAssetGisDO> selectPage(DaAssetGisPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetGisDO>()
                .eqIfPresent(DaAssetGisDO::getAssetId, reqVO.getAssetId())
                .eqIfPresent(DaAssetGisDO::getUrl, reqVO.getUrl())
                .eqIfPresent(DaAssetGisDO::getType, reqVO.getType())
                .eqIfPresent(DaAssetGisDO::getHttpMethod, reqVO.getHttpMethod())
                .eqIfPresent(DaAssetGisDO::getCoordinateSystem, reqVO.getCoordinateSystem())
                .eqIfPresent(DaAssetGisDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetGisDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
