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

package tech.qiantong.qdata.module.dm.service.businessCategory;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 业务分类Service接口
 *
 * @author qdata
 * @date 2026-04-08
 */
public interface IDmBusinessCategoryService extends IService<DmBusinessCategoryDO> {

    /**
     * 获得业务分类分页列表
     *
     * @param pageReqVO 分页请求
     * @return 业务分类分页列表
     */
    PageResult<DmBusinessCategoryDO> getDmBusinessCategoryPage(DmBusinessCategoryPageReqVO pageReqVO);

    /**
     * 创建业务分类
     *
     * @param createReqVO 业务分类信息
     * @return 业务分类编号
     */
    Long createDmBusinessCategory(DmBusinessCategorySaveReqVO createReqVO);

    /**
     * 更新业务分类
     *
     * @param updateReqVO 业务分类信息
     */
    int updateDmBusinessCategory(DmBusinessCategorySaveReqVO updateReqVO);

    /**
     * 删除业务分类
     *
     * @param idList 业务分类编号
     */
    int removeDmBusinessCategory(Collection<Long> idList);

    /**
     * 获得业务分类详情
     *
     * @param id 业务分类编号
     * @return 业务分类
     */
    DmBusinessCategoryDO getDmBusinessCategoryById(Long id);

    /**
     * 获得全部业务分类列表
     *
     * @return 业务分类列表
     */
    List<DmBusinessCategoryDO> getDmBusinessCategoryList(DmBusinessCategoryPageReqVO dmBusinessCategory);

    /**
     * 获得全部业务分类 Map
     *
     * @return 业务分类 Map
     */
    Map<Long, DmBusinessCategoryDO> getDmBusinessCategoryMap();


    /**
     * 导入业务分类数据
     *
     * @param importExcelList 业务分类数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDmBusinessCategory(List<DmBusinessCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * 更改指定pid下的所有code
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);
}
