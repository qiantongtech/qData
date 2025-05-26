package tech.qiantong.qdata.module.da.api.service.assetchild.gis;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IDaAssetGisOutService {
    void executeServiceForwarding(HttpServletResponse response, Long apiId, Map<String, Object> params);

}
