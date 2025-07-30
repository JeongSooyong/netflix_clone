package com.soo.netflix_clone.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.GenreVo;

@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class GenreDaoImpl implements IGenreDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.GenreDaoImpl.";

    @Autowired
    public GenreDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 장르 목록
    @Override
    public List<GenreVo> selectAllGenres() {
        return session.selectList(NS + "selectAllGenres");
    }

}
