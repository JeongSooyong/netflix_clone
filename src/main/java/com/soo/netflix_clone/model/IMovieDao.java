package com.soo.netflix_clone.model;

import java.util.List;

import com.soo.netflix_clone.vo.MovieVo;

public interface IMovieDao {

    // 모든 영화
    public List<MovieVo> selectAllMovies();

    // 영화 조회
    public MovieVo selectMovie(MovieVo vo);

}
