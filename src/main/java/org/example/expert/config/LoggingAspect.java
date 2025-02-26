package org.example.expert.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ObjectMapper objectMapper;

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object loggingApiRequests(ProceedingJoinPoint joinPoint) throws Throwable{
        long userId = (long) request.getAttribute("userId");
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();
        long startSecond = System.currentTimeMillis();
        LocalDateTime startTime = LocalDateTime.now();

        String requestBody;
        // Lv.4 request 의 타입이 CachedBodyHttpServletRequest 와 같다면, request 를 cachedRequest 에 할당한다.
        if (request instanceof CachedBodyHttpServletRequest cachedRequest) {
            requestBody = new String(cachedRequest.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            requestBody = "[요청 바디 캐싱 실패]";
        }
        String requestBodyJson = requestBody.isBlank() ? "없음" : requestBody;
        log.info("[API 요청 시작] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 요청 본문: {}, 요청 시각: {}", userId, httpMethod, url, requestBodyJson, startTime);


        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Exception e) {
            LocalDateTime errorTime = LocalDateTime.now();
            log.error("[API 요청 실패] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 에러: {}, 발생시간: {}", userId, httpMethod, url, e.getMessage(), errorTime);
            throw e;
        }
        
        long duration = System.currentTimeMillis() - startSecond;

        String responseBodyJson = "없음";
        if (response instanceof ContentCachingResponseWrapper cachedResponse) {
            byte[] responseBodyBytes = cachedResponse.getContentAsByteArray();
            responseBodyJson = responseBodyBytes.length > 0 ? new String(responseBodyBytes, StandardCharsets.UTF_8) : "없음";
        }

        log.info("[API 응답 완료] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 응답 본문: {}, 실행 시간: {}ms", userId, httpMethod, url, responseBodyJson, duration);

        return response;
    }
}
