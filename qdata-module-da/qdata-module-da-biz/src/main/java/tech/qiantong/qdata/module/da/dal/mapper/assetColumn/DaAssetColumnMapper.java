package tech.qiantong.qdata.module.da.dal.mapper.assetColumn;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 数据资产字段Mapper接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface DaAssetColumnMapper extends BaseMapperX<DaAssetColumnDO> {

    default PageResult<DaAssetColumnDO> selectPage(DaAssetColumnPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));
        MPJLambdaWrapper<DaAssetColumnDO> lambdaQueryWrapper = new MPJLambdaWrapper();
        lambdaQueryWrapper.selectAll(DaAssetColumnDO.class)
                .select("t2.SENSITIVE_LEVEl as sensitiveLevelName")
                .leftJoin("DA_SENSITIVE_LEVEL t2 on t.SENSITIVE_LEVEL_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(StringUtils.isNotBlank(reqVO.getAssetId()),DaAssetColumnDO::getAssetId, reqVO.getAssetId())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        // 构造动态查询条件
        return selectJoinPage(reqVO, DaAssetColumnDO.class, lambdaQueryWrapper);
    }

    int updateDaAssetColumn(DaAssetColumnDO daAssetColumnDO);

    void deleteAssetColumnByAssetId(Long assetId);


    /**
     * 根据资产详情进行查询字段属性
     */
    default List<DaAssetColumnDO> findByAssetId(Long assetId) {
        LambdaQueryWrapper<DaAssetColumnDO> queryWrapper = Wrappers.<DaAssetColumnDO>lambdaQuery()
                .eq(DaAssetColumnDO::getAssetId, assetId)
                .eq(DaAssetColumnDO::getDelFlag, 0)
                .orderByAsc(DaAssetColumnDO::getId);
        return selectList(queryWrapper);
    }

}
