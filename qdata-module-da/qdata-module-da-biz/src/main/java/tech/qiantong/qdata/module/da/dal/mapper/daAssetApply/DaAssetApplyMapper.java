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

package tech.qiantong.qdata.module.da.dal.mapper.daAssetApply;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.collections4.CollectionUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 数据资产申请Mapper接口
 *
 * @author shu
 * @date 2025-03-19
 */
public interface DaAssetApplyMapper extends BaseMapperX<DaAssetApplyDO> {

    default PageResult<DaAssetApplyDO> selectPage(DaAssetApplyPageReqVO reqVO) {
        MPJLambdaWrapper<DaAssetApplyDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DaAssetApplyDO.class)
                .select("t2.NAME AS assetName, t2.TABLE_NAME AS assetTableName, t5.NAME AS catAssetName, t5.CODE AS carAssetCode, t3.NAME AS projectName")
                .leftJoin("DA_ASSET t2 on t.ASSET_ID = t2.ID")
                .leftJoin("ATT_PROJECT t3 on t.PROJECT_ID = t3.ID AND t3.DEL_FLAG = 0")
                .leftJoin("ATT_ASSET_CAT t5 on t2.CAT_CODE = t5.CODE AND t5.DEL_FLAG = 0")
                .eq("t2.DEL_FLAG", 0)
                .in(CollectionUtils.isNotEmpty(reqVO.getAssetIds()), "t2.ID", reqVO.getAssetIds())
                .like(StringUtils.isNotEmpty(reqVO.getAssetName()), "t2.NAME", reqVO.getAssetName())
                .like(StringUtils.isNotEmpty(reqVO.getCreateBy()), DaAssetApplyDO::getCreateBy, reqVO.getCreateBy())
                .eq(StringUtils.isNotEmpty(reqVO.getStatus()), DaAssetApplyDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotEmpty(reqVO.getCatAssetCode()), "t5.CODE", reqVO.getCatAssetCode())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DaAssetApplyDO.class, lambdaWrapper);
    }
}
