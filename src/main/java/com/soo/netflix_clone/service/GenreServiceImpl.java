package com.soo.netflix_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IGenreDao;
import com.soo.netflix_clone.vo.GenreVo;

@Service // 비즈니스 로직을 명시하는 어노테이션
public class GenreServiceImpl implements IGenreService {

    // IGenreDao 자동 주입
    @Autowired
    private IGenreDao dao;

    // 모든 영화
    @Override
    public List<GenreVo> selectAllGenres() {
        return dao.selectAllGenres();
    }

}
