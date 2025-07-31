package com.soo.netflix_clone.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.MovieVo;

@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class MovieDaoImpl implements IMovieDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.MovieDaoImpl.";

    @Autowired
    public MovieDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 모든 영상
    @Override
    public List<MovieVo> selectAllMovies() {
        return session.selectList(NS + "selectAllMovies");
    }

    // 영상 등록
    @Override
    public int insertMovie(MovieVo vo) {
        return session.insert(NS + "insertMovie", vo);
    }


    // 영상 조회
    @Override
    public MovieVo selectMovie(MovieVo vo) {
        return session.selectOne(NS + "selectMovie", vo);
    }

}
