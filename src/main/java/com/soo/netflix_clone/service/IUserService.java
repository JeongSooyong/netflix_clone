package com.soo.netflix_clone.service;


import com.soo.netflix_clone.vo.UserVo;


public interface IUserService {

    // 회원가입
    public int insertUser(UserVo vo);

    // 개인정보 및 로그인
    public UserVo selectUser(UserVo vo);

    // 아이디 존재 여부 확인
    public boolean countUserId(String userId);

    // 사용이 제한된 사용자
    public UserVo permittedUser(UserVo vo);

    // 회원탈퇴
    public int delUser(String userId);

    // 아이디 찾기
    public UserVo findId(UserVo vo);

}

