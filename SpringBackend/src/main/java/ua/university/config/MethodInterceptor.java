package ua.university.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class MethodInterceptor implements HandlerInterceptor {

    private final String[] allowedMethods = new String[]{"POST", "GET", "OPTIONS"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Arrays.stream(allowedMethods).noneMatch(x -> x.equals(request.getMethod()))) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.setHeader("Allow", "POST, GET, OPTIONS");
            response.setContentType("message/http");
            response.getWriter().println(request.getMethod() + " method not allowed");
            response.getWriter().flush();
            return false;
        }
        return true;
    }
}