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

package tech.qiantong.qdata.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统配置Service接口
 *
 * @author qdata
 * @date 2024-12-31
 */
public interface ISystemContentService extends IService<SystemContentDO> {

    /**
     * 获得系统配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 系统配置分页列表
     */
    PageResult<SystemContentDO> getSystemContentPage(SystemContentPageReqVO pageReqVO);

    /**
     * 创建系统配置
     *
     * @param createReqVO 系统配置信息
     * @return 系统配置编号
     */
    Long createSystemContent(SystemContentSaveReqVO createReqVO);

    /**
     * 更新系统配置
     *
     * @param updateReqVO 系统配置信息
     */
    int updateSystemContent(SystemContentSaveReqVO updateReqVO);

    /**
     * 删除系统配置
     *
     * @param idList 系统配置编号
     */
    int removeSystemContent(Collection<Long> idList);

    /**
     * 获得系统配置详情
     *
     * @param id 系统配置编号
     * @return 系统配置
     */
    SystemContentDO getSystemContentById(Long id);

    /**
     * 获得全部系统配置列表
     *
     * @return 系统配置列表
     */
    List<SystemContentDO> getSystemContentList();

    /**
     * 获得全部系统配置 Map
     *
     * @return 系统配置 Map
     */
    Map<Long, SystemContentDO> getSystemContentMap();


    /**
     * 导入系统配置数据
     *
     * @param importExcelList 系统配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importSystemContent(List<SystemContentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
