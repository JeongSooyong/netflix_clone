package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IUserDao;
import com.soo.netflix_clone.vo.UserVo;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao dao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // 스프링 시큐리티에 등록한 BCryptPasswordEncoder 주입받기
    
    // 회원가입
    @Override
    public int insertUser(UserVo vo) {
        // 사용자가 입력한 비밀번호를 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(vo.getUserPw());
        vo.setUserPw(encodedPassword); // 암호화된 비밀번호를 UserVo에 다시 설정
        return dao.insertUser(vo); // 암호화된 비밀번호를 DB에 저장
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

    // 비밀번호 재설정
    @Override
    public int updatePw(UserVo vo) {
        String newUserPw = bCryptPasswordEncoder.encode(vo.getUserPw()); // userPw는 새 평문 비밀번호

        // 3. UserVo 객체의 userPw 필드에 해싱된 비밀번호를 설정
        vo.setUserPw(newUserPw);
        return dao.updatePw(vo);
    }

    // 개인정보 수정
    @Override
    public int updateMyinfo(String userId) {
        return dao.updateMyinfo(userId);
    }

}
