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

package tech.qiantong.qdata.module.da.service.assetchild.audit;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 资产稽查调度Service接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetAuditScheduleService extends IService<DaAssetAuditScheduleDO> {

    /**
     * 获得资产稽查调度分页列表
     *
     * @param pageReqVO 分页请求
     * @return 资产稽查调度分页列表
     */
    PageResult<DaAssetAuditScheduleDO> getDaAssetAuditSchedulePage(DaAssetAuditSchedulePageReqVO pageReqVO);

    /**
     * 创建资产稽查调度
     *
     * @param createReqVO 资产稽查调度信息
     * @return 资产稽查调度编号
     */
    Long createDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO createReqVO);

    /**
     * 更新资产稽查调度
     *
     * @param updateReqVO 资产稽查调度信息
     */
    int updateDaAssetAuditSchedule(DaAssetAuditScheduleSaveReqVO updateReqVO);

    /**
     * 删除资产稽查调度
     *
     * @param idList 资产稽查调度编号
     */
    int removeDaAssetAuditSchedule(Collection<Long> idList);

    /**
     * 获得资产稽查调度详情
     *
     * @param id 资产稽查调度编号
     * @return 资产稽查调度
     */
    DaAssetAuditScheduleDO getDaAssetAuditScheduleById(Long id);

    /**
     * 获得全部资产稽查调度列表
     *
     * @return 资产稽查调度列表
     */
    List<DaAssetAuditScheduleDO> getDaAssetAuditScheduleList();

    /**
     * 获得全部资产稽查调度 Map
     *
     * @return 资产稽查调度 Map
     */
    Map<Long, DaAssetAuditScheduleDO> getDaAssetAuditScheduleMap();


    /**
     * 导入资产稽查调度数据
     *
     * @param importExcelList 资产稽查调度数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetAuditSchedule(List<DaAssetAuditScheduleRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
