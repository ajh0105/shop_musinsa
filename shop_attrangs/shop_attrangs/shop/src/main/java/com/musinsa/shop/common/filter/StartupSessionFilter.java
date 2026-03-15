package com.musinsa.shop.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 백엔드 재시작 시 이전 세션을 즉시 무효화하는 필터.
 * DevTools 소프트 재시작 포함, 모든 기동 시 새 startupId를 생성하여
 * 기존 세션과 불일치하면 강제 로그아웃 처리한다.
 */
public class StartupSessionFilter extends OncePerRequestFilter {

    private static final String SESSION_STARTUP_KEY = "_startup_id";
    private final String startupId = UUID.randomUUID().toString();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 기존 세션의 startupId가 현재 기동 ID와 다르면 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            String savedId = (String) session.getAttribute(SESSION_STARTUP_KEY);
            if (!startupId.equals(savedId)) {
                session.invalidate();
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);

        // 로그인 후 새로 생성된 세션에 현재 startupId 기록
        HttpSession newSession = request.getSession(false);
        if (newSession != null && newSession.getAttribute(SESSION_STARTUP_KEY) == null) {
            newSession.setAttribute(SESSION_STARTUP_KEY, startupId);
        }
    }
}
