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

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEvaluateLogStatisticsVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEvaluateLogDO;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 评测规则结果Service接口
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface IDppEvaluateLogService extends IService<DppEvaluateLogDO> {

    /**
     * 获得评测规则结果分页列表
     *
     * @param pageReqVO 分页请求
     * @return 评测规则结果分页列表
     */
    PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO);

    /**
     * 创建评测规则结果
     *
     * @param createReqVO 评测规则结果信息
     * @return 评测规则结果编号
     */
    Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO);

    /**
     * 更新评测规则结果
     *
     * @param updateReqVO 评测规则结果信息
     */
    int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO);

    /**
     * 删除评测规则结果
     *
     * @param idList 评测规则结果编号
     */
    int removeDppEvaluateLog(Collection<Long> idList);

    /**
     * 获得评测规则结果详情
     *
     * @param id 评测规则结果编号
     * @return 评测规则结果
     */
    DppEvaluateLogDO getDppEvaluateLogById(Long id);

    /**
     * 获得全部评测规则结果列表
     *
     * @return 评测规则结果列表
     */
    List<DppEvaluateLogDO> getDppEvaluateLogList();

    /**
     * 获得全部评测规则结果 Map
     *
     * @return 评测规则结果 Map
     */
    Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap();

    Map<String, Object> sumTotalAndProblemTotalByTaskLogId(String taskLogId);


    /**
     * 导入评测规则结果数据
     *
     * @param importExcelList 评测规则结果数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    List<DppEvaluateLogStatisticsVO> statisticsEvaluateOne(Long id);

    JSONObject statisticsEvaluateTow(Long id , Date deDate , Date oldDate , int type);

    List<DppEvaluateLogRespVO> statisticsEvaluateTable(Long id);

    JSONObject pageErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO);

    boolean updateErrorData(CheckErrorDataReqDTO checkErrorDataReqDTO);
}
