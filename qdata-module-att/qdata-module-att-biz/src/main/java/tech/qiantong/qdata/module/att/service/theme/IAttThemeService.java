/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.att.service.theme;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 主题Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttThemeService extends IService<AttThemeDO> {

    /**
     * 获得主题分页列表
     *
     * @param pageReqVO 分页请求
     * @return 主题分页列表
     */
    PageResult<AttThemeDO> getAttThemePage(AttThemePageReqVO pageReqVO);

    /**
     * 创建主题
     *
     * @param createReqVO 主题信息
     * @return 主题编号
     */
    Long createAttTheme(AttThemeSaveReqVO createReqVO);

    /**
     * 更新主题
     *
     * @param updateReqVO 主题信息
     */
    int updateAttTheme(AttThemeSaveReqVO updateReqVO);

    /**
     * 删除主题
     *
     * @param idList 主题编号
     */
    int removeAttTheme(Collection<Long> idList);

    /**
     * 获得主题详情
     *
     * @param id 主题编号
     * @return 主题
     */
    AttThemeDO getAttThemeById(Long id);

    /**
     * 获得全部主题列表
     *
     * @return 主题列表
     */
    List<AttThemeDO> getAttThemeList();

    /**
     * 获得全部主题 Map
     *
     * @return 主题 Map
     */
    Map<Long, AttThemeDO> getAttThemeMap();


    /**
     * 导入主题数据
     *
     * @param importExcelList 主题数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTheme(List<AttThemeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<AttThemeDO> getAttThemeListByReqVO(AttThemePageReqVO attTheme);
}
