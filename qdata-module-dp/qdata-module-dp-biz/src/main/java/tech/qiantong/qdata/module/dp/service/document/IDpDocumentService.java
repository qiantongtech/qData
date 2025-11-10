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

package tech.qiantong.qdata.module.dp.service.document;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 标准信息登记Service接口
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface IDpDocumentService extends IService<DpDocumentDO> {

    /**
     * 获得标准信息登记分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标准信息登记分页列表
     */
    PageResult<DpDocumentDO> getDpDocumentPage(DpDocumentPageReqVO pageReqVO);

    /**
     * 获得全部标准信息登记列表
     *
     * @return 标准信息登记列表
     */
    List<DpDocumentDO> getDpDocumentList(DpDocumentPageReqVO pageReqVO);

    /**
     * 创建标准信息登记
     *
     * @param createReqVO 标准信息登记信息
     * @return 标准信息登记编号
     */
    Long createDpDocument(DpDocumentSaveReqVO createReqVO);

    /**
     * 更新标准信息登记
     *
     * @param updateReqVO 标准信息登记信息
     */
    int updateDpDocument(DpDocumentSaveReqVO updateReqVO);

    /**
     * 删除标准信息登记
     *
     * @param idList 标准信息登记编号
     */
    int removeDpDocument(Collection<Long> idList);

    /**
     * 获得标准信息登记详情
     *
     * @param id 标准信息登记编号
     * @return 标准信息登记
     */
    DpDocumentDO getDpDocumentById(Long id);

    /**
     * 获得全部标准信息登记列表
     *
     * @return 标准信息登记列表
     */
    List<DpDocumentDO> getDpDocumentList();

    /**
     * 获得全部标准信息登记 Map
     *
     * @return 标准信息登记 Map
     */
    Map<Long, DpDocumentDO> getDpDocumentMap();


    /**
     * 导入标准信息登记数据
     *
     * @param importExcelList 标准信息登记数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpDocument(List<DpDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 标准检索分页列表
     *
     * @param dpDocument
     * @return
     */
    PageResult<DpDocumentSearchRespVO> getDpDocumentSearchPage(DpDocumentSearchReqVO dpDocument);
}
