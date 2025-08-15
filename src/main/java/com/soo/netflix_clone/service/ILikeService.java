package com.soo.netflix_clone.service;

import com.soo.netflix_clone.vo.LikeVo;

public interface ILikeService {

    // 영상 추천
    public int likeMovie(LikeVo vo);

    // 영상 추천 취소
    public int likeMovieCancel(LikeVo vo);

    // 영상 추천 개수
    public int countLikeMovie(int movieNo);

    // 영상 추천 여부 확인
    public int isLikedMovie(int userNo, int movieNo);

}
