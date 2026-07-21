/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.ds.service.api;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.*;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.SqlParseDto;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * API service service interface
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface IDsApiService extends IService<DsApiDO> {

    /**
     * Returns a paginated API service list.
     *
     * @param pageReqVO pagination request
     * @return the paginated API service list
     */
    PageResult<DsApiDO> getDsApiPage(DsApiPageReqVO pageReqVO);

    /**
     * Creates an API service.
     *
     * @param createReqVO API service information
     * @return the API service ID
     */
    Long createDsApi(DsApiSaveReqVO createReqVO);

    /**
     * Updates an API service.
     *
     * @param updateReqVO API service information
     */
    int updateDsApi(DsApiSaveReqVO updateReqVO);

    /**
     * Deletes an API service.
     *
     * @param idList API service IDs
     */
    int removeDsApi(Collection<Long> idList);

    /**
     * Returns API service details.
     *
     * @param id API service ID
     * @return the API service
     */
    DsApiDO getDsApiById(Long id);

    /**
     * Returns all API services.
     *
     * @return the API service list
     */
    List<DsApiDO> getDsApiList();

    /**
     * Returns all API services as a map.
     *
     * @return API services as a map
     */
    Map<Long, DsApiDO> getDsApiMap();


    /**
     * Imports API service data.
     *
     * @param importExcelList API service data list
     * @param isUpdateSupport whether existing records should be updated
     * @param operName operator
     * @return the result
     */
    String importDsApi(List<DsApiRespVO> importExcelList, boolean isUpdateSupport, String operName);


    SqlParseVo sqlParse(SqlParseDto sqlParseDto);


    Object serviceTesting(DsApiDO dataApi);


    AjaxResult saveDataApi(DsApiDO dataApi);


    AjaxResult updateDataApi(DsApiDO dataApi);


    void releaseDataApi(String id,Long updateId, String updateBy);

    void cancelDataApi(String id,Long updateId, String updateBy);

    DsApiDO repeatFlag(JSONObject jsonObject);

    void queryServiceForwarding(HttpServletResponse response, DsApiReqVO dsApiReqVO);
}
