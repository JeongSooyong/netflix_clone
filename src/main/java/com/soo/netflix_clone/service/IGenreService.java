package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.GenreVo;

public interface IGenreService {

    // 모든 영화
    public List<GenreVo> selectAllGenres();

}
