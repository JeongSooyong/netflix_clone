package com.soo.netflix_clone.model;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.UserVo;


@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class UserDaoImpl implements IUserDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.UserDaoImpl.";

    @Autowired
    public UserDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 회원가입
    @Override
    public int insertUser(UserVo vo) {
        return session.insert(NS + "insertUser", vo);
    }

    // 로그인 및 개인정보 조회
    @Override
    public UserVo selectUser(UserVo vo) {
        return session.selectOne(NS + "selectUser", vo);
    }

    // 아이디 존재 여부 확인
    @Override
    public int countUserId(String userId) {
        return session.selectOne(NS + "countUserId", userId);
    }

    // 사용이 제한된 사용자
    @Override
    public UserVo permittedUser(UserVo vo) {
        return session.selectOne(NS + "permittedUser", vo);
    }

    // 회원탈퇴
    @Override
    public int delUser(String userId) {
        return session.update(NS + "delUser", userId);
    }

    // 아이디 찾기
    @Override
    public UserVo findId(UserVo vo) {
        return session.selectOne(NS + "findId", vo);
    }

    // 비밀번호 재설정
    @Override
    public int updatePw(UserVo vo) {
        return session.update(NS + "updatePw", vo);
    }

    // 개인정보 수정
    @Override
    public int updateMyinfo(UserVo vo) {
        return session.update(NS + "updateMyinfo", vo);
    }

}
