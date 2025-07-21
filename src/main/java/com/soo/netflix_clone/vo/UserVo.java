package com.soo.netflix_clone.vo;

import java.time.LocalDateTime;


public class UserVo {

    private int userNo;
    private String userId;
    private String userPw;
    private String userEmail;
    private LocalDateTime userSignupDate;
    private LocalDateTime userDelDate;
    private int commonNo;

    public int getCommonNo() {
        return this.commonNo;
    }

    public void setCommonNo(int commonNo) {
        this.commonNo = commonNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getUserSignupDate() {
        return userSignupDate;
    }

    public void setUserSignupDate(LocalDateTime userSignupDate) {
        this.userSignupDate = userSignupDate;
    }

    public LocalDateTime getUserDelDate() {
        return userDelDate;
    }

    public void setUserDelDate(LocalDateTime userDelDate) {
        this.userDelDate = userDelDate;
    }

    @Override
    public String toString() {
        return "UserVo{"
                + "userNo=" + userNo
                + ", userId='" + userId + '\''
                + ", userPw='" + userPw + '\''
                + ", userEmail='" + userEmail + '\''
                + ", userSignupDate=" + userSignupDate
                + ", userDelDate=" + userDelDate
                + '}';
    }

}
