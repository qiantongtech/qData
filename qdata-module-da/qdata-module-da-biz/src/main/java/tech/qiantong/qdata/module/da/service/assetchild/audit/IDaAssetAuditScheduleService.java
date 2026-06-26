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
