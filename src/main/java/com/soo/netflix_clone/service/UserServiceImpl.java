package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IUserDao;
import com.soo.netflix_clone.vo.UserVo;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao dao;
    
    // 회원가입
    @Override
    public int insertUser(UserVo vo) {
        return dao.insertUser(vo);
    }

    // 개인정보 및 로그인
    @Override
    public UserVo selectUser(UserVo vo) {
        return dao.selectUser(vo);
    }

    // 아이디 존재 여부 확인
    @Override
    public boolean countUserId(String userId) {
        return dao.countUserId(userId) > 0;
    }

    // 사용이 제한된 사용자
    @Override
    public UserVo permittedUser(UserVo vo) {
        return dao.permittedUser(vo);
    } 

    // 회원탈퇴
    @Override
    public int delUser(String userId) {
        return dao.delUser(userId);
    }

    // 아이디 찾기
    @Override
    public UserVo findId(UserVo vo) {
        return dao.findId(vo);
    }

    // 비밀번호 찾기
    @Override
    public UserVo findPw(UserVo vo) {
        return dao.findPw(vo);
    }
}
