package tech.qiantong.qdata.module.dg.dal.mapper.standard;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Standard Data Element Mapper Interface
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DgDataElemMapper extends BaseMapperX<DgDataElemDO> {

    default PageResult<DgDataElemDO> selectPage(DgDataElemPageReqVO reqVO) {
        // Define allowed sort columns (prevent SQL injection, must match database field names)
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapperX<DgDataElemDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DgDataElemDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("DG_DATA_ELEM_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DgDataElemDO::getName, reqVO.getName())
                .like(StringUtils.isNotBlank(reqVO.getEngName()), DgDataElemDO::getEngName, reqVO.getEngName())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DgDataElemDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DgDataElemDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getColumnType()), DgDataElemDO::getColumnType, reqVO.getColumnType())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DgDataElemDO::getStatus, reqVO.getStatus())
                .eq(reqVO.getDocumentId() != null, DgDataElemDO::getDocumentId, reqVO.getDocumentId());
        lambdaWrapper.orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        return selectJoinPage(reqVO, DgDataElemDO.class, lambdaWrapper);
    }

    default List<DgDataElemDO> selectList(DgDataElemPageReqVO reqVO) {
        LambdaQueryWrapperX<DgDataElemDO> queryWrapper = new LambdaQueryWrapperX<>();
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        queryWrapper.likeIfPresent(DgDataElemDO::getName, reqVO.getName())
                .likeIfPresent(DgDataElemDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DgDataElemDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(DgDataElemDO::getType, reqVO.getType())
                .eqIfPresent(DgDataElemDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DgDataElemDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DgDataElemDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DgDataElemDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DgDataElemDO::getDocumentId, reqVO.getDocumentId())
                .eqIfPresent(DgDataElemDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DgDataElemDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(DgDataElemDO::getName, reqVO.getName())
                // Sort by createTime field in descending order
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);
        return selectList(queryWrapper);
    }

    /**
     * Check whether the current metadata is used by models and assets
     *
     * @param idList
     * @return
     */
    Long checkHasRel(@Param("idList") List<Long> idList);

    default boolean existsByCatCode(String catCode) {
        return exists(Wrappers.lambdaQuery(DgDataElemDO.class)
                .likeRight(DgDataElemDO::getCatCode, catCode));
    }

    /**
     * Batch update old CAT_CODE to new CAT_CODE
     *
     * @param oldCatCode old category code
     * @param newCatCode new category code
     * @return number of affected rows
     */
    default int updateCatCode(String oldCatCode, String newCatCode) {
        return this.update(
                null,
                Wrappers.<DgDataElemDO>lambdaUpdate()
                        .set(DgDataElemDO::getCatCode, newCatCode)
                        .eq(DgDataElemDO::getDelFlag, "0")
                        .eq(DgDataElemDO::getCatCode, oldCatCode)
        );
    }

}
