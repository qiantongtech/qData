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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 逻辑模型类目管理Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttModelCatService extends IService<AttModelCatDO> {

    /**
     * 获得逻辑模型类目管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 逻辑模型类目管理分页列表
     */
    PageResult<AttModelCatDO> getAttModelCatPage(AttModelCatPageReqVO pageReqVO);

    /**
     * 创建逻辑模型类目管理
     *
     * @param createReqVO 逻辑模型类目管理信息
     * @return 逻辑模型类目管理编号
     */
    Long createAttModelCat(AttModelCatSaveReqVO createReqVO);

    /**
     * 更新逻辑模型类目管理
     *
     * @param updateReqVO 逻辑模型类目管理信息
     */
    int updateAttModelCat(AttModelCatSaveReqVO updateReqVO);

    /**
     * 删除逻辑模型类目管理
     *
     * @param idList 逻辑模型类目管理编号
     */
    int removeAttModelCat(Collection<Long> idList);
    int removeAttModelCat(Long id);

    /**
     * 获得逻辑模型类目管理详情
     *
     * @param id 逻辑模型类目管理编号
     * @return 逻辑模型类目管理
     */
    AttModelCatDO getAttModelCatById(Long id);

    /**
     * 获得全部逻辑模型类目管理列表
     *
     * @return 逻辑模型类目管理列表
     */
    List<AttModelCatDO> getAttModelCatList();

    /**
     * 获得全部逻辑模型类目管理列表
     *
     * @return 逻辑模型类目管理列表
     */
    List<AttModelCatDO> getAttModelCatList(AttModelCatPageReqVO reqVO);

    /**
     * 获得全部逻辑模型类目管理 Map
     *
     * @return 逻辑模型类目管理 Map
     */
    Map<Long, AttModelCatDO> getAttModelCatMap();


    /**
     * 导入逻辑模型类目管理数据
     *
     * @param importExcelList 逻辑模型类目管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttModelCat(List<AttModelCatRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);
}
