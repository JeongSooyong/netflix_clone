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

    // 영상 상세 조회
    @Override
    public MovieVo selectMovie(String movieTitle) {
        return dao.selectMovie(movieTitle);
    }

    // 영상 정보 수정
    @Override
    public int updateMovie(MovieVo vo) {
        return dao.updateMovie(vo);
    }

    // 영상 비공개 처리
    @Override
    public int moviePrivate(String movieTitle) {
        return dao.moviePrivate(movieTitle);
    }

}
