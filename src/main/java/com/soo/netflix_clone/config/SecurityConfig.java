package com.soo.netflix_clone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 세션 인증 필터 빈 등록
    @Bean
    public SessionAuthenticationFilter sessionAuthenticationFilter() {
        return new SessionAuthenticationFilter();
    }

    // 스프링 시큐리티 빈으로 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 세션 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
            .addFilterBefore(sessionAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests((authz) -> authz
                // 정적 리소스는 모두 허용
                .requestMatchers(AntPathRequestMatcher.antMatcher("/css/**"),
                    AntPathRequestMatcher.antMatcher("/js/**"),
                    AntPathRequestMatcher.antMatcher("/img/**"),
                    AntPathRequestMatcher.antMatcher("/favicon.ico")).permitAll()
                // 로그인, 회원가입, 아이디 찾기 등은 모두 허용
                .requestMatchers(AntPathRequestMatcher.antMatcher("/"),
                    AntPathRequestMatcher.antMatcher("/login"),
                    AntPathRequestMatcher.antMatcher("/signup"),
                    AntPathRequestMatcher.antMatcher("/signup2"),
                    AntPathRequestMatcher.antMatcher("/findId"),
                    AntPathRequestMatcher.antMatcher("/findId2"),
                    AntPathRequestMatcher.antMatcher("/findPw"),
                    AntPathRequestMatcher.antMatcher("/findPw2"),
                    AntPathRequestMatcher.antMatcher("/checkUserId"),
                    AntPathRequestMatcher.antMatcher("/checkUserEmail")).permitAll()
                // 관리자 전용 페이지 (commonNo == 104)
                .requestMatchers(AntPathRequestMatcher.antMatcher("/insertMovie"),
                    AntPathRequestMatcher.antMatcher("/insertMovie2"),
                    AntPathRequestMatcher.antMatcher("/insertActor"),
                    AntPathRequestMatcher.antMatcher("/insertActor2"),
                    AntPathRequestMatcher.antMatcher("/updateActor/**"),
                    AntPathRequestMatcher.antMatcher("/updateActor2"),
                    AntPathRequestMatcher.antMatcher("/moviePrivate"),
                    AntPathRequestMatcher.antMatcher("/actorPrivate/**")).hasRole("ADMIN")
                // 인증이 필요한 API 엔드포인트 (좋아요, 리뷰 작성 등)
                .requestMatchers(AntPathRequestMatcher.antMatcher("/likeMovie"),
                    AntPathRequestMatcher.antMatcher("/likeMovieCancel"),
                    AntPathRequestMatcher.antMatcher("/likeActor"),
                    AntPathRequestMatcher.antMatcher("/likeActorCancel"),
                    AntPathRequestMatcher.antMatcher("/insertMovieReview"),
                    AntPathRequestMatcher.antMatcher("/insertActorReview"),
                    AntPathRequestMatcher.antMatcher("/actor/**/review/**")).authenticated()
                // 일반 사용자 페이지는 인증 필요
                .requestMatchers(AntPathRequestMatcher.antMatcher("/main"),
                    AntPathRequestMatcher.antMatcher("/myinfo"),
                    AntPathRequestMatcher.antMatcher("/updateMyinfo"),
                    AntPathRequestMatcher.antMatcher("/updateMyinfo2"),
                    AntPathRequestMatcher.antMatcher("/delUser"),
                    AntPathRequestMatcher.antMatcher("/logout")).authenticated()
                // 나머지는 모두 허용 (조회 페이지 등)
                .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.disable()) // 개발하는 동안 CSRF(Cross-Site Request Forgery) 방어 비활성
            .formLogin(formLogin -> formLogin.disable()); // 기본 폼 로그인 기능을 사용하지 않음.
        return http.build();
    }

    @Bean // 비밀번호 해시화를 위해 빈으로 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 