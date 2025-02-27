package org.example.expert.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴을 지정합니다.

        return registrationBean;
    }

    /**
     * Lv.4 캐싱 필터를 추가 (admin URI 만 필터링하여 자원낭비 방지)
     */
    @Bean
    public FilterRegistrationBean<BodyCachingFilter> requestBodyCachingFilter() {
        FilterRegistrationBean<BodyCachingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new BodyCachingFilter());
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/admin/*"); // Admin 요청에서만 RequestBody 캐싱

        return registrationBean;
    }
}
