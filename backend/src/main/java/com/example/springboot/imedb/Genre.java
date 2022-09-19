package com.example.springboot.imedb;

public class Genre {
    private String genreName;
    private int movieId;


    public Genre(int movieId, String genreName) {
        this.genreName = genreName;
        this.movieId = movieId;
    }

    public int getMovieId(){return movieId;}

    public String getName(){return genreName;}
}
