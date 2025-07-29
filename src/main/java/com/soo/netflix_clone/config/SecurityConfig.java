package com.soo.netflix_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 스프링 시큐리티 빈으로 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().permitAll()
            ) // 개발중에는 인증없이 접근 허용. 추후 수정
            .csrf().disable() // 개발하는 동안 CSRF(Cross-Site Request Forgery) 방어 비활성
            .formLogin().disable(); // 기본 폼 로그인 기능을 사용하지 않음.
        return http.build();
    }

    @Bean // 비밀번호 해시화를 위해 빈으로 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 