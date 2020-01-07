package lmc.stage.springprojectstage.security;

import lmc.stage.springprojectstage.security.errorHandler.CustomRestExceptionHandler;
import lmc.stage.springprojectstage.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
@Order(1)
@CrossOrigin(origins = "http://localhost:4200")
public class BlackListFilter implements Filter {
    // Added a filterChain to check black listed tokens
    // You can check the token if its black listed in the JwtAuthorizationTokenFilter

    private CustomRestExceptionHandler customRestExceptionHandler;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("Inside BlackListFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest; // casting ServletRequest to HttpRequest

        logger.info("Authorization header in blacklist: " + httpRequest.getHeader("Authorization"));

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            String authToken = authHeader.substring(7);
            if (!redisUtil.sismember(authToken, "blacklisted"))
                filterChain.doFilter(servletRequest, servletResponse);
            else{
                logger.info("Blocked blacklisted token: " + httpRequest.getHeader("Authorization"));
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Your token has expired, try to relog.");
                return;
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
