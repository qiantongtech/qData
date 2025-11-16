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

package tech.qiantong.qdata.module.dp.service.model;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelSaveReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelPageReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
/**
 * 逻辑模型Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelService extends IService<DpModelDO> {

    /**
     * 获得逻辑模型分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型分页列表
     */
    PageResult<DpModelDO> getDpModelPage(DpModelPageReqVO pageReqVO);

    /**
     * 创建逻辑模型
     *
     * @param createReqVO 逻辑模型信息
     * @return 逻辑模型编号
     */
    Long createDpModel(DpModelSaveReqVO createReqVO);

    /**
     * 更新逻辑模型
     *
     * @param updateReqVO 逻辑模型信息
     */
    int updateDpModel(DpModelSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型
     *
     * @param idList 逻辑模型编号
     */
    int removeDpModel(Collection<Long> idList);

    /**
     * 获得逻辑模型详情
     *
     * @param id 逻辑模型编号
     * @return 逻辑模型
     */
    DpModelDO getDpModelById(Long id);

    /**
     * 获得全部逻辑模型列表
     *
     * @return 逻辑模型列表
     */
    List<DpModelDO> getDpModelList();

    /**
     * 获得全部逻辑模型 Map
     *
     * @return 逻辑模型 Map
     */
    Map<Long, DpModelDO> getDpModelMap();


    /**
     * 导入逻辑模型数据
     *
     * @param importExcelList 逻辑模型数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpModel(List<DpModelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeDpModelAndColumnAll(List<Long> asList);

    Boolean updateStatus(Long id, Long status);
}
