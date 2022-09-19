package com.example.springboot.imedb;

public class Watchlist {
    private int userId;
    private int movieId;

    public Watchlist(int userId, int movieId){
        this.userId = userId;
        this.movieId = movieId;
    }

    public int getUserId(){return userId;}
    public int getMovieId(){return movieId;}
}
