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
