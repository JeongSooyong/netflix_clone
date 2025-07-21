package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.MovieVo;

public interface IMovieService {

    // 모든 영화
    public List<MovieVo> selectAllMovies();

    // 영화 조회
    public MovieVo selectMovie(MovieVo vo);

}
