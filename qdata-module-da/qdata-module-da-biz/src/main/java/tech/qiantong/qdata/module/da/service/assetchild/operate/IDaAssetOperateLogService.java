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

package tech.qiantong.qdata.module.da.service.assetchild.operate;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据资产操作记录Service接口
 *
 * @author qdata
 * @date 2025-05-09
 */
public interface IDaAssetOperateLogService extends IService<DaAssetOperateLogDO> {

    /**
     * 获得数据资产操作记录分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据资产操作记录分页列表
     */
    PageResult<DaAssetOperateLogDO> getDaAssetOperateLogPage(DaAssetOperateLogPageReqVO pageReqVO);

    /**
     * 创建数据资产操作记录
     *
     * @param createReqVO 数据资产操作记录信息
     * @return 数据资产操作记录编号
     */
    Long createDaAssetOperateLog(DaAssetOperateLogSaveReqVO createReqVO);

    /**
     * 更新数据资产操作记录
     *
     * @param updateReqVO 数据资产操作记录信息
     */
    int updateDaAssetOperateLog(DaAssetOperateLogSaveReqVO updateReqVO);

    /**
     * 删除数据资产操作记录
     *
     * @param idList 数据资产操作记录编号
     */
    int removeDaAssetOperateLog(Collection<Long> idList);

    /**
     * 获得数据资产操作记录详情
     *
     * @param id 数据资产操作记录编号
     * @return 数据资产操作记录
     */
    DaAssetOperateLogDO getDaAssetOperateLogById(Long id);

    /**
     * 获得全部数据资产操作记录列表
     *
     * @return 数据资产操作记录列表
     */
    List<DaAssetOperateLogDO> getDaAssetOperateLogList();

    /**
     * 获得全部数据资产操作记录 Map
     *
     * @return 数据资产操作记录 Map
     */
    Map<Long, DaAssetOperateLogDO> getDaAssetOperateLogMap();


    /**
     * 导入数据资产操作记录数据
     *
     * @param importExcelList 数据资产操作记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDaAssetOperateLog(List<DaAssetOperateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void rollBack(Long id);

    PageResult<DaAssetOperateLogDO> queryDaAssetOperateLogPage(DaAssetOperateLogPageReqVO daAssetOperateLog);
}
