package tech.qiantong.qdata.module.dg.dal.mapper.standard;

import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据元类目管理Mapper接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface DgDataElemCatMapper extends BaseMapperX<DgDataElemCatDO> {

    default List<DgDataElemCatDO> selectList(DgDataElemCatPageReqVO reqVO) {
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time", "sort_order"));
        if (StringUtils.isBlank(reqVO.getOrderByColumn())) {
            reqVO.setOrderByColumn("sort_order");
            reqVO.setIsAsc("asc");
        }
        LambdaQueryWrapperX<DgDataElemCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(DgDataElemCatDO::getName, reqVO.getName())
                .eqIfPresent(DgDataElemCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DgDataElemCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(DgDataElemCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DgDataElemCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(DgDataElemCatDO::getCode, reqVO.getCode())
                .eqIfPresent(DgDataElemCatDO::getCreateTime, reqVO.getCreateTime())
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns)
                .orderBy(!reqVO.getOrderByColumn().contains("create_time") && !reqVO.getOrderByColumn().contains("createTime"),
                        false, BaseEntity::getCreateTime);
        return selectList(queryWrapperX);
    }

    int updateValidFlag(@Param("prefixCode") String prefixCode, @Param("validFlag") Boolean validFlag);

}
