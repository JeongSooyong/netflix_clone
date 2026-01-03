package com.soo.netflix_clone.model;

import java.util.List;

import com.soo.netflix_clone.vo.ReviewVo;

public interface IReviewDao {

    // 영화 리뷰 조회
    public List<ReviewVo> selectMovieReview(int reviewTarget);

    // 영화 리뷰 작성
    public int insertMovieReview(ReviewVo vo);

    // 배우 리뷰 조회
    public List<ReviewVo> selectActorReview(int reviewTarget);

    // 배우 리뷰 작성
    public int insertActorReview(ReviewVo vo);

}
