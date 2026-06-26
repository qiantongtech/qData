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

package tech.qiantong.qdata.module.att.service.sourceSystem;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 来源系统Service接口
 *
 * @author qdata
 * @date 2026-04-03
 */
public interface IAttSourceSystemService extends IService<AttSourceSystemDO> {

    /**
     * 获得来源系统分页列表
     *
     * @param pageReqVO 分页请求
     * @return 来源系统分页列表
     */
    PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO);

    /**
     * 创建来源系统
     *
     * @param createReqVO 来源系统信息
     * @return 来源系统编号
     */
    Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO);

    /**
     * 更新来源系统
     *
     * @param updateReqVO 来源系统信息
     */
    int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO);

    /**
     * 删除来源系统
     *
     * @param idList 来源系统编号
     */
    int removeAttSourceSystem(Collection<Long> idList);

    /**
     * 获得来源系统详情
     *
     * @param id 来源系统编号
     * @return 来源系统
     */
    AttSourceSystemDO getAttSourceSystemById(Long id);

    /**
     * 获得全部来源系统列表
     *
     * @return 来源系统列表
     */
    List<AttSourceSystemDO> getAttSourceSystemList();

    /**
     * 获得全部来源系统列表(带状态)
     *
     * @return 来源系统列表
     */
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag);

    /**
     * 获得全部来源系统 Map
     *
     * @return 来源系统 Map
     */
    Map<Long, AttSourceSystemDO> getAttSourceSystemMap();


    /**
     * 导入来源系统数据
     *
     * @param importExcelList 来源系统数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
