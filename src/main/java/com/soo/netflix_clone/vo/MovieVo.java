package com.soo.netflix_clone.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class MovieVo {

    private int movieNo;
    private String movieTitle;
    private String movieDescrip;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate movieReleaseDate;
    private int movieDuration;
    private int movieGenre;
    private String moviePoster;
    private String movieVideoUrl;
    private int movieViewCount;


    public int getMovieNo() {
        return this.movieNo;
    }

    public void setMovieNo(int movieNo) {
        this.movieNo = movieNo;
    }

    public String getMovieTitle() {
        return this.movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDescrip() {
        return this.movieDescrip;
    }

    public void setMovieDescrip(String movieDescrip) {
        this.movieDescrip = movieDescrip;
    }

    public LocalDate getMovieReleaseDate() {
        return this.movieReleaseDate;
    }

    public void setMovieReleaseDate(LocalDate movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public int getMovieDuration() {
        return this.movieDuration;
    }

    public void setMovieDuration(int movieDuration) {
        this.movieDuration = movieDuration;
    }

    public int getMovieGenre() {
        return this.movieGenre;
    }

    public void setMovieGenre(int movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMoviePoster() {
        return this.moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieVideoUrl() {
        return this.movieVideoUrl;
    }

    public void setMovieVideoUrl(String movieVideoUrl) {
        this.movieVideoUrl = movieVideoUrl;
    }

    public int getMovieViewCount() {
        return this.movieViewCount;
    }

    public void setMovieViewCount(int movieViewCount) {
        this.movieViewCount = movieViewCount;
    }


    @Override
    public String toString() {
        return "{" +
            " movieNo='" + getMovieNo() + "'" +
            ", movieTitle='" + getMovieTitle() + "'" +
            ", movieDescrip='" + getMovieDescrip() + "'" +
            ", movieReleaseDate='" + getMovieReleaseDate() + "'" +
            ", movieDuration='" + getMovieDuration() + "'" +
            ", movieGenre='" + getMovieGenre() + "'" +
            ", moviePoster='" + getMoviePoster() + "'" +
            ", movieVideoUrl='" + getMovieVideoUrl() + "'" +
            ", movieViewCount='" + getMovieViewCount() + "'" +
            "}";
    }


}
