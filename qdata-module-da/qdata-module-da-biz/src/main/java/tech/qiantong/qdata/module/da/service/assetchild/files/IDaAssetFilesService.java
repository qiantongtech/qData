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

package tech.qiantong.qdata.module.da.service.assetchild.files;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据资产-文件服务Service接口
 *
 * @author qdata
 * @date 2025-06-26
 */
public interface IDaAssetFilesService extends IService<DaAssetFilesDO> {

    /**
     * 获得数据资产-文件服务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-文件服务分页列表
     */
    PageResult<DaAssetFilesDO> getDaAssetFilesPage(DaAssetFilesPageReqVO pageReqVO);

    /**
     * 创建数据资产-文件服务
     *
     * @param createReqVO 数据资产-文件服务信息
     * @return 数据资产-文件服务编号
     */
    Long createDaAssetFiles(DaAssetFilesSaveReqVO createReqVO);

    /**
     * 更新数据资产-文件服务
     *
     * @param updateReqVO 数据资产-文件服务信息
     */
    int updateDaAssetFiles(DaAssetFilesSaveReqVO updateReqVO);

    /**
     * 删除数据资产-文件服务
     *
     * @param idList 数据资产-文件服务编号
     */
    int removeDaAssetFiles(Collection<Long> idList);

    /**
     * 获得数据资产-文件服务详情
     *
     * @param id 数据资产-文件服务编号
     * @return 数据资产-文件服务
     */
    DaAssetFilesDO getDaAssetFilesById(Long id);

    /**
     * 获得全部数据资产-文件服务列表
     *
     * @return 数据资产-文件服务列表
     */
    List<DaAssetFilesDO> getDaAssetFilesList();

    /**
     * 获得全部数据资产-文件服务 Map
     *
     * @return 数据资产-文件服务 Map
     */
    Map<Long, DaAssetFilesDO> getDaAssetFilesMap();


    /**
     * 导入数据资产-文件服务数据
     *
     * @param importExcelList 数据资产-文件服务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetFiles(List<DaAssetFilesRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
