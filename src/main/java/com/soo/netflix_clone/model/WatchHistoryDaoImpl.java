package com.soo.netflix_clone.model;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soo.netflix_clone.vo.WatchHistoryVo;

@Repository // DB에 접근하는 것을 명시하는 어노테이션
public class WatchHistoryDaoImpl implements IWatchHistoryDao {

    // MyBatis와 Spring이 통합될때 사용되는 객체
    private final SqlSessionTemplate session;

    // 매퍼(Mapper).xml 파일의 네임스페이스를 저장하는 문자열 NS
    private final String NS = "com.soo.netflix_clone.model.WatchHistoryDaoImpl.";

    @Autowired
    public WatchHistoryDaoImpl(SqlSessionTemplate session) {
        this.session = session;
    }

    // 시청 기록 저장
    @Override
    public int insertWatchHistory(WatchHistoryVo vo) {
        return session.insert(NS + "insertWatchHistory", vo);
    }

    // 가장 최근에 시청한 영상
    @Override
    public WatchHistoryVo selectLatestWatchHistory(int userNo) {
        return session.selectOne(NS + "selectLatestWatchHistory", userNo);
    }

}
