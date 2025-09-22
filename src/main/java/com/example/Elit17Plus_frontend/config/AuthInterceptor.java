package com.example.Elit17Plus_frontend.config;

import com.example.Elit17Plus_frontend.dtos.auth.PendingRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = (String) request.getSession().getAttribute("sessionToken");
        String requestURI = request.getRequestURI();

        // Allow public access to login page, static resources, etc.
        if (requestURI.equals("/auth/login") ||
                requestURI.startsWith("/login/google") ||
                requestURI.startsWith("/process-oauth2") ||
                requestURI.startsWith("/auth/register") ||
                requestURI.equals("/") ||
                requestURI.startsWith("/products") ||
                requestURI.startsWith("/continue-action") ||
                requestURI.startsWith("/services/HAIRSTYLING") ||
                requestURI.startsWith("/services/NAIL") ||
                requestURI.startsWith("/services/MAKEUP") ||
                requestURI.startsWith("/services/LASH") ||
                requestURI.startsWith("/services/MASSAGE") ||
                requestURI.equals("/services") ||
                requestURI.startsWith("/appointments/calendar/") ||
                requestURI.startsWith("/appointments/select_worker/") ||
                requestURI.startsWith("/images/")) {
            return true;
        }

        if (token == null) {
            // Store the original request details for POST requests
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                request.getSession().setAttribute("pendingRequest", new PendingRequest(
                        requestURI,
                        request.getMethod(),
                        request.getParameterMap()
                ));

                System.out.println(request.getSession().getAttribute("pendingRequest"));

            } else {
                // For GET requests, just store the URI
                String originalUrl = requestURI;
                String queryString = request.getQueryString();
                if (queryString != null) {
                    originalUrl += "?" + queryString;
                }
                request.getSession().setAttribute("redirectAfterLogin", originalUrl);

                System.out.println(request.getSession().getAttribute("redirectAfterLogin"));
            }

            response.sendRedirect("/auth/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // No action needed here for this use case
    }
}