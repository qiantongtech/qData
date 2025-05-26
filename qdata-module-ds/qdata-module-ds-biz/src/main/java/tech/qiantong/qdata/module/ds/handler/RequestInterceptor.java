package tech.qiantong.qdata.module.ds.handler;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.qiantong.qdata.common.enums.ParamType;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.IPUtil;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    private RedisTemplate<String, Object> redisTemplate;

    public RequestInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 请求之前执行
     *
     * @return 当返回对象时，直接将此对象返回到页面，返回null时，继续执行后续操作
     * @throws Exception
     */
    public void preHandle( HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params) {

//    public void preHandle(String username, HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params, DataApiApplyServiceFeign dataApiApplyService, SysUserServiceFeign sysUserService) {
        log.info("************ApiInterceptor preHandle executed**********");
        String uri = request.getRequestURI();
        log.info("getRequestURI的值：" + uri);
        String ipAddr = IPUtil.getIpAddr(request);
        log.info("ipAddr的值：" + ipAddr);

        //判断是否是管理员 IS_ADMIN  鉴权判断
//        if (sysUserService.checkAdmin(username) == 1) {
//            return;
//        }
//        R res = dataApiApplyService.getDataApiApplyByUsernameAndUrl(username, uri);
//        if (!res.isSuccess()) {
//            throw new ServiceException("服务异常，请联系管理员");
//        }
//        DsApiLogDO dataApiApplyEntity = JSONObject.parseObject(JSONObject.toJSONString(res.getData()), DataApiApplyEntity.class);
//        //判断申请是否为空或者当前登陆人不是api发布者并且未申请通过该服务
//        if(dataApiApplyEntity != null && StringUtils.equals("4",dataApiApplyEntity.getApplyStatus()) ){
//            throw new ServiceException("权限已过期，请在后台管理页面申请该接口的权限！");
//        }
//
//        if (dataApiApplyEntity == null || (!StringUtils.equals(username, dataApiApplyEntity.getCreateBy()) && !StringUtils.equals("2", dataApiApplyEntity.getApplyStatus()))) {
//            throw new ServiceException("无权限访问请在后台管理页面申请该接口的权限！");
//        }

        // 黑名单校验
        String deny = api.getDenyIp();
        if (StrUtil.isNotBlank(deny)) {
            List<String> denyList = Arrays.asList(deny.split(","));
            if (CollUtil.isNotEmpty(denyList)) {
                for (String ip : denyList) {
                    if (ip.equals(ipAddr)) {
                        throw new ServiceException(ip + "已被加入IP黑名单");
                    }
                }
            }
        }
        api.setResParamsList();
        //移除鉴权的参数
        params.remove("client_token");
        // 参数校验
        if (MapUtil.isNotEmpty(params)) {
            api.getReqParamsList().forEach(param -> {
                if (params.containsKey(param.getParamName())) {
                    // 参数类型是否正确
                    ParamType.parse(ParamType.getParamType(param.getParamType()), params.get(param.getParamName()));
                }
            });
        }

        //添加请求

        // 限流校验
//        RateLimit rateLimit = api.getRateLimit();
//        if (DataConstant.TrueOrFalse.TRUE.getKey().equals(rateLimit.getEnable())) {
//            Integer times = rateLimit.getTimes();
//            Integer seconds = rateLimit.getSeconds();
//            // 请求次数
//            times = Optional.ofNullable(times).orElse(5);
//            // 请求时间范围60秒
//            seconds = Optional.ofNullable(seconds).orElse(60);
//            // 根据 USER + API 限流
//            String key = "user:" + username + ":api:" + dataApiApplyEntity.getResourceId();
//            // 根据key获取已请求次数
//            Integer maxTimes = (Integer) redisTemplate.opsForValue().get(key);
//            if (maxTimes == null) {
//                // set时一定要加过期时间
//                redisTemplate.opsForValue().set(key, 1, seconds, TimeUnit.SECONDS);
//            } else if (maxTimes < times) {
//                redisTemplate.opsForValue().set(key, maxTimes + 1, seconds, TimeUnit.SECONDS);
//            } else {
//                throw new DataException("API调用过于频繁");
//            }
//        }
    }

    /**
     * 执行完毕之后执行
     *
     * @throws Exception
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, DsApiDO api, Map<String, Object> params, Object value) throws Exception {
    }
}
