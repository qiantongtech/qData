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
 * API服务Service接口
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface IDsApiService extends IService<DsApiDO> {

    /**
     * 获得API服务分页列表
     *
     * @param pageReqVO 分页请求
     * @return API服务分页列表
     */
    PageResult<DsApiDO> getDsApiPage(DsApiPageReqVO pageReqVO);

    /**
     * 创建API服务
     *
     * @param createReqVO API服务信息
     * @return API服务编号
     */
    Long createDsApi(DsApiSaveReqVO createReqVO);

    /**
     * 更新API服务
     *
     * @param updateReqVO API服务信息
     */
    int updateDsApi(DsApiSaveReqVO updateReqVO);

    /**
     * 删除API服务
     *
     * @param idList API服务编号
     */
    int removeDsApi(Collection<Long> idList);

    /**
     * 获得API服务详情
     *
     * @param id API服务编号
     * @return API服务
     */
    DsApiDO getDsApiById(Long id);

    /**
     * 获得全部API服务列表
     *
     * @return API服务列表
     */
    List<DsApiDO> getDsApiList();

    /**
     * 获得全部API服务 Map
     *
     * @return API服务 Map
     */
    Map<Long, DsApiDO> getDsApiMap();


    /**
     * 导入API服务数据
     *
     * @param importExcelList API服务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
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
