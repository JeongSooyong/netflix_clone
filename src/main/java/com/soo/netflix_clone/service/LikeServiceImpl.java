package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.ILikeDao;
import com.soo.netflix_clone.vo.LikeVo;

@Service // 비즈니스 로직을 명시하는 어노테이션
public class LikeServiceImpl implements ILikeService {

    @Autowired
    private ILikeDao dao;

    // 영상 추천
    @Override
    public int likeMovie(LikeVo vo) {
        return dao.likeMovie(vo);
    }

    // 영상 추천 취소
    @Override
    public int likeMovieCancel(LikeVo vo) {
        return dao.likeMovieCancel(vo);
    }

    // 영상 추천 개수
    @Override
    public int countLikeMovie(int movieNo) {
        return dao.countLikeMovie(movieNo);
    }

    // 영상 추천 여부 확인
    @Override
    public int isLikedMovie(int userNo, int movieNo) {
        return dao.isLikedMovie(userNo, movieNo);
    }

}
