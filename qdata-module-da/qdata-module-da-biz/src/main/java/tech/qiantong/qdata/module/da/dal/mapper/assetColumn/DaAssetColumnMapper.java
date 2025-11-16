/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
