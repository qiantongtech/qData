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

package tech.qiantong.qdata.module.dg.service.whitelist;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
/**
 * 脱敏白名单Service接口
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface IDgDesensitizeWhitelistService extends IService<DgDesensitizeWhitelistDO> {

    /**
     * 获得脱敏白名单分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏白名单分页列表
     */
    PageResult<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistPage(DgDesensitizeWhitelistPageReqVO pageReqVO);

    /**
     * 创建脱敏白名单
     *
     * @param createReqVO 脱敏白名单信息
     * @return 脱敏白名单编号
     */
    Long createDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO createReqVO);

    /**
     * 更新脱敏白名单
     *
     * @param updateReqVO 脱敏白名单信息
     */
    int updateDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO updateReqVO);

    /**
     * 删除脱敏白名单
     *
     * @param idList 脱敏白名单编号
     */
    int removeDgDesensitizeWhitelist(Collection<Long> idList);

    /**
     * 获得脱敏白名单详情
     *
     * @param id 脱敏白名单编号
     * @return 脱敏白名单
     */
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistById(Long id);

    /**
     * 获得全部脱敏白名单列表
     *
     * @return 脱敏白名单列表
     */
    List<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistList();

    /**
     * 获得全部脱敏白名单 Map
     *
     * @return 脱敏白名单 Map
     */
    Map<Long, DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistMap();


    /**
     * 导入脱敏白名单数据
     *
     * @param importExcelList 脱敏白名单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeWhitelist(List<DgDesensitizeWhitelistRespVO> importExcelList, boolean isUpdateSupport, String operName);

    //根据分类ID查询脱敏白名单
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistByCategoryId(Long categoryId);
}
