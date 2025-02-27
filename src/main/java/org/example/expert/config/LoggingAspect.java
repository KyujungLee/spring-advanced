package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    @Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
            "execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
    public Object loggingApiRequests(ProceedingJoinPoint joinPoint) throws Throwable{

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        long userId = (long) request.getAttribute("userId");
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();
        long startSecond = System.currentTimeMillis();
        LocalDateTime startTime = LocalDateTime.now();

        // Lv.4 request 의 타입이 CachedBodyHttpServletRequest 와 같다면, request 를 cachedRequest 에 할당한다.
        // Lv.4 수정 - 스프링에서 제공하는 ContentCachingRequestWrapper 사용하여 요청 바디를 캐싱.
        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        String requestBody = new String(cachingRequest.getContentAsByteArray(), request.getCharacterEncoding());
        String requestBodyJson = requestBody.isBlank() ? "없음" : requestBody;
        log.info("[API 요청 시작] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 요청 본문: {}, 요청 시각: {}",
                userId, httpMethod, url, requestBodyJson, startTime);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            LocalDateTime errorTime = LocalDateTime.now();
            log.error("[API 요청 실패] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 에러: {}, 발생시간: {}",
                    userId, httpMethod, url, e.getMessage(), errorTime);
            throw e;
        }

        long duration = System.currentTimeMillis() - startSecond;

        // Lv.4 수정 - 스프링에서 제공하는 ContentCachingResponseWrapper 사용하여 응답 바디를 캐싱.
        ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;
        String responseBody = new String(cachingResponse.getContentAsByteArray(), cachingResponse.getCharacterEncoding());
        String responseBodyJson = responseBody.isBlank() ? "없음" : responseBody;
        log.info("[API 응답 완료] 사용자 ID: {}, HTTP 메서드: {}, URL: {}, 응답 본문: {}, 실행 시간: {}ms",
                userId, httpMethod, url, responseBodyJson, duration);

        return result;
    }
}
