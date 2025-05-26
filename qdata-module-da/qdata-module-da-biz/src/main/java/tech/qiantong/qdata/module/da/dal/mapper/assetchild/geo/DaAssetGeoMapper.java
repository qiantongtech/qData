package tech.qiantong.qdata.module.da.dal.mapper.assetchild.geo;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据资产-矢量Mapper接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface DaAssetGeoMapper extends BaseMapperX<DaAssetGeoDO> {

    default PageResult<DaAssetGeoDO> selectPage(DaAssetGeoPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaAssetGeoDO>()
                .eqIfPresent(DaAssetGeoDO::getAssetId, reqVO.getAssetId())
                .likeIfPresent(DaAssetGeoDO::getFileName, reqVO.getFileName())
                .eqIfPresent(DaAssetGeoDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(DaAssetGeoDO::getFileType, reqVO.getFileType())
                .eqIfPresent(DaAssetGeoDO::getElementType, reqVO.getElementType())
                .eqIfPresent(DaAssetGeoDO::getCoordinateSystem, reqVO.getCoordinateSystem())
                .eqIfPresent(DaAssetGeoDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaAssetGeoDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
