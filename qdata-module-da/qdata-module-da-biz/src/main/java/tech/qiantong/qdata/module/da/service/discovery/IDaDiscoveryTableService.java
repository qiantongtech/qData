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

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据发现库信息Service接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryTableService extends IService<DaDiscoveryTableDO> {

    /**
     * 获得数据发现库信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据发现库信息分页列表
     */
    PageResult<DaDiscoveryTableDO> getDaDiscoveryTablePage(DaDiscoveryTablePageReqVO pageReqVO);

    /**
     * 获得全部数据发现库信息列表
     *
     * @return 数据发现库信息列表
     */
    List<DaDiscoveryTableDO> getDaDiscoveryTableList(DaDiscoveryTablePageReqVO discoveryTablePageReqVO);

    /**
     * 创建数据发现库信息
     *
     * @param createReqVO 数据发现库信息信息
     * @return 数据发现库信息编号
     */
    Long createDaDiscoveryTable(DaDiscoveryTableSaveReqVO createReqVO);
    Long createDaDiscoveryTable(DaDiscoveryTableDO createReqVO);

    /**
     * 更新数据发现库信息
     *
     * @param updateReqVO 数据发现库信息信息
     */
    int updateDaDiscoveryTable(DaDiscoveryTableSaveReqVO updateReqVO);
    int updateDaDiscoveryTable(DaDiscoveryTableDO updateReqVO);

    /**
     * 删除数据发现库信息
     *
     * @param idList 数据发现库信息编号
     */
    int removeDaDiscoveryTable(Collection<Long> idList);

    /**
     * 获得数据发现库信息详情
     *
     * @param id 数据发现库信息编号
     * @return 数据发现库信息
     */
    DaDiscoveryTableDO getDaDiscoveryTableById(Long id);

    /**
     * 获得全部数据发现库信息列表
     *
     * @return 数据发现库信息列表
     */
    List<DaDiscoveryTableDO> getDaDiscoveryTableList();

    /**
     * 获得全部数据发现库信息 Map
     *
     * @return 数据发现库信息 Map
     */
    Map<Long, DaDiscoveryTableDO> getDaDiscoveryTableMap();


    /**
     * 导入数据发现库信息数据
     *
     * @param importExcelList 数据发现库信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDiscoveryTable(List<DaDiscoveryTableRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Integer commitOrRevokeDiscoveryInfo(DaDiscoveryTableSaveReqVO daDiscoveryTable);


    Integer updateByTaskIdListAndTableNameStatus(DaDiscoveryTableSaveReqVO daDiscoveryTable);
}
