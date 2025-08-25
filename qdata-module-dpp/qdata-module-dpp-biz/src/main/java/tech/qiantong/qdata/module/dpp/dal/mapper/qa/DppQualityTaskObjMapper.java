package tech.qiantong.qdata.module.dpp.dal.mapper.qa;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据质量任务-稽查对象Mapper接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface DppQualityTaskObjMapper extends BaseMapperX<DppQualityTaskObjDO> {

    default PageResult<DppQualityTaskObjDO> selectPage(DppQualityTaskObjPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<DppQualityTaskObjDO>()
                .likeIfPresent(DppQualityTaskObjDO::getName, reqVO.getName())
                .eqIfPresent(DppQualityTaskObjDO::getDatasourceId, reqVO.getDatasourceId())
                .likeIfPresent(DppQualityTaskObjDO::getTableName, reqVO.getTableName())
                .eqIfPresent(DppQualityTaskObjDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DppQualityTaskObjDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
