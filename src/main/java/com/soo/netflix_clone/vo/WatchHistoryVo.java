package com.soo.netflix_clone.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WatchHistoryVo {

    private int watchHistoryNo;
    private int userNo;
    private int movieNo;
    private LocalDate watchHistoryDate;


    public int getWatchHistoryNo() {
        return this.watchHistoryNo;
    }

    public void setWatchHistoryNo(int watchHistoryNo) {
        this.watchHistoryNo = watchHistoryNo;
    }

    public int getUserNo() {
        return this.userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getMovieNo() {
        return this.movieNo;
    }

    public void setMovieNo(int movieNo) {
        this.movieNo = movieNo;
    }

    public LocalDate getWatchHistoryDate() {
        return this.watchHistoryDate;
    }

    public void setWatchHistoryDate(LocalDate watchHistoryDate) {
        this.watchHistoryDate = watchHistoryDate;
    }


    @Override
    public String toString() {
        return "{" +
            " watchHistoryNo='" + getWatchHistoryNo() + "'" +
            ", userNo='" + getUserNo() + "'" +
            ", movieNo='" + getMovieNo() + "'" +
            ", watchHistoryDate='" + getWatchHistoryDate() + "'" +
            "}";
    }


}
