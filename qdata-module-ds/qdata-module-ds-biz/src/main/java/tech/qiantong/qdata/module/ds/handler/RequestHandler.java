package tech.qiantong.qdata.module.ds.handler;


import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.database.core.PageResult;
import tech.qiantong.qdata.common.utils.IPUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.ds.annotation.DsCheckClientToken;
import tech.qiantong.qdata.module.ds.async.AsyncTask;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.service.api.impl.ApiMappingEngine;
import tech.qiantong.qdata.module.ds.utils.DataTimeUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestHandler {

    private static final String X_ACCESS_TOKEN = "Authorization";

    private RequestInterceptor requestInterceptor;

    private ApiMappingEngine apiMappingEngine;

    private ObjectMapper objectMapper;

    @Lazy
    @Resource
    private AsyncTask asyncTask;

//    @Autowired
//    private DataApiApplyServiceFeign dataApiApplyService;
//
//    @Autowired
//    SysUserServiceFeign sysUserService;


    public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    public void setApiMappingEngine(ApiMappingEngine apiMappingEngine) {
        this.apiMappingEngine = apiMappingEngine;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @ResponseBody
    @DsCheckClientToken
    public Object invoke(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable(required = false) Map<String, Object> pathVariables,
                         @RequestParam(required = false) Map<String, Object> requestParams,
                         @RequestBody(required = false) Map<String, Object> requestBodys) {

        LocalDateTime now = LocalDateTime.now();//获取请求
        // 的时间戳// 日志记录开始时间
        //转换时间戳
        long timestamp = DataTimeUtil.timeByTimeStamp(now);

        DsApiDO api;
        Map<String, Object> params = new HashMap<>();
        if (MapUtil.isNotEmpty(pathVariables)) {
            log.info("pathVariables:{}", pathVariables.toString());
            params.putAll(pathVariables);
        }
        if (MapUtil.isNotEmpty(requestParams)) {
            log.info("requestParams:{}", requestParams.toString());
            params.putAll(requestParams);
        }
        if (MapUtil.isNotEmpty(requestBodys)) {
            log.info("requestBodys:{}", requestBodys.toString());
            params.putAll(requestBodys);
        }
        String msg = null;
        int caller_size = 0;
        Long api_id = 0L;
        String caller_url = "";
        String caller_params = "";
        String caller_ip = "";
        Long cat_id = null;
        String cat_code = "";
        try {
            api = MappingHandlerMapping.getMappingApiInfo(request);
            {//封装参数
                api_id = api.getId();
                cat_id = api.getCatId();
                cat_code = api.getCatCode();
                caller_params = JSON.toJSONString(api.getReqParams());
                caller_url = request.getRequestURI();
                caller_ip = IPUtil.getIpAddr(request);
            }

            // 序列化
            api = objectMapper.readValue(objectMapper.writeValueAsString(api), DsApiDO.class);
            // 执行前置拦截器
            requestInterceptor.preHandle(request, response, api, params);

            //创建返回值
            Object responseValuel;
            //判断是什么类型的接口请求，1-数据服务。2-模型数据服务，3-三方api服务
            String isIntegrate = api.getApiServiceType();
            if (StringUtils.equals("3", isIntegrate)) {//三方api服务
                //代码的执行
                apiMappingEngine.executeServiceForwarding(api, params, response);
                //暂时对于三方接口，并不知道具体的返回信息，所以默认使用调取量1
                caller_size = 1;
                return null;
            } else if (StringUtils.equals("4", isIntegrate)) {//文件服务
                //返回文件
                apiMappingEngine.executeFileService(api, response);
                //暂时对于三方接口，并不知道具体的返回信息，所以默认使用调取量1
                caller_size = 1;
                return null;
            } else {
                //代码的执行
                Object value = apiMappingEngine.execute(api, params);
                try {
                    if(StringUtils.isNotEmpty(api.getResDataType())){
                        if(StringUtils.equals("1", api.getResDataType())){//详情只有一条
                            caller_size = 1;
                        }else if(StringUtils.equals("2", api.getResDataType())){//列表
                            List<Map<String, Object>> list = (List<Map<String, Object>>)value;
                            caller_size = list.size();
                        }else{//分页
                            PageResult<Map<String, Object>> r = (PageResult<Map<String, Object>>)value;
                            List<Map<String, Object>> data = r.getData();
                            if(StringUtils.isNotNull(data)){
                                caller_size = data.size();
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("统计查询调用数据量异常",e);
                }
                responseValuel = value;
                // 执行后置拦截器
                requestInterceptor.postHandle(request, response, api, params, responseValuel);
                return AjaxResult.success(responseValuel);
            }
        } catch (Exception e) {
            msg = e.getMessage();
            throw e;
        } finally {
            //创建日志实体
            DsApiLogDO apiLogDto = new DsApiLogDO();
            apiLogDto.setCallerStartDate(now);
            // 计算响应时间
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - timestamp;
            //耗时
            apiLogDto.setCallerTime(responseTime);
            //信息记录
            apiLogDto.setMsg(msg);
            //处理请求是否成功
            Integer status = 1;
            if (msg != null) {
                status = 0;
            }
            apiLogDto.setStatus(status);
            //调用数据量
            apiLogDto.setCallerSize(caller_size);
            apiLogDto.setApiId(api_id);
            apiLogDto.setCallerUrl(caller_url);
            apiLogDto.setCallerParams(caller_params);
            apiLogDto.setCallerIp(caller_ip);
            apiLogDto.setCallerId("0");
            apiLogDto.setCallerBy("-");
            apiLogDto.setCatId(cat_id);
            apiLogDto.setCatCode(cat_code);
            log.info("asyncTask.doTask(apiLogDto);");
            // 异步记录api日志
            asyncTask.doTask(apiLogDto);
        }
    }
}
