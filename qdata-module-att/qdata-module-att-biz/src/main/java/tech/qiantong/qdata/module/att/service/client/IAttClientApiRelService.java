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

package tech.qiantong.qdata.module.att.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 应用API服务关联Service接口
 *
 * @author FXB
 * @date 2025-08-21
 */
public interface IAttClientApiRelService extends IService<AttClientApiRelDO> {

    /**
     * 获得应用API服务关联分页列表
     *
     * @param pageReqVO 分页请求
     * @return 应用API服务关联分页列表
     */
    PageResult<AttClientApiRelDO> getAttClientApiRelPage(AttClientApiRelPageReqVO pageReqVO);

    /**
     * 创建应用API服务关联
     *
     * @param createReqVO 应用API服务关联信息
     * @return 应用API服务关联编号
     */
    Long createAttClientApiRel(AttClientApiRelSaveReqVO createReqVO);

    /**
     * 更新应用API服务关联
     *
     * @param updateReqVO 应用API服务关联信息
     */
    int updateAttClientApiRel(AttClientApiRelSaveReqVO updateReqVO);

    /**
     * 删除应用API服务关联
     *
     * @param idList 应用API服务关联编号
     */
    int removeAttClientApiRel(Collection<Long> idList);

    /**
     * 获得应用API服务关联详情
     *
     * @param id 应用API服务关联编号
     * @return 应用API服务关联
     */
    AttClientApiRelDO getAttClientApiRelById(Long id);

    /**
     * 获得全部应用API服务关联列表
     *
     * @return 应用API服务关联列表
     */
    List<AttClientApiRelDO> getAttClientApiRelList();

    /**
     * 获得全部应用API服务关联 Map
     *
     * @return 应用API服务关联 Map
     */
    Map<Long, AttClientApiRelDO> getAttClientApiRelMap();


    /**
     * 导入应用API服务关联数据
     *
     * @param importExcelList 应用API服务关联数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttClientApiRel(List<AttClientApiRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
