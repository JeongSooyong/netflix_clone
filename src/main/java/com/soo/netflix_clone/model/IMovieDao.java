package com.soo.netflix_clone.model;

import java.util.List;

import com.soo.netflix_clone.vo.MovieVo;

public interface IMovieDao {

    // 모든 영상
    public List<MovieVo> selectAllMovies();

    // 영상 등록
    public int insertMovie(MovieVo vo);

    // 영화 조회
    public MovieVo selectMovie(MovieVo vo);

}
