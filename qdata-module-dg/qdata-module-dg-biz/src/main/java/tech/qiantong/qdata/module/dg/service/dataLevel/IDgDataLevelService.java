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

package tech.qiantong.qdata.module.dg.service.dataLevel;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
/**
 * 数据分级Service接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface IDgDataLevelService extends IService<DgDataLevelDO> {

    /**
     * 获得数据分级分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据分级分页列表
     */
    PageResult<DgDataLevelDO> getDgDataLevelPage(DgDataLevelPageReqVO pageReqVO);

    /**
     * 创建数据分级
     *
     * @param createReqVO 数据分级信息
     * @return 数据分级编号
     */
    Long createDgDataLevel(DgDataLevelSaveReqVO createReqVO);

    /**
     * 更新数据分级
     *
     * @param updateReqVO 数据分级信息
     */
    int updateDgDataLevel(DgDataLevelSaveReqVO updateReqVO);

    /**
     * 删除数据分级
     *
     * @param idList 数据分级编号
     */
    int removeDgDataLevel(Collection<Long> idList);

    /**
     * 获得数据分级详情
     *
     * @param id 数据分级编号
     * @return 数据分级
     */
    DgDataLevelDO getDgDataLevelById(Long id);

    /**
     * 获得全部数据分级列表
     *
     * @return 数据分级列表
     */
    List<DgDataLevelDO> getDgDataLevelList();

    /**
     * 获得全部数据分级 Map
     *
     * @return 数据分级 Map
     */
    Map<Long, DgDataLevelDO> getDgDataLevelMap();


    /**
     * 导入数据分级数据
     *
     * @param importExcelList 数据分级数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDataLevel(List<DgDataLevelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DgDataLevelDO> getDgDataLevelListAll(DgDataLevelPageReqVO dgDataLevel);
}
