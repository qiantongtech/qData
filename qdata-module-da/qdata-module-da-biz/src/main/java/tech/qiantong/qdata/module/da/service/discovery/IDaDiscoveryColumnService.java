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
 */

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据发现字段Service接口
 *
 * @author qdata
 * @date 2025-02-11
 */
public interface IDaDiscoveryColumnService extends IService<DaDiscoveryColumnDO> {

    /**
     * 获得数据发现字段分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据发现字段分页列表
     */
    PageResult<DaDiscoveryColumnDO> getDaDiscoveryColumnPage(DaDiscoveryColumnPageReqVO pageReqVO);

    /**
     * 获得全部数据发现字段列表
     *
     * @return 数据发现字段列表
     */
    List<DaDiscoveryColumnDO> getDaDiscoveryColumnList(DaDiscoveryColumnPageReqVO pageReqVO);

    /**
     * 创建数据发现字段
     *
     * @param createReqVO 数据发现字段信息
     * @return 数据发现字段编号
     */
    Long createDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO createReqVO);
    Long createDaDiscoveryColumn(DaDiscoveryColumnDO createReqVO);

    /**
     * 更新数据发现字段
     *
     * @param updateReqVO 数据发现字段信息
     */
    int updateDaDiscoveryColumn(DaDiscoveryColumnSaveReqVO updateReqVO);
    int updateDaDiscoveryColumn(DaDiscoveryColumnDO updateReqVO);

    /**
     * 删除数据发现字段
     *
     * @param idList 数据发现字段编号
     */
    int removeDaDiscoveryColumn(Collection<Long> idList);

    /**
     * 获得数据发现字段详情
     *
     * @param id 数据发现字段编号
     * @return 数据发现字段
     */
    DaDiscoveryColumnDO getDaDiscoveryColumnById(Long id);

    /**
     * 获得全部数据发现字段列表
     *
     * @return 数据发现字段列表
     */
    List<DaDiscoveryColumnDO> getDaDiscoveryColumnList();

    /**
     * 获得全部数据发现字段 Map
     *
     * @return 数据发现字段 Map
     */
    Map<Long, DaDiscoveryColumnDO> getDaDiscoveryColumnMap();


    /**
     * 导入数据发现字段数据
     *
     * @param importExcelList 数据发现字段数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaDiscoveryColumn(List<DaDiscoveryColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
