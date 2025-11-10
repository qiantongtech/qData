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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.dal.mapper.dataElem;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemAssetRelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemAssetRelDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * 数据元数据资产关联信息Mapper接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface DpDataElemAssetRelMapper extends BaseMapperX<DpDataElemAssetRelDO> {

    default PageResult<DpDataElemAssetRelDO> selectPage(DpDataElemAssetRelPageReqVO reqVO) {
        MPJLambdaWrapper<DpDataElemAssetRelDO> lambdaWrapper = new MPJLambdaWrapper<>();
        lambdaWrapper.selectAll(DpDataElemAssetRelDO.class)
                .select("t2.name AS assetName","t2.table_comment AS tableComment","t2.DESCRIPTION AS description")
                .leftJoin("DA_ASSET t2 on t.asset_id = t2.id AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DpDataElemAssetRelDO::getTableName,
                        reqVO.getTableName())
                .like(StringUtils.isNotBlank(reqVO.getColumnName()), DpDataElemAssetRelDO::getColumnName,
                        reqVO.getColumnName())
                .eq(reqVO.getDataElemType() != null, DpDataElemAssetRelDO::getDataElemType, reqVO.getDataElemType())
                .eq(reqVO.getDataElemId() != null, DpDataElemAssetRelDO::getDataElemId, reqVO.getDataElemId())
                .eq(reqVO.getAssetId() != null, DpDataElemAssetRelDO::getAssetId, reqVO.getAssetId())
                .eq(reqVO.getColumnId() != null, DpDataElemAssetRelDO::getColumnId, reqVO.getColumnId())
                .eq(reqVO.getCreateTime() != null, DpDataElemAssetRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                        StringUtils.equals("asc", reqVO.getIsAsc()),
                        StringUtils.isNotBlank(reqVO.getOrderByColumn())
                                ? Arrays.asList(reqVO.getOrderByColumn().split(","))
                                : null);

        return selectJoinPage(reqVO, DpDataElemAssetRelDO.class, lambdaWrapper);
    }
}
