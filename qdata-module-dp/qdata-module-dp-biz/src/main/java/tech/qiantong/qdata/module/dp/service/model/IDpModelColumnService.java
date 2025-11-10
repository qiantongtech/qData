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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.dp.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 逻辑模型属性信息Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelColumnService extends IService<DpModelColumnDO> {

    /**
     * 获得逻辑模型属性信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型属性信息分页列表
     */
    PageResult<DpModelColumnDO> getDpModelColumnPage(DpModelColumnPageReqVO pageReqVO);

    /**
     * 创建逻辑模型属性信息
     *
     * @param createReqVO 逻辑模型属性信息信息
     * @return 逻辑模型属性信息编号
     */
    Long createDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * 更新逻辑模型属性信息
     *
     * @param updateReqVO 逻辑模型属性信息信息
     */
    int updateDpModelColumn(DpModelColumnSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型属性信息
     *
     * @param idList 逻辑模型属性信息编号
     */
    int removeDpModelColumn(Collection<Long> idList);

    /**
     * 批量删除逻辑模型属性信息
     *
     * @param modelIdList 逻辑模型编号
     */
    int removeDpModelColumnByModelId(Collection<Long> modelIdList);

    /**
     * 获得逻辑模型属性信息详情
     *
     * @param id 逻辑模型属性信息编号
     * @return 逻辑模型属性信息
     */
    DpModelColumnDO getDpModelColumnById(Long id);

    /**
     * 获得全部逻辑模型属性信息列表
     *
     * @return 逻辑模型属性信息列表
     */
    List<DpModelColumnDO> getDpModelColumnList();
    List<DpModelColumnDO> getDpModelColumnList(DpModelColumnSaveReqVO createReqVO);

    long countByDpModelColumn(DpModelColumnSaveReqVO createReqVO);

    /**
     * 获得全部逻辑模型属性信息 Map
     *
     * @return 逻辑模型属性信息 Map
     */
    Map<Long, DpModelColumnDO> getDpModelColumnMap();


    /**
     * 导入逻辑模型属性信息数据
     *
     * @param importExcelList 逻辑模型属性信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpModelColumn(List<DpModelColumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 批量插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    Boolean createDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);

    /**
     * 批量修改和插入逻辑模型属性信息数据
     *
     * @param dpModelColumnList 逻辑模型属性信息数据列表
     * @return 结果
     */
    Boolean updateDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList);
}
