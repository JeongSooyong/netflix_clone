package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.ILikeDao;
import com.soo.netflix_clone.vo.LikeVo;

@Service // 비즈니스 로직을 명시하는 어노테이션
public class LikeServiceImpl implements ILikeService {

    // ILikeDao 자동주입
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

    // 배우 추천
    @Override
    public int likeActor(LikeVo vo) {
        return dao.likeActor(vo);
    }

    // 배우 추천 취소
    @Override
    public int likeActorCancel(LikeVo vo) {
        return dao.likeActorCancel(vo);
    }

    // 배우 추천 개수
    @Override
    public int countLikeActor(int actorNo) {
        return dao.countLikeActor(actorNo);
    }

    // 배우 추천 여부 확인
    @Override
    public int isLikedActor(int userNo, int actorNo) {
        return dao.isLikedActor(userNo, actorNo);
    }

}
