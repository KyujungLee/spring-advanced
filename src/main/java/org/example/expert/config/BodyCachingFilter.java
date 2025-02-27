package org.example.expert.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class BodyCachingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest && response instanceof HttpServletResponse httpServletResponse) {
            // 요청 바디 캐싱
            // Lv 4 수정 - 스프링에서 제공하는 ContentCachingRequestWrapper 으로 변경
            ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(httpServletRequest);

            // 응답 바디 캐싱 (ContentCachingResponseWrapper 사용)
            ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(httpServletResponse);

            // 필터 체인 실행 (요청/응답 처리)
            chain.doFilter(cachedRequest, cachedResponse);

            // 응답 바디를 클라이언트에게 전달 (중요)
            cachedResponse.copyBodyToResponse();
        } else {
            chain.doFilter(request, response);
        }
    }
}

