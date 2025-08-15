package com.soo.netflix_clone.vo;

import java.time.LocalDate;

public class ReviewVo {

    private int reviewNo;
    private int userNo;
    private int reviewTarget;
    private LocalDate reviewDate;
    private String reviewComment;
    private int reviewRating;
    private int commonNo;
    private String userId;


    public int getReviewNo() {
        return this.reviewNo;
    }

    public void setReviewNo(int reviewNo) {
        this.reviewNo = reviewNo;
    }

    public int getUserNo() {
        return this.userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getReviewTarget() {
        return this.reviewTarget;
    }

    public void setReviewTarget(int reviewTarget) {
        this.reviewTarget = reviewTarget;
    }

    public LocalDate getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewComment() {
        return this.reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public int getReviewRating() {
        return this.reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public int getCommonNo() {
        return this.commonNo;
    }

    public void setCommonNo(int commonNo) {
        this.commonNo = commonNo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    @Override
    public String toString() {
        return "{" +
            " reviewNo='" + getReviewNo() + "'" +
            ", userNo='" + getUserNo() + "'" +
            ", reviewTarget='" + getReviewTarget() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", reviewComment='" + getReviewComment() + "'" +
            ", reviewRating='" + getReviewRating() + "'" +
            ", commonNo='" + getCommonNo() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }


}
