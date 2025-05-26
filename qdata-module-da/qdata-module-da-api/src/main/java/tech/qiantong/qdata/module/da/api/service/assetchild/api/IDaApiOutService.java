package tech.qiantong.qdata.module.da.api.service.assetchild.api;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface IDaApiOutService {

    void executeServiceForwarding(HttpServletResponse response, Long apiId, Map<String, Object> params);
}
