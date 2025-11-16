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

package tech.qiantong.qdata.module.dpp.service.etl;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
/**
 * 数据质量日志Service接口
 *
 * @author qdata
 * @date 2025-07-19
 */
public interface IDppQualityLogService extends IService<DppQualityLogDO> {

    /**
     * 获得数据质量日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量日志分页列表
     */
    PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO);

    /**
     * 创建数据质量日志
     *
     * @param createReqVO 数据质量日志信息
     * @return 数据质量日志编号
     */
    Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO);

    /**
     * 更新数据质量日志
     *
     * @param updateReqVO 数据质量日志信息
     */
    int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO);

    /**
     * 删除数据质量日志
     *
     * @param idList 数据质量日志编号
     */
    int removeDppQualityLog(Collection<Long> idList);

    /**
     * 获得数据质量日志详情
     *
     * @param id 数据质量日志编号
     * @return 数据质量日志
     */
    DppQualityLogDO getDppQualityLogById(Long id);
    DppQualityLogDO selectPrevLogByIdWithWrapper(Long id);

    /**
     * 获得数据质量日志详情
     * 资产专用
     * @return 数据质量日志
     */
    DppQualityLogDO getDppQualityLogById(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO);

    /**
     * 获得全部数据质量日志列表
     *
     * @return 数据质量日志列表
     */
    List<DppQualityLogDO> getDppQualityLogList();

    /**
     * 获得全部数据质量日志 Map
     *
     * @return 数据质量日志 Map
     */
    Map<Long, DppQualityLogDO> getDppQualityLogMap();


    /**
     * 导入数据质量日志数据
     *
     * @param importExcelList 数据质量日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 发送数据质量日志的消息
     * @param id
     */
    void sendMessage(Long id);

}
