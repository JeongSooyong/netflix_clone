package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.MovieVo;

public interface IMovieService {

    // 모든 영상
    public List<MovieVo> selectAllMovies();

    // 영상 등록
    public int insertMovie(MovieVo vo);

    // 영상 상세 조회
    public MovieVo selectMovie(String movieTitle);

}
