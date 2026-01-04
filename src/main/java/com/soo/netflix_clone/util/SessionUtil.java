package com.soo.netflix_clone.util;

import com.soo.netflix_clone.vo.UserVo;

import jakarta.servlet.http.HttpSession;

/**
 * 세션 관련 유틸리티 클래스
 */
public class SessionUtil {
    
    private static final String LOGIN_USER_KEY = "loginUser";
    private static final int ADMIN_COMMON_NO = 104;
    
    /**
     * 세션에서 로그인 사용자 정보를 가져옵니다.
     * 
     * @param session HttpSession
     * @return UserVo 로그인 사용자 정보, 없으면 null
     */
    public static UserVo getLoginUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (UserVo) session.getAttribute(LOGIN_USER_KEY);
    }
    
    /**
     * 세션에 로그인 사용자 정보를 저장합니다.
     * 
     * @param session HttpSession
     * @param user UserVo 로그인 사용자 정보
     */
    public static void setLoginUser(HttpSession session, UserVo user) {
        if (session != null) {
            session.setAttribute(LOGIN_USER_KEY, user);
        }
    }
    
    /**
     * 로그인 여부를 확인합니다.
     * 
     * @param session HttpSession
     * @return boolean 로그인 여부
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getLoginUser(session) != null;
    }
    
    /**
     * 관리자 여부를 확인합니다.
     * 
     * @param session HttpSession
     * @return boolean 관리자 여부
     */
    public static boolean isAdmin(HttpSession session) {
        UserVo loginUser = getLoginUser(session);
        return loginUser != null && loginUser.getCommonNo() == ADMIN_COMMON_NO;
    }
    
    /**
     * 세션을 무효화합니다.
     * 
     * @param session HttpSession
     */
    public static void invalidateSession(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}

