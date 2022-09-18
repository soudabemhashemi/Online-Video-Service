package com.example.springboot.imedb;

public class Writer {
    private String name;
    private int movieId;
    private int temp;


    public Writer(int movieId, String name) {
        this.name = name;
        this.movieId = movieId;
    }

    public int getMovieId(){return movieId;}

    public String getName(){return name;}


}
