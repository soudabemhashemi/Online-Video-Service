package com.example.springboot.imedb.jsonClass;


import java.net.Inet4Address;

public class JsonMovie {
    private String name;
    private float imdbRate;
    private String image;
    private Integer movieId;

    public JsonMovie(String name, float imdbRate, String image, Integer movieId) {
        this.name = name;
        this.imdbRate = imdbRate;
        this.image = image;
        this.movieId = movieId;
    }

    public String getName(){return name;}
    public float getImdbRate(){return imdbRate;}
    public String getImage(){return image;}
    public Integer getMovieId(){return movieId;}

}
