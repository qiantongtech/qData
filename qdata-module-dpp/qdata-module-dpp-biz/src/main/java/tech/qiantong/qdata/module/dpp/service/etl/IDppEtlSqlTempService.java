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

package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成SQL模版Service接口
 *
 * @author FXB
 * @date 2025-06-25
 */
public interface IDppEtlSqlTempService extends IService<DppEtlSqlTempDO> {

    /**
     * 获得数据集成SQL模版分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成SQL模版分页列表
     */
    PageResult<DppEtlSqlTempDO> getDppEtlSqlTempPage(DppEtlSqlTempPageReqVO pageReqVO);

    /**
     * 创建数据集成SQL模版
     *
     * @param createReqVO 数据集成SQL模版信息
     * @return 数据集成SQL模版编号
     */
    Long createDppEtlSqlTemp(DppEtlSqlTempSaveReqVO createReqVO);

    /**
     * 更新数据集成SQL模版
     *
     * @param updateReqVO 数据集成SQL模版信息
     */
    int updateDppEtlSqlTemp(DppEtlSqlTempSaveReqVO updateReqVO);

    /**
     * 删除数据集成SQL模版
     *
     * @param idList 数据集成SQL模版编号
     */
    int removeDppEtlSqlTemp(Collection<Long> idList);

    /**
     * 获得数据集成SQL模版详情
     *
     * @param id 数据集成SQL模版编号
     * @return 数据集成SQL模版
     */
    DppEtlSqlTempDO getDppEtlSqlTempById(Long id);

    /**
     * 获得全部数据集成SQL模版列表
     *
     * @return 数据集成SQL模版列表
     */
    List<DppEtlSqlTempDO> getDppEtlSqlTempList();

    /**
     * 获得全部数据集成SQL模版 Map
     *
     * @return 数据集成SQL模版 Map
     */
    Map<Long, DppEtlSqlTempDO> getDppEtlSqlTempMap();


    /**
     * 导入数据集成SQL模版数据
     *
     * @param importExcelList 数据集成SQL模版数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlSqlTemp(List<DppEtlSqlTempRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
