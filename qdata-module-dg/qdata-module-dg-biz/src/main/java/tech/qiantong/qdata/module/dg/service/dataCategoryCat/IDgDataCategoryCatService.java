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

package tech.qiantong.qdata.module.dg.service.dataCategoryCat;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;

/**
 * 数据分类-类目Service接口
 *
 * @author FXB
 * @date 2026-04-07
 */
public interface IDgDataCategoryCatService extends IService<DgDataCategoryCatDO> {

    /**
     * 获得数据分类-类目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据分类-类目分页列表
     */
    PageResult<DgDataCategoryCatDO> getDgDataCategoryCatPage(DgDataCategoryCatPageReqVO pageReqVO);

    /**
     * 创建数据分类-类目
     *
     * @param createReqVO 数据分类-类目信息
     * @return 数据分类-类目编号
     */
    Long createDgDataCategoryCat(DgDataCategoryCatSaveReqVO createReqVO);

    /**
     * 更新数据分类-类目
     *
     * @param updateReqVO 数据分类-类目信息
     */
    int updateDgDataCategoryCat(DgDataCategoryCatSaveReqVO updateReqVO);

    /**
     * 删除数据分类-类目
     *
     * @param idList 数据分类-类目编号
     */
    int removeDgDataCategoryCat(Collection<Long> idList);

    /**
     * 获得数据分类-类目详情
     *
     * @param id 数据分类-类目编号
     * @return 数据分类-类目
     */
    DgDataCategoryCatDO getDgDataCategoryCatById(Long id);

    /**
     * 获得全部数据分类-类目列表
     *
     * @return 数据分类-类目列表
     */
    List<DgDataCategoryCatDO> getDgDataCategoryCatList();
    /**
     * 获得全部数据分类-类目 Map
     *
     * @return 数据分类-类目 Map
     */
    Map<Long, DgDataCategoryCatDO> getDgDataCategoryCatMap();


    /**
     * 导入数据分类-类目数据
     *
     * @param importExcelList 数据分类-类目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDgDataCategoryCat(List<DgDataCategoryCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * 更改指定pid下的所有code
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

}
