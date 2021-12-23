package spring.security.security;

import spring.security.utils.RedisUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
@CrossOrigin(origins = "http://localhost:4200")
public class BlackListFilter implements Filter {
    // Added a filterChain to check black listed tokens
    // You can check the token if its black listed in the JwtAuthorizationTokenFilter

    @Value("${jwt.header}")
    private String authorizationHeader;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisUtil redisUtil;

    @Autowired
    public BlackListFilter(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        logger.info("Inside BlackListFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest; // casting ServletRequest to HttpRequest

        logger.warn(String.format("Authorization header in blacklist: %s", httpRequest.getHeader(authorizationHeader)));

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            String authToken = authHeader.substring(7);
            if (redisUtil.sismember(authToken, "blacklisted")) {
                logger.warn(String.format("Blocked user with token: %s", httpRequest.getHeader(authorizationHeader)));
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        // Empty
    }
}
