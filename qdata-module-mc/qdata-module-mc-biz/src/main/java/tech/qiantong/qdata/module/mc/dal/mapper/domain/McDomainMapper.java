package tech.qiantong.qdata.module.mc.dal.mapper.domain;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 业务域Mapper接口
 *
 * @author qdata
 * @date 2026-02-12
 */
public interface McDomainMapper extends BaseMapperX<McDomainDO> {

    default PageResult<McDomainDO> selectPage(McDomainPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // 构造动态查询条件
        return selectPage(reqVO, new LambdaQueryWrapperX<McDomainDO>()
                .likeIfPresent(McDomainDO::getName, reqVO.getName())
                .eqIfPresent(McDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(McDomainDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(McDomainDO::getCode, reqVO.getCode())
                .eqIfPresent(McDomainDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(McDomainDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McDomainDO::getRemark, reqVO.getRemark())
                .eqIfPresent(McDomainDO::getDescription, reqVO.getDescription())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(McDomainDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns));
    }

    default List<McDomainDO> selectList(McDomainPageReqVO reqVO) {
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time", "sort_order"));
        if (StringUtils.isBlank(reqVO.getOrderByColumn())) {
            reqVO.setOrderByColumn("sort_order");
            reqVO.setIsAsc("asc");
        }
        LambdaQueryWrapperX<McDomainDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(McDomainDO::getName, reqVO.getName())
                .eqIfPresent(McDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(McDomainDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(McDomainDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(McDomainDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(McDomainDO::getCode, reqVO.getCode())
                .eqIfPresent(McDomainDO::getCreateTime, reqVO.getCreateTime())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        // FIXME 迁移时候存在问题
        //queryWrapperX.orderBy(!reqVO.getOrderByColumn().contains("create_time") && !reqVO.getOrderByColumn().contains("createTime"),
        //        false, BaseEntity::getCreateTime);
        return selectList(queryWrapperX);
    }

    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
