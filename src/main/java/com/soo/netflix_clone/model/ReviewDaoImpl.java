package com.soo.netflix_clone.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.ReviewVo;

@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class ReviewDaoImpl implements IReviewDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.ReviewDaoImpl.";

    @Autowired
    public ReviewDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 영화 리뷰 조회
    @Override
    public List<ReviewVo> selectMovieReview(int reviewTarget) {
        return session.selectList(NS + "selectMovieReview", reviewTarget);
    }

    // 영화 리뷰 작성
    @Override
    public int insertMovieReview(ReviewVo vo) {
        return session.insert(NS + "insertMovieReview", vo);
    }

    // 배우 리뷰 조회
    @Override
    public List<ReviewVo> selectActorReview(int reviewTarget) {
        return session.selectList(NS + "selectActorReview", reviewTarget);
    }

    // 배우 리뷰 작성
    @Override
    public int insertActorReview(ReviewVo vo) {
        return session.insert(NS + "insertActorReview", vo);
    }
    
}
