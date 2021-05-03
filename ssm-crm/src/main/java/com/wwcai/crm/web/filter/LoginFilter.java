package com.wwcai.crm.web.filter;

import com.wwcai.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        System.out.println("进入验证有没有登录的过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();
        // 不应被拦截的资源，自动放行
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {
            chain.doFilter(req, resp);

        } else {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            // user 不为null 说明登录了
            if (user != null) {
                chain.doFilter(req, resp);
            } else {
//            response.sendRedirect("/crm/login.jsp");
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }

}
