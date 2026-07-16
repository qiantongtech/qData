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
 * Business domain Mapper interface
 *
 * @author qdata
 * @date 2026-02-12
 */
public interface McDomainMapper extends BaseMapperX<McDomainDO> {

    default PageResult<McDomainDO> selectPage(McDomainPageReqVO reqVO) {
        // Define the sorting field (prevent SQL injection, consistent with the database field name)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        // Construct dynamic query conditions
        return selectPage(reqVO, new LambdaQueryWrapperX<McDomainDO>()
                .likeIfPresent(McDomainDO::getName, reqVO.getName())
                .eqIfPresent(McDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(McDomainDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(McDomainDO::getCode, reqVO.getCode())
                .eqIfPresent(McDomainDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(McDomainDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(McDomainDO::getRemark, reqVO.getRemark())
                .eqIfPresent(McDomainDO::getDescription, reqVO.getDescription())
                // If reqVO.getName() is not empty, add an exact matching condition for name (name = '<name>')
                // .likeIfPresent(McDomainDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
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
        // FIXME There is a problem during migration
        //queryWrapperX.orderBy(!reqVO.getOrderByColumn().contains("create_time") && !reqVO.getOrderByColumn().contains("createTime"),
        //        false, BaseEntity::getCreateTime);
        return selectList(queryWrapperX);
    }

    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
