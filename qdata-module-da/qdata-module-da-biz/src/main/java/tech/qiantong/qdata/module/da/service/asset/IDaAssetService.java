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

package tech.qiantong.qdata.module.da.service.asset;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产Service接口
 *
 * @author lhs
 * @date 2025-01-21
 */
public interface IDaAssetService extends IService<DaAssetDO> {

    /**
     * 获得数据资产分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产分页列表
     */
    PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO,String daAssetQueryType);
    List<DaAssetDO> getDaAssetList(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getTablesByDataSourceId(DaAssetPageReqVO pageReqVO);
    DaAssetDO getDaAssetByDaAssetPageReqVO(DaAssetPageReqVO pageReqVO);




    /**
     * 创建数据资产
     *
     * @param createReqVO 数据资产信息
     * @return 数据资产编号
     */
    Long createDaAsset(DaAssetSaveReqVO createReqVO);

    /**
     * 更新数据资产
     *
     * @param updateReqVO 数据资产信息
     */
    int updateDaAsset(DaAssetSaveReqVO updateReqVO);

    /**
     * 删除数据资产
     *
     * @param idList 数据资产编号
     */
    int removeDaAsset(Collection<Long> idList);
    int removeDaAsset(Long id);

    /**
     * 获得数据资产详情
     *
     * @param id 数据资产编号
     * @return 数据资产
     */
    DaAssetRespVO getDaAssetById(Long id);

    DaAssetRespVO getDaAssetByIdSimple(Long id);



    /**
     * 获得全部数据资产列表
     *
     * @return 数据资产列表
     */
    List<DaAssetDO> getDaAssetList();

    /**
     * 获得全部数据资产 Map
     *
     * @return 数据资产 Map
     */
    Map<Long, DaAssetDO> getDaAssetMap();


    /**
     * 导入数据资产数据
     *
     * @param importExcelList 数据资产数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAsset(List<DaAssetRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 数据资产预览带有脱敏规则后的数据预览
     * @param jsonObject 主键id和条件查询的内容
     * @return
     */
    Map<String,Object> getColumnData(JSONObject jsonObject);

    /**
     * 对数据资产的数据进行脱敏
     * @param id 数据资产id
     * @param data 数据资产的数据
     * @return
     */
    List<Map<String, Object>> dataMasking(Long id, List<Map<String, Object>> data);

    void insertAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO, List<DaAssetColumnSaveReqVO> columnSaveReqVOList);

    void updateAssetByDiscoveryInfo(DaAssetPageReqVO daAssetPageReqVO);

    PageResult<DaAssetDO> getDppAssetPage(DaAssetPageReqVO daAsset);

    List<DaAssetDO> getDppAssetNoPageList(DaAssetPageReqVO daAsset);

    Long createDaAssetNew(DaAssetSaveReqVO daAsset);

    int updateDaAssetNew(DaAssetSaveReqVO daAsset);

    AjaxResult startDaAssetDatasourceTask(Long id);

    List<DaAssetColumnRelRuleVO> listRelRule(Long id, String type);

    List<DaAssetColumnRelRuleVO> listRelRule(Long datasourceId, String tableName, String type);
}
