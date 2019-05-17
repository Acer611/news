package td.news.api.common.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by root on 17-3-6.
 */
@Component
@Order(1)
public class SignFilter implements Filter {

    @Autowired
    private GatewayAuthority gatewayAuthority;
    @Value("${spring.profiles.active}")
    private String profiles;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (true) {
            chain.doFilter(req, resp);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) req;
        if (!"rls".equalsIgnoreCase(profiles)) {
            chain.doFilter(req, resp);
            return;
        }
//        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (!gatewayAuthority.isTimeValid(request)) {
            response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            response.getWriter().write("oops, please check your time.");
            return;
        }
        if (!gatewayAuthority.isSignValid(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("oops, please check your sign code.");
            return;
        }
        // all is ok
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
