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

package tech.qiantong.qdata.api.ds.service.etl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsSchedulerUpdateReqDTO;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-21 10:30
 **/
@Slf4j
@Service
public class DsEtlSchedulerServiceImpl implements IDsEtlSchedulerService {
    @Override
    public DsSchedulerRespDTO saveScheduler(DsSchedulerSaveReqDTO dsSchedulerSaveReqDTO, String projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.CREATE_SCHEDULE;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCode(apiType.getUrl(), projectCode),
                apiType.getMethod(),
                JSONObject.parseObject(JSONObject.toJSONString(dsSchedulerSaveReqDTO)),
                DsSchedulerRespDTO.class);
    }

    @Override
    public DsSchedulerRespDTO updateScheduler(DsSchedulerUpdateReqDTO dsSchedulerUpdateReqDTO, String projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.UPDATE_SCHEDULE;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCodeAndId(apiType.getUrl(), projectCode, dsSchedulerUpdateReqDTO.getId()),
                apiType.getMethod(),
                JSONObject.parseObject(JSONObject.toJSONString(dsSchedulerUpdateReqDTO)),
                DsSchedulerRespDTO.class);
    }

    @Override
    public DsStatusRespDTO onlineScheduler(String projectCode, Long id) {
        QianTongDCApiType apiType = QianTongDCApiType.SCHEDULE_ONLINE;
        return DsRequestUtils.request(DsRequestUtils.replaceProjectCodeAndId(apiType.getUrl(), projectCode, id),
                apiType.getMethod(),
                null, null,
                DsStatusRespDTO.class);
    }

    @Override
    public DsStatusRespDTO offlineScheduler(String projectCode, Long id) {
        QianTongDCApiType apiType = QianTongDCApiType.SCHEDULE_OFFLINE;
        return DsRequestUtils.request(DsRequestUtils.replaceProjectCodeAndId(apiType.getUrl(), projectCode, id),
                apiType.getMethod(),
                null, null,
                DsStatusRespDTO.class);
    }

    @Override
    public DsSchedulerRespDTO getByTaskCode(String projectCode, String taskCode) {
        QianTongDCApiType apiType = QianTongDCApiType.GET_SCHEDULE_BY_PROCESS_CODE;
        return DsRequestUtils.request(DsRequestUtils.replaceProjectCodeAndCode(apiType.getUrl(), projectCode, taskCode),
                apiType.getMethod(),
                null, null,
                DsSchedulerRespDTO.class);
    }
}
