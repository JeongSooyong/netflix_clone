package com.soo.netflix_clone.vo;

public class LikeVo {

    private int userNo;
    private int movieNo;
    private int actorNo;

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

    public int getActorNo() {
        return this.actorNo;
    }

    public void setActorNo(int actorNo) {
        this.actorNo = actorNo;
    }




    @Override
    public String toString() {
        return "{" +
            " userNo='" + getUserNo() + "'" +
            ", movieNo='" + getMovieNo() + "'" +
            ", actorNo='" + getActorNo() + "'" +
            "}";
    }

}
