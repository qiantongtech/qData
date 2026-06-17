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

package tech.qiantong.qdata.module.dg.service.dataCategory;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategorySaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategory.vo.DgDataCategoryTreeRespVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;

/**
 * 数据分类Service接口
 *
 * @author qdata
 * @date 2026-04-07
 */
public interface IDgDataCategoryService extends IService<DgDataCategoryDO> {

    /**
     * 获得数据分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据分类分页列表
     */
    PageResult<DgDataCategoryDO> getDgDataCategoryPage(DgDataCategoryPageReqVO pageReqVO);

    /**
     * 创建数据分类
     *
     * @param createReqVO 数据分类信息
     * @return 数据分类编号
     */
    Long createDgDataCategory(DgDataCategorySaveReqVO createReqVO);

    /**
     * 更新数据分类
     *
     * @param updateReqVO 数据分类信息
     */
    int updateDgDataCategory(DgDataCategorySaveReqVO updateReqVO);

    /**
     * 删除数据分类
     *
     * @param idList 数据分类编号
     */
    int removeDgDataCategory(Collection<Long> idList);

    /**
     * 获得数据分类详情
     *
     * @param id 数据分类编号
     * @return 数据分类
     */
    DgDataCategoryDO getDgDataCategoryById(Long id);

    /**
     * 获得全部数据分类列表
     *
     * @return 数据分类列表
     */
    List<DgDataCategoryDO> getDgDataCategoryList();


    /**
     * 获得全部数据分类 Map
     *
     * @return 数据分类 Map
     */
    Map<Long, DgDataCategoryDO> getDgDataCategoryMap();


    /**
     * 导入数据分类数据
     *
     * @param importExcelList 数据分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDgDataCategory(List<DgDataCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取数据分类树列表
     *
     * @return 树列表
     */
    List<DgDataCategoryTreeRespVO> selectTree(String type);

    /**
     * 根据分类编码获取数量
     *
     * @param catCode
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * 将老的 CAT_CODE 批量更新成新的 CAT_CODE
     *
     * @param oldCatCode 旧分类编码
     * @param newCatCode 新分类编码
     * @return 受影响行数
     */
    int updateCatCode(String codeOld, String codeNew);

    List<DgDataCategoryDO> getDgDataCategoryList(DgDataCategoryPageReqVO dgDataCategory);
}
