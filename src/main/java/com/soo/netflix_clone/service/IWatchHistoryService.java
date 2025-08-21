package com.soo.netflix_clone.service;

import com.soo.netflix_clone.vo.WatchHistoryVo;

public interface IWatchHistoryService {

    // 시청 기록 저장
    public int insertWatchHistory(WatchHistoryVo vo);

}
