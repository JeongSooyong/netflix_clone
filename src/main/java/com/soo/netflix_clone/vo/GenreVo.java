package com.soo.netflix_clone.vo;

public class GenreVo {

    private int genreNo;
    private String genreName;

    public int getGenreNo() {
        return this.genreNo;
    }

    public void setGenreNo(int genreNo) {
        this.genreNo = genreNo;
    }

    public String getGenreName() {
        return this.genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return "{" +
            " genreNo='" + getGenreNo() + "'" +
            ", genreName='" + getGenreName() + "'" +
            "}";
    }

}
