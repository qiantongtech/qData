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

package tech.qiantong.qdata.module.da.service.assetchild.geo;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产-矢量Service接口
 *
 * @author qdata
 * @date 2025-04-14
 */
public interface IDaAssetGeoService extends IService<DaAssetGeoDO> {

    /**
     * 获得数据资产-矢量分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产-矢量分页列表
     */
    PageResult<DaAssetGeoDO> getDaAssetGeoPage(DaAssetGeoPageReqVO pageReqVO);

    DaAssetGeoRespVO getDaAssetGeoByAssetId(Long assetId);

    /**
     * 创建数据资产-矢量
     *
     * @param createReqVO 数据资产-矢量信息
     * @return 数据资产-矢量编号
     */
    Long createDaAssetGeo(DaAssetGeoSaveReqVO createReqVO);

    /**
     * 更新数据资产-矢量
     *
     * @param updateReqVO 数据资产-矢量信息
     */
    int updateDaAssetGeo(DaAssetGeoSaveReqVO updateReqVO);

    /**
     * 删除数据资产-矢量
     *
     * @param idList 数据资产-矢量编号
     */
    int removeDaAssetGeo(Collection<Long> idList);

    /**
     * 获得数据资产-矢量详情
     *
     * @param id 数据资产-矢量编号
     * @return 数据资产-矢量
     */
    DaAssetGeoDO getDaAssetGeoById(Long id);

    /**
     * 获得全部数据资产-矢量列表
     *
     * @return 数据资产-矢量列表
     */
    List<DaAssetGeoDO> getDaAssetGeoList();

    /**
     * 获得全部数据资产-矢量 Map
     *
     * @return 数据资产-矢量 Map
     */
    Map<Long, DaAssetGeoDO> getDaAssetGeoMap();


    /**
     * 导入数据资产-矢量数据
     *
     * @param importExcelList 数据资产-矢量数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetGeo(List<DaAssetGeoRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
