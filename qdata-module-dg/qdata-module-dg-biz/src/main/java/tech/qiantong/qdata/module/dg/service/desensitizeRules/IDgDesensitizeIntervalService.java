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

package tech.qiantong.qdata.module.dg.service.desensitizeRules;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
/**
 * 脱敏区间Service接口
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface IDgDesensitizeIntervalService extends IService<DgDesensitizeIntervalDO> {

    /**
     * 获得脱敏区间分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏区间分页列表
     */
    PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO);

    /**
     * 创建脱敏区间
     *
     * @param createReqVO 脱敏区间信息
     * @return 脱敏区间编号
     */
    Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO);

    /**
     * 更新脱敏区间
     *
     * @param updateReqVO 脱敏区间信息
     */
    int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO);

    /**
     * 删除脱敏区间
     *
     * @param idList 脱敏区间编号
     */
    int removeDgDesensitizeInterval(Collection<Long> idList);

    /**
     * 获得脱敏区间详情
     *
     * @param id 脱敏区间编号
     * @return 脱敏区间
     */
    DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id);

    /**
     * 获得全部脱敏区间列表
     *
     * @return 脱敏区间列表
     */
    List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList();

    /**
     * 获得全部脱敏区间 Map
     *
     * @return 脱敏区间 Map
     */
    Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap();


    /**
     * 导入脱敏区间数据
     *
     * @param importExcelList 脱敏区间数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
