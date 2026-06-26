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

package tech.qiantong.qdata.module.da.service.discovery;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryLogBodyDO;

/**
 * 数据发现节点实例-日志Service接口
 *
 * @author qdata
 * @date 2025-10-15
 */
public interface IDaDiscoveryLogBodyService extends IService<DaDiscoveryLogBodyDO> {

    /** 取任务最新一条日志内容（按 TM 倒序） */
    String getLatestLog(Long taskId);

    /** 按复合主键获取日志内容 */
    String getLog(Long taskId);

    /** 按复合主键 (taskId, tm) 执行保存或更新 */
    int saveOrUpdateByPk(DaDiscoveryLogBodyDO entity);
    void taskLogAppend(Long taskId, String logStr);


    /**
     * 按复合主键删除日志
     *
     * @param taskId 任务ID
     * @return 是否删除成功
     */
    boolean deleteByPk(Long taskId);
}
