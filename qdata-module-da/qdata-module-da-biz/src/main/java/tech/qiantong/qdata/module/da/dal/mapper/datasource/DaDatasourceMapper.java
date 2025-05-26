package tech.qiantong.qdata.module.da.dal.mapper.datasource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据源Mapper接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaDatasourceMapper extends BaseMapperX<DaDatasourceDO> {

    default PageResult<DaDatasourceDO> selectPage(DaDatasourcePageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DaDatasourceDO>()
                .likeIfPresent(DaDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .inIfPresent(DaDatasourceDO::getDatasourceType, StringUtils.isNotEmpty(reqVO.getDatasourceType()) ? reqVO.getDatasourceType().split(",") : null)
                .eqIfPresent(DaDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eqIfPresent(DaDatasourceDO::getIp, reqVO.getIp())
                .eqIfPresent(DaDatasourceDO::getPort, reqVO.getPort())
                .eqIfPresent(DaDatasourceDO::getListCount, reqVO.getListCount())
                .eqIfPresent(DaDatasourceDO::getSyncCount, reqVO.getSyncCount())
                .eqIfPresent(DaDatasourceDO::getDataSize, reqVO.getDataSize())
                .eqIfPresent(DaDatasourceDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DaDatasourceDO::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(DaDatasourceDO::getId, reqVO.getIdList())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DaDatasourceDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    public List<DaDatasourceDO> getDataSourceByAsset();
}
