package com.example.springboot.imedb;

public class Cast {
    private int actorId;
    private int movieId;


    public Cast(int movieId, int actorId) {
        this.actorId = actorId;
        this.movieId = movieId;
    }

    public int getMovieId(){return movieId;}

    public int getActorId(){return actorId;}
}

