package lmc.stage.springprojectstage.security;

import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        // ( ex: not having a jwt token / expired token / modified token )
        response.setContentType("application/json");
        response.getOutputStream().print("{\"error\":\"Unauthorized.. Please authenticate..\"}");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}