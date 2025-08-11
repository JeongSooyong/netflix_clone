package com.soo.netflix_clone.vo;

public class LikeVo {

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

    private int userNo;
    private int movieNo;


    @Override
    public String toString() {
        return "{" +
            " userNo='" + getUserNo() + "'" +
            ", movieNo='" + getMovieNo() + "'" +
            "}";
    }

}
