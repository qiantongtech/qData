package tech.qiantong.qdata.module.ds.annotation.handler;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.data.model.ClientTokenModel;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tech.qiantong.qdata.module.ds.annotation.DsCheckClientToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查 ClientToken 拦截器
 * @author Ming
 */
@Component
public class DsCheckClientTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            DsCheckClientToken annotation = handlerMethod.getMethodAnnotation(DsCheckClientToken.class);
            if (annotation != null) {
                // 执行检查逻辑
                ClientTokenModel clientTokenModel = SaOAuth2Util.checkClientToken(SaHolder.getRequest().getParam(SaOAuth2Consts.Param.client_token));
                return clientTokenModel != null;
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在业务处理器处理请求并渲染视图后调用
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在整个请求完全结束后调用，即在视图渲染完成后
    }
}
