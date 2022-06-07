package ua.university.filters;

import org.keycloak.representations.AccessToken;
import ua.university.utils.KeycloakTokenUtil;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebFilter("/api/*")
public class KeycloakFilter implements Filter {
    private final List<String> requiredRoles = Arrays.asList("ROLE_ADMIN", "ROLE_STUDENT", "ROLE_TEACHER");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        boolean hasRequiredRole = false;

        if (request.getMethod().equals("OPTIONS")) {
//            filterChain.doFilter(servletRequest, servletResponse);
            response.setStatus(200);
        } else {
            try {
                AccessToken accessToken = KeycloakTokenUtil.getToken(request, request.getHeader("Authorization"));
                Set<String> roles = KeycloakTokenUtil.getRoles(accessToken);
                for (String item: roles) {
                    if (requiredRoles.contains(item)) {
                        hasRequiredRole = true;
                        break;
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
                response.setStatus(401);
                return;
            }

            if (hasRequiredRole) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(403);
            }
        }
    }

}
