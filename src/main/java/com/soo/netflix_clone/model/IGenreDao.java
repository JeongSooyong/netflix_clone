package com.soo.netflix_clone.model;

import java.util.List;

import com.soo.netflix_clone.vo.GenreVo;

public interface IGenreDao {

    // 장르 목록
    public List<GenreVo> selectAllGenres();

}
