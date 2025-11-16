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

package tech.qiantong.qdata.module.da.service.assetchild.api;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-外部API-参数Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetApiParamService extends IService<DaAssetApiParamDO> {

    /**
     * 获得数据资产-外部API-参数分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-外部API-参数分页列表
     */
    PageResult<DaAssetApiParamDO> getDaAssetApiParamPage(DaAssetApiParamPageReqVO pageReqVO);

    /**
     * 创建数据资产-外部API-参数
     *
     * @param createReqVO 数据资产-外部API-参数信息
     * @return 数据资产-外部API-参数编号
     */
    Long createDaAssetApiParam(DaAssetApiParamSaveReqVO createReqVO);

    void createDaAssetApiParamDeep(List<DaAssetApiParamSaveReqVO> daAssetApiParamList, Long daAssetApiId);

    /**
     * 更新数据资产-外部API-参数
     *
     * @param updateReqVO 数据资产-外部API-参数信息
     */
    int updateDaAssetApiParam(DaAssetApiParamSaveReqVO updateReqVO);

    /**
     * 删除数据资产-外部API-参数
     *
     * @param idList 数据资产-外部API-参数编号
     */
    int removeDaAssetApiParam(Collection<Long> idList);
    int removeThemeRelByAssetApiId( Long assetApiId);

    /**
     * 获得数据资产-外部API-参数详情
     *
     * @param id 数据资产-外部API-参数编号
     * @return 数据资产-外部API-参数
     */
    DaAssetApiParamDO getDaAssetApiParamById(Long id);

    /**
     * 获得全部数据资产-外部API-参数列表
     *
     * @return 数据资产-外部API-参数列表
     */
    List<DaAssetApiParamDO> getDaAssetApiParamList();
    List<DaAssetApiParamRespVO> getDaAssetApiParamList(Long daAssetApiId);

    /**
     * 获得全部数据资产-外部API-参数 Map
     *
     * @return 数据资产-外部API-参数 Map
     */
    Map<Long, DaAssetApiParamDO> getDaAssetApiParamMap();


    /**
     * 导入数据资产-外部API-参数数据
     *
     * @param importExcelList 数据资产-外部API-参数数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetApiParam(List<DaAssetApiParamRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
