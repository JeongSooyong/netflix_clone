package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IUserDao;
import com.soo.netflix_clone.vo.UserVo;


@Service // 비즈니스 로직을 명시하는 어노테이션
public class UserServiceImpl implements IUserService {

    // IUserDao 자동 주입
    @Autowired
    private IUserDao dao;

    // 스프링 시큐리티에 등록한 BCryptPasswordEncoder 자동 주입
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; 
    
    // 회원가입
    @Override
    public int insertUser(UserVo vo) {
        // BCryptPasswordEncoder로 사용자가 입력한 비밀번호를 해시화하여 변수 encodedPassword에 할당
        String encodedPassword = bCryptPasswordEncoder.encode(vo.getUserPw());
        vo.setUserPw(encodedPassword); // 해시화된 비밀번호를 UserVo에 다시 저장
        return dao.insertUser(vo); // 암호화된 비밀번호를 DB에 저장
    }

    // 개인정보 및 로그인
    @Override
    public UserVo selectUser(UserVo vo) {
        
        // dao에서 selectUser 메서드 호출
        UserVo userVo = dao.selectUser(vo);

        // 아이디가 존재하지 않는 경우
        if (userVo == null) {
            return null;
        }
        
        // 비밀번호가 입력되지 않았을 경우
        if (vo.getUserPw()!=null && !vo.getUserPw().isEmpty()) {

            // vo의 getUserPw 메서드로 평문 비밀번호와 userVo의 getUserPw 메서드로 암호화된 비밀번호를 
            // 호출하여 두 비밀번호를 비교한다.
            if (bCryptPasswordEncoder.matches(vo.getUserPw(),userVo.getUserPw())) {

                // 두 비밀번호가 일치하면 로그인
                userVo.setUserPw(null);
                return userVo;

            } else { // 비밀번호 불일치시
                return null;
            }

        } else {

            userVo.setUserPw(null);

            return userVo;

        }
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

        // newUserPw에 해시화된 userPw를 할당
        String newUserPw = bCryptPasswordEncoder.encode(vo.getUserPw()); 

        // UserVo 객체의 userPw 필드에 해싱된 비밀번호를 저장
        vo.setUserPw(newUserPw);
        
        return dao.updatePw(vo);
    }

    // 개인정보 수정
    @Override
    public int updateMyinfo(UserVo vo) {
        return dao.updateMyinfo(vo);
    }

}
