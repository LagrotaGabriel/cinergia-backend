package br.com.backend.config.security.hook.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AsaasWebhookAuthorizationFilter extends BasicAuthenticationFilter {

    public AsaasWebhookAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws IOException, ServletException {

        String asaasWebhookToken = System.getenv("TOKEN_WEBHOOK_ASAAS");

        String accessToken = httpServletRequest.getHeader("asaas-access-token");

        if ((accessToken != null) && (accessToken.equals(asaasWebhookToken)))
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        else {
            httpServletResponse.setStatus(401);
            httpServletResponse.getOutputStream().write("Acesso não autorizado: O token de acesso é inválido".getBytes());
        }
    }

}
