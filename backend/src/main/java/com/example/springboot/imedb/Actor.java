package com.example.springboot.imedb;


import com.example.springboot.imedb.jsonClass.JsonMovie;

import java.sql.SQLException;
import java.util.ArrayList;

public class Actor {
    private int id;
    private String name;
    private String birthDate;
    private String nationality;
    private String image;
    ArrayList<JsonMovie> acted_in;
    private Integer movieCount;

    public Actor(int id, String name, String birthDate, String nationality, String image) throws SQLException {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.image = image;
        this.acted_in = DataBase.getInstance().get_acted_movies(this);
        this.movieCount = acted_in.size();
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getNationality() {
        return nationality;
    }
    public String getImage(){return image;}
    public ArrayList<JsonMovie> getActed_in(){return acted_in;}
    public Integer getMovieCount(){return acted_in.size();}

    public void updateActor(String name, String birthDate, String nationality, String image) throws SQLException {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.image = image;
        this.acted_in = DataBase.getInstance().get_acted_movies(this);
        this.movieCount = acted_in.size();
    }


}


