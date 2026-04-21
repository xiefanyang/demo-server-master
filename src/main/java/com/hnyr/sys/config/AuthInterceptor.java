package com.hnyr.sys.config;

import cn.hutool.json.JSONUtil;
import com.hnyr.sys.rbac.entity.vo.TokenUserVo;
import com.hnyr.sys.rbac.security.AuthPermissions;
import com.hnyr.sys.rbac.security.PermissionHandler;
import com.hnyr.sys.rbac.security.SecurityConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName: AuthInterceptor
 * @Description: 用户识别权限限定拦截器
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    PermissionHandler permissionHandler;
    /**
     * 根据配置默认启用后端验证，生产必须验证，dev 环境早期快速开发可暂不设置。交付需重点检查测试
     */
    @Value("${yr.bs.auth:true}")
    Boolean auth;

    /**
     * 请求执行前执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String requestUri = request.getRequestURI();
        if (!StringUtils.hasText(token)) {
            log.error("miss Authorization : {}", requestUri);
            failWriter(response, ErrorEnum.NO_AUTH.getCode(), ErrorEnum.NO_AUTH.getMessage());
            return false;
        }

        /**
         * 识别用户（
         */
        TokenUserVo tokenUser = permissionHandler.getTokenUser(token);
        request.setAttribute(SecurityConstant.TOKEN_USER_KEY, tokenUser);
        if (auth) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                AuthPermissions authority = method.getAnnotation(AuthPermissions.class);
                if (authority == null) {
                    // 如果注解为null, 说明不需要拦截, 直接放过
                    return true;
                }
                String[] permissions = authority.value();
                boolean hasPermission = permissionHandler.isResourceValid(tokenUser.getId(), permissions);
                if (hasPermission == false) {
                    log.info("后端权限未通过 {} {} - {} - {} ", request.getRequestURI(), tokenUser.getId(), permissions, false);
                    failWriter(response, ErrorEnum.INVALID_PERMISSION.getCode(), ErrorEnum.INVALID_PERMISSION.getMessage());
                    return false;
                }
            }
        }
        return true;

    }

    @SneakyThrows
    private void failWriter(HttpServletResponse response, int errorCode, String errorMessage) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/json;charset=utf-8");
        ResponseResult responseResult = ResponseResult.error(errorCode, errorMessage);
        response.getWriter().write(JSONUtil.toJsonStr(responseResult));
    }

    /**
     * 请求结束执行
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 视图渲染完成后执行
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
