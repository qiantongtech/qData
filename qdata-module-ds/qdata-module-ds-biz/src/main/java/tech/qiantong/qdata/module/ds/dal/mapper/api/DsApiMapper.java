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

package tech.qiantong.qdata.module.ds.dal.mapper.api;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.DsApiPageReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;

/**
 * API服务Mapper接口
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface DsApiMapper extends BaseMapperX<DsApiDO> {

    default PageResult<DsApiDO> selectPage(DsApiPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        MPJLambdaWrapper<DsApiDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DsApiDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_API_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .between(reqVO.getParamByKey("beginCreateTime") != null && reqVO.getParamByKey("endCreateTime") != null,
                        DsApiDO::getCreateTime,
                        reqVO.getParamByKey("beginCreateTime"),
                        reqVO.getParamByKey("endCreateTime"))
                .like(StringUtils.isNotBlank(reqVO.getName()),DsApiDO::getName, reqVO.getName())
                .in(reqVO.getApiIdList() != null && !reqVO.getApiIdList().isEmpty(),DsApiDO::getId,reqVO.getApiIdList())
                .in(reqVO.getCatIds() != null && !reqVO.getCatIds().isEmpty(),DsApiDO::getCatId,reqVO.getCatIds())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DsApiDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()),DsApiDO::getStatus, reqVO.getStatus())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DsApiDO.class, lambdaWrapper);
    }
}
