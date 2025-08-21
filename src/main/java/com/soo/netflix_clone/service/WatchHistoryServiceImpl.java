package com.soo.netflix_clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soo.netflix_clone.model.IWatchHistoryDao;
import com.soo.netflix_clone.vo.WatchHistoryVo;


@Service // 비즈니스 로직을 명시하는 어노테이션
public class WatchHistoryServiceImpl implements IWatchHistoryService {

    // IWatchHistoryDao 자동 주입
    @Autowired
    private IWatchHistoryDao dao;

    // 시청 기록 저장
    @Override
    public int insertWatchHistory(WatchHistoryVo vo) {
        return dao.insertWatchHistory(vo);
    }

}
