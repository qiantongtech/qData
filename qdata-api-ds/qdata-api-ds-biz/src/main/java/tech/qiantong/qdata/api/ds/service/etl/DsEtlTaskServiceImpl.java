package tech.qiantong.qdata.api.ds.service.etl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsStartTaskReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveReqDTO;
import tech.qiantong.qdata.api.ds.api.etl.DsTaskSaveRespDTO;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.common.httpClient.DsRequestUtils;
import tech.qiantong.qdata.common.httpClient.constants.QianTongDCApiType;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:ds数据集成任务相关接口实现
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-20 09:51
 **/
@Slf4j
@Service
public class DsEtlTaskServiceImpl implements IDsEtlTaskService {
    @Override
    public DsTaskSaveRespDTO createTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, Long projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.CREATE_PROCESS_DEFINITION;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCode(apiType.getUrl(), String.valueOf(projectCode)),
                apiType.getMethod(),
                JSONObject.parseObject(JSONObject.toJSONString(dsTaskSaveReqDTO)),
                DsTaskSaveRespDTO.class);
    }

    @Override
    public DsTaskSaveRespDTO updateTask(DsTaskSaveReqDTO dsTaskSaveReqDTO, String projectCode, String taskCode) {
        QianTongDCApiType apiType = QianTongDCApiType.UPDATE_PROCESS_DEFINITION;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCodeAndCode(apiType.getUrl(), String.valueOf(projectCode), taskCode),
                apiType.getMethod(),
                JSONObject.parseObject(JSONObject.toJSONString(dsTaskSaveReqDTO)),
                DsTaskSaveRespDTO.class);
    }

    @Override
    public DsStatusRespDTO releaseTask(String releaseState, String projectCode, String code) {
        QianTongDCApiType apiType = QianTongDCApiType.RELEASE_PROCESS_DEFINITION;
        Map<String, Object> params = new HashMap<>();
        params.put("releaseState", releaseState);
        return DsRequestUtils.request(DsRequestUtils.replaceProjectCodeAndCode(apiType.getUrl(), projectCode, code),
                apiType.getMethod(),
                null, params,
                DsStatusRespDTO.class);
    }

    @Override
    public DsStatusRespDTO deleteTask(String projectCode, String code) {
        QianTongDCApiType apiType = QianTongDCApiType.DELETE_PROCESS_DEFINITION;
        return DsRequestUtils.request(DsRequestUtils.replaceProjectCodeAndCode(apiType.getUrl(), projectCode, code),
                apiType.getMethod(),
                null, null,
                DsStatusRespDTO.class);
    }

    @Override
    public DsStatusRespDTO startTask(DsStartTaskReqDTO dsStartTaskReqDTO, String projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.POST_START_PROCESS;
        return DsRequestUtils.requestForm(DsRequestUtils.replaceProjectCode(apiType.getUrl(), projectCode),
                apiType.getMethod(), JSONObject.parseObject(JSONObject.toJSONString(dsStartTaskReqDTO)),
                DsStatusRespDTO.class);
    }

    @Override
    public DsTaskSaveRespDTO batchCopy(String code, String projectCode) {
        QianTongDCApiType apiType = QianTongDCApiType.BATCH_COPY_PROCESS_DEFINITION;

        // URL 拼接
        String url = DsRequestUtils.replaceProjectCode(apiType.getUrl(), projectCode);

        // 表单参数
        Map<String, Object> params = new HashMap<>();
        params.put("codes", code);
        params.put("targetProjectCode", projectCode);

        // 调用
        return DsRequestUtils.requestForm(url, apiType.getMethod(), params, DsTaskSaveRespDTO.class);
    }
}
