package com.soo.netflix_clone.model;

import com.soo.netflix_clone.vo.WatchHistoryVo;

public interface IWatchHistoryDao {

    // 시청 기록 저장
    public int insertWatchHistory(WatchHistoryVo vo);    

    // 가장 최근에 시청한 영상
    public WatchHistoryVo selectLatestWatchHistory(int userNo);


}
