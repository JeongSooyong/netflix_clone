package com.soo.netflix_clone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IReviewDao;
import com.soo.netflix_clone.vo.ReviewVo;

@Service // 비즈니스 로직을 명시하는 어노테이션
public class ReviewServiceImpl implements IReviewService {

    // IReviewDao 자동주입
    @Autowired
    private IReviewDao dao;
    
    // 리뷰 조회
    @Override
    public List<ReviewVo> selectMovieReview(int reviewTarget) {
        return dao.selectMovieReview(reviewTarget);
    }

    // 리뷰 작성
    public int insertMovieReview(ReviewVo vo) {
        return dao.insertMovieReview(vo);
    }

}
