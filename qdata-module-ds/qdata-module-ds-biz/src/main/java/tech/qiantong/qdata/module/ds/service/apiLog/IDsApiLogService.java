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

package tech.qiantong.qdata.module.ds.service.apiLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * API服务调用日志Service接口
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface IDsApiLogService extends IService<DsApiLogDO> {

    /**
     * 获得API服务调用日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return API服务调用日志分页列表
     */
    PageResult<DsApiLogDO> getDsApiLogPage(DsApiLogPageReqVO pageReqVO);

    /**
     * 创建API服务调用日志
     *
     * @param createReqVO API服务调用日志信息
     * @return API服务调用日志编号
     */
    Long createDsApiLog(DsApiLogSaveReqVO createReqVO);

    /**
     * 更新API服务调用日志
     *
     * @param updateReqVO API服务调用日志信息
     */
    int updateDsApiLog(DsApiLogSaveReqVO updateReqVO);

    /**
     * 删除API服务调用日志
     *
     * @param idList API服务调用日志编号
     */
    int removeDsApiLog(Collection<Long> idList);

    /**
     * 获得API服务调用日志详情
     *
     * @param id API服务调用日志编号
     * @return API服务调用日志
     */
    DsApiLogDO getDsApiLogById(Long id);

    /**
     * 获得全部API服务调用日志列表
     *
     * @return API服务调用日志列表
     */
    List<DsApiLogDO> getDsApiLogList();

    /**
     * 获得全部API服务调用日志 Map
     *
     * @return API服务调用日志 Map
     */
    Map<Long, DsApiLogDO> getDsApiLogMap();


    /**
     * 导入API服务调用日志数据
     *
     * @param importExcelList API服务调用日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDsApiLog(List<DsApiLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
