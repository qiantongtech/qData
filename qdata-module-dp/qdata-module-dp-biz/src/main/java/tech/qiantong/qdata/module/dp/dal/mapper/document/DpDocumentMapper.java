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

package tech.qiantong.qdata.module.dp.dal.mapper.document;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentSearchReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.DpDocumentSearchRespVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.mybatis.core.mapper.BaseMapperX;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 标准信息登记Mapper接口
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface DpDocumentMapper extends BaseMapperX<DpDocumentDO> {


    default PageResult<DpDocumentDO> selectPage(DpDocumentPageReqVO reqVO) {
        // 定义排序的字段（防止 SQL 注入，与数据库字段名称一致）
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        MPJLambdaWrapper<DpDocumentDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDocumentDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DOCUMENT_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(StringUtils.isNotBlank(reqVO.getName()), DpDocumentDO::getName, reqVO.getName())
                .like(StringUtils.isNotBlank(reqVO.getCode()), DpDocumentDO::getCode, reqVO.getCode())
                .and(StringUtils.isNotBlank(reqVO.getKeyWordParam()),
                        q -> q.like(DpDocumentDO::getCode, reqVO.getKeyWordParam())
                                .or()
                                .like(DpDocumentDO::getName, reqVO.getKeyWordParam()))
                .like(StringUtils.isNotBlank(reqVO.getIssuingAgency()), DpDocumentDO::getIssuingAgency, reqVO.getIssuingAgency())
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DpDocumentDO::getCatCode, reqVO.getCatCode())
                .eq(StringUtils.isNotBlank(reqVO.getType()),DpDocumentDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()),DpDocumentDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getVersion()),DpDocumentDO::getVersion, reqVO.getVersion());
        if ("1".equals(reqVO.getExistStandardUrl())) {
            lambdaWrapper.isNotNull(DpDocumentDO::getFileUrl)
                    .ne(DpDocumentDO::getFileUrl, "");
        }
        lambdaWrapper.orderByStr(
                StringUtils.isNotBlank(reqVO.getOrderByColumn()),
                StringUtils.equals("asc", reqVO.getIsAsc()),
                StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);
        return selectJoinPage(reqVO, DpDocumentDO.class, lambdaWrapper);
    }

    /**
     * 标准检索分页列表
     *
     * @param page
     * @param dpDocument
     * @return
     */
    IPage<DpDocumentSearchRespVO> getDpDocumentSearchPage(Page page, @Param("params") DpDocumentSearchReqVO dpDocument);
}
