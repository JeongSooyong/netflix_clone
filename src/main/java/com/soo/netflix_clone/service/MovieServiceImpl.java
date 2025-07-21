package com.soo.netflix_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IMovieDao;
import com.soo.netflix_clone.vo.MovieVo;

@Service
public class MovieServiceImpl implements IMovieService {

    @Autowired
    private IMovieDao dao;

    // 모든 영화
    @Override
    public List<MovieVo> selectAllMovies() {
        return dao.selectAllMovies();
    }

    // 영화 조회
    @Override
    public MovieVo selectMovie(MovieVo vo) {
        return dao.selectMovie(vo);
    }

}
