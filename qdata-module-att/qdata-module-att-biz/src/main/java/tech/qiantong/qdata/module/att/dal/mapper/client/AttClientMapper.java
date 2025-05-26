package tech.qiantong.qdata.module.att.dal.mapper.client;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 应用管理Mapper接口
 *
 * @author qdata
 * @date 2025-02-18
 */
public interface AttClientMapper extends BaseMapperX<AttClientDO> {

    default PageResult<AttClientDO> selectPage(AttClientPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<AttClientDO>()
                .eqIfPresent(AttClientDO::getId, reqVO.getId())
                .likeIfPresent(AttClientDO::getName, reqVO.getName())
                .eqIfPresent(AttClientDO::getType, reqVO.getType())
                .eqIfPresent(AttClientDO::getSecret, reqVO.getSecret())
                .eqIfPresent(AttClientDO::getHomepageUrl, reqVO.getHomepageUrl())
                .eqIfPresent(AttClientDO::getAllowUrl, reqVO.getAllowUrl())
                .eqIfPresent(AttClientDO::getSyncUrl, reqVO.getSyncUrl())
                .eqIfPresent(AttClientDO::getLogo, reqVO.getLogo())
                .eqIfPresent(AttClientDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttClientDO::getPublicFlag, reqVO.getPublicFlag())
                .eqIfPresent(AttClientDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(AttClientDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }
}
