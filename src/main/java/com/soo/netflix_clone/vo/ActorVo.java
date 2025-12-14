package com.soo.netflix_clone.vo;

import java.time.LocalDate;

public class ActorVo {

    private int actorNo;
    private String actorName;
    private String actorPoster;
    private String actorDescrip;
    private String actorGender;
    private LocalDate actorBirth;
    private String actorNation;
    private int commonNo;



    public int getActorNo() {
        return this.actorNo;
    }

    public void setActorNo(int actorNo) {
        this.actorNo = actorNo;
    }

    public String getActorName() {
        return this.actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getActorPoster() {
        return this.actorPoster;
    }

    public void setActorPoster(String actorPoster) {
        this.actorPoster = actorPoster;
    }

    public String getActorDescrip() {
        return this.actorDescrip;
    }

    public void setActorDescrip(String actorDescrip) {
        this.actorDescrip = actorDescrip;
    }

    public String getActorGender() {
        return this.actorGender;
    }

    public void setActorGender(String actorGender) {
        this.actorGender = actorGender;
    }

    public LocalDate getActorBirth() {
        return this.actorBirth;
    }

    public void setActorBirth(LocalDate actorBirth) {
        this.actorBirth = actorBirth;
    }

    public String getActorNation() {
        return this.actorNation;
    }

    public void setActorNation(String actorNation) {
        this.actorNation = actorNation;
    }

    public int getCommonNo() {
        return this.commonNo;
    }

    public void setCommonNo(int commonNo) {
        this.commonNo = commonNo;
    }


    @Override
    public String toString() {
        return "{" +
            " actorNo='" + getActorNo() + "'" +
            ", actorName='" + getActorName() + "'" +
            ", actorPoster='" + getActorPoster() + "'" +
            ", actorDescrip='" + getActorDescrip() + "'" +
            ", actorGender='" + getActorGender() + "'" +
            ", actorBirth='" + getActorBirth() + "'" +
            ", actorNation='" + getActorNation() + "'" +
            ", commonNo='" + getCommonNo() + "'" +
            "}";
    }
    


}
