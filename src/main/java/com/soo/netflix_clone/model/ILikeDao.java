package com.soo.netflix_clone.model;

import com.soo.netflix_clone.vo.LikeVo;

public interface ILikeDao {

    // 영상 추천
    public int likeMovie(LikeVo vo);

}
