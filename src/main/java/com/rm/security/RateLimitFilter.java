package com.rm.security;

import com.rm.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/auth/login")
                && request.getMethod().equalsIgnoreCase("POST")) {

            String ip = request.getRemoteAddr();

            String key = "login:" + ip;

            if (!rateLimitService.isAllowed(key)) {

                response.setStatus(429);

                response.getWriter().write(
                        "Too Many Requests");

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}