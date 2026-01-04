package com.soo.netflix_clone.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.soo.netflix_clone.vo.UserVo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            UserVo loginUser = (UserVo) session.getAttribute("loginUser");
            
            if (loginUser != null) {
                // 세션에 로그인 사용자가 있으면 Spring Security 인증 객체 생성
                String role = loginUser.getCommonNo() == 104 ? "ROLE_ADMIN" : "ROLE_USER";
                
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    loginUser.getUserId(),
                    null,
                    java.util.Collections.singletonList(new SimpleGrantedAuthority(role))
                );
                
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}

