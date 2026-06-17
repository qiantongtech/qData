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
