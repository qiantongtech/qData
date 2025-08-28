package tech.qiantong.qdata.module.da.dal.mapper.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;

import java.util.Arrays;

import tech.qiantong.qdata.common.core.page.PageResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

/**
 * 数据资产Mapper接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaAssetMapper extends BaseMapperX<DaAssetDO> {

    default PageResult<DaAssetDO> selectPage(DaAssetPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName")
                .select("t3.PROJECT_ID AS projectId,t3.PROJECT_CODE AS projectCode")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_ASSET_PROJECT_REL t3 on t.id = t3.ASSET_ID AND t3.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaAssetDO::getCatCode, reqVO.getCatCode())
//                .eq(DaAssetDO::getDelFlag,"")
                .like(StringUtils.isNotBlank(reqVO.getName()), DaAssetDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getDatasourceId()), DaAssetDO::getDatasourceId, reqVO.getDatasourceId())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DaAssetDO::getType, reqVO.getType())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaAssetDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaAssetDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaAssetDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DaAssetDO::getDescription, reqVO.getDescription())
                .in(reqVO.getThemeAssetIdList() != null && !reqVO.getThemeAssetIdList().isEmpty(), DaAssetDO::getId, reqVO.getThemeAssetIdList())
                .in(reqVO.getAssetIdList() != null && reqVO.getAssetIdList().size() > 0, DaAssetDO::getId, reqVO.getAssetIdList())
                .and(wrapper -> wrapper
                        .isNull("t3.PROJECT_CODE")
                        .or(inner -> inner.isNotNull("t3.PROJECT_CODE").eq("t.STATUS", "1"))
                )
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, DaAssetDO.class, lambdaWrapper);
    }

    default PageResult<DaAssetDO> selectPageDpp(DaAssetPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName")
                .select("t3.PROJECT_ID AS projectId,t3.PROJECT_CODE AS projectCode")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_ASSET_PROJECT_REL t3 on t.id = t3.ASSET_ID AND t3.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaAssetDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DaAssetDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getDatasourceId()), DaAssetDO::getDatasourceId, reqVO.getDatasourceId())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DaAssetDO::getType, reqVO.getType())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaAssetDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaAssetDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaAssetDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DaAssetDO::getDescription, reqVO.getDescription())
                .in(reqVO.getThemeAssetIdList() != null && !reqVO.getThemeAssetIdList().isEmpty(), DaAssetDO::getId, reqVO.getThemeAssetIdList())
                .and(wrapper -> wrapper
                        .in(reqVO.getAssetIdList() != null && !reqVO.getAssetIdList().isEmpty(), DaAssetDO::getId, reqVO.getAssetIdList())
                        .or(inner -> inner
                                .eq(reqVO.getProjectId() != null, "t3.PROJECT_ID", reqVO.getProjectId())
                                .eq(StringUtils.isNotBlank(reqVO.getProjectCode()), "t3.PROJECT_CODE", reqVO.getProjectCode())
                        )
                )
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return selectJoinPage(reqVO, DaAssetDO.class, lambdaWrapper);
    }

    void deleteAssetById(Long id);


    default List<DaAssetDO> findByDatasourceIdAndTableName(Long datasourceId, String tableName) {
        LambdaQueryWrapper<DaAssetDO> queryWrapper = Wrappers.<DaAssetDO>lambdaQuery()
                .eq(DaAssetDO::getDatasourceId, datasourceId)
                .eq(DaAssetDO::getTableName, tableName);
        return selectList(queryWrapper);
    }

}
