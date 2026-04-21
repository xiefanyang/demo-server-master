package com.hnyr.sys.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @ClassName: XssAndSqlFilter
 * @Description: xss\sql过滤器（例外的可在配置中增加路径）
 * @Author: demo
 * @CreateDate: 2023/9/22 09:44
 * @Version: 1.0
 */
@Slf4j
public class XssAndSqlFilter implements Filter {
    private String[] prefixIgnores;
    private String ignoresParam;

    @Override
    public void destroy() {
        prefixIgnores = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (canIgnore(request)) {
            chain.doFilter(request, response);
            return;
        }
        String methodGet = "GET";
        String methodPost = "POST";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;

        if (request instanceof HttpServletRequest) {
            methodGet = request.getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }

        if (methodPost.equalsIgnoreCase(methodGet)) {
            param = this.getBodyString(xssRequest.getReader());
            if (StringUtils.isNotBlank(param)) {
                if (xssRequest.checkXssAndSql(param)) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    log.error("{}", param);
                    throw new BusinessException("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!");
                }
            }
        }
        if (xssRequest.checkParameter()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            throw new BusinessException("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!");
        }
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        ignoresParam = config.getInitParameter("excludes");
        if (StringUtils.isNotEmpty(ignoresParam)) {
            prefixIgnores = ignoresParam.split(",");
        }
    }

    private boolean canIgnore(HttpServletRequest request) {
        boolean isExcludedPage = false;
        // 判断是否在过滤url之外
        for (String page : prefixIgnores) {
            if (request.getServletPath().startsWith(page)) {
                isExcludedPage = true;
                break;
            }
        }
        return isExcludedPage;
    }

    /**
     * 获取request请求body中参数
     *
     * @param br
     * @return
     */
    public String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;

    }

}