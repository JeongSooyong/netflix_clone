package com.soo.netflix_clone.service;

import com.soo.netflix_clone.vo.LikeVo;

public interface ILikeService {

    // 영상 추천
    public int likeMovie(LikeVo vo);
}
