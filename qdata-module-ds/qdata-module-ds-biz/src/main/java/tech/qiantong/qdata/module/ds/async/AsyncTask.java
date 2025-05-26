package tech.qiantong.qdata.module.ds.async;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.handler.MappingHandlerMapping;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;
import tech.qiantong.qdata.module.ds.service.apiLog.IDsApiLogService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 异步保存API服务日志
 */
@Slf4j
@Component
public class AsyncTask {

    @Autowired
    private IDsApiLogService apiLogService;


    @Autowired
    private MappingHandlerMapping mappingHandlerMapping;

    @Lazy
    @Resource
    private IDsApiService iDsApiService;

    private static String HANDLER_RELEASE = "1";
    private static String HANDLER_CANCEL = "2";

    /**
     * 异步保存日志
     * @param apiLogDto
     */
    @Async("threadPoolTaskExecutor")
    public void doTask(DsApiLogDO apiLogDto) {
        apiLogService.save(apiLogDto);
    }


    @Async("threadPoolTaskExecutor")
    public void releaseOrCancelDataApi(Map<String, Object> map) {
        try {
            String id =(String) map.get("id");
            String type = (String) map.get("type");//0:取消 1:发布
            DsApiDO dsApiById = iDsApiService.getDsApiById(Long.valueOf(id));
            if (dsApiById != null) {
                if (HANDLER_RELEASE.equals(type)) {
                    mappingHandlerMapping.registerMapping(dsApiById);
                } else if (HANDLER_CANCEL.equals(type)) {
                    mappingHandlerMapping.unregisterMapping(dsApiById);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
