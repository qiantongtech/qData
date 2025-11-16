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

package tech.qiantong.qdata.module.att.service.cat;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;
import java.util.Map;

/**
 * 标准信息分类管理Service接口
 *
 * @author qdata
 * @date 2025-08-21
 */
public interface IAttDocumentCatService extends IService<AttDocumentCatDO> {

    /**
     * 获得标准信息分类管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标准信息分类管理分页列表
     */
    PageResult<AttDocumentCatDO> getAttDocumentCatPage(AttDocumentCatPageReqVO pageReqVO);

    /**
     * 获得全部标准信息分类管理列表
     *
     * @return 标准信息分类管理列表
     */
    List<AttDocumentCatDO> getAttDocumentCatList(AttDocumentCatPageReqVO pageReqVO);

    /**
     * 创建标准信息分类管理
     *
     * @param createReqVO 标准信息分类管理信息
     * @return 标准信息分类管理编号
     */
    Long createAttDocumentCat(AttDocumentCatSaveReqVO createReqVO);

    /**
     * 更新标准信息分类管理
     *
     * @param updateReqVO 标准信息分类管理信息
     */
    int updateAttDocumentCat(AttDocumentCatSaveReqVO updateReqVO);

    /**
     * 删除标准信息分类管理
     *
     * @param id 标准信息分类管理编号
     */
    int removeAttDocumentCat(Long id);

    /**
     * 获得标准信息分类管理详情
     *
     * @param id 标准信息分类管理编号
     * @return 标准信息分类管理
     */
    AttDocumentCatDO getAttDocumentCatById(Long id);

    /**
     * 获得全部标准信息分类管理列表
     *
     * @return 标准信息分类管理列表
     */
    List<AttDocumentCatDO> getAttDocumentCatList();

    /**
     * 获得全部标准信息分类管理 Map
     *
     * @return 标准信息分类管理 Map
     */
    Map<Long, AttDocumentCatDO> getAttDocumentCatMap();

    /**
     * 是否存在标准信息分类管理子节点
     *
     * @param id 标准信息分类管理id
     * @return 结果 true 存在 false 不存在
     */
    boolean hasChildByAttDocumentCatId(Long id);

    /**
     * 生成code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);


}
