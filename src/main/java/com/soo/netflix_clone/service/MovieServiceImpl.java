package com.soo.netflix_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IMovieDao;
import com.soo.netflix_clone.vo.MovieVo;

@Service // 비즈니스 로직을 명시하는 어노테이션
public class MovieServiceImpl implements IMovieService {

    // IMovieDao 자동주입
    @Autowired
    private IMovieDao dao;

    // 모든 영상
    @Override
    public List<MovieVo> selectAllMovies() {
        return dao.selectAllMovies();
    }

    // 영상 등록
    @Override
    public int insertMovie(MovieVo vo) {
        return dao.insertMovie(vo);
    }

    // 영상 조회
    @Override
    public MovieVo selectMovie(MovieVo vo) {
        return dao.selectMovie(vo);
    }

}
