package com.soo.netflix_clone.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.MovieVo;

@Repository
public class MovieDaoImpl implements IMovieDao {

    private final SqlSessionTemplate session;

    private final String NS = "com.soo.netflix_clone.model.MovieDaoImpl.";

    @Autowired
    public MovieDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    @Override
    // 모든 영화
    public List<MovieVo> selectAllMovies() {
        return session.selectList(NS + "selectAllMovies");
    }

    @Override
    // 영화 조회
    public MovieVo selectMovie(MovieVo vo) {
        return session.selectOne(NS + "selectMovie", vo);
    }

}
