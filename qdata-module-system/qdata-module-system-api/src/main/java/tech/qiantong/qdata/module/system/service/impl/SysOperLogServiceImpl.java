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

package tech.qiantong.qdata.module.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.module.system.domain.SysOperLog;
import tech.qiantong.qdata.module.system.mapper.SysOperLogMapper;
import tech.qiantong.qdata.module.system.service.ISysOperLogService;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author qdata
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}
