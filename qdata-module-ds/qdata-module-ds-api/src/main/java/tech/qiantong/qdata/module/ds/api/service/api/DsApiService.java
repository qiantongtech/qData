package tech.qiantong.qdata.module.ds.api.service.api;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.module.ds.api.api.dto.DsApiReqDTO;
import tech.qiantong.qdata.module.ds.api.api.dto.DsApiRespDTO;
import tech.qiantong.qdata.module.ds.api.apiLog.dto.DsApiLogReqDTO;
import tech.qiantong.qdata.module.ds.api.apiLog.dto.DsApiLogRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DsApiService {

    Long getCountByCatCode(String catCode);

}
