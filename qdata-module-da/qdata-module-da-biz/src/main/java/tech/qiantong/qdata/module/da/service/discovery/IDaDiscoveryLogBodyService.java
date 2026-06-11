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
