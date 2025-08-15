package com.soo.netflix_clone.service;

import java.util.List;

import com.soo.netflix_clone.vo.ReviewVo;

public interface IReviewService {

    // 리뷰 조회
    public List<ReviewVo> selectMovieReview(int reviewTarget);

    // 리뷰 작성
    public int insertMovieReview(ReviewVo vo);

}
