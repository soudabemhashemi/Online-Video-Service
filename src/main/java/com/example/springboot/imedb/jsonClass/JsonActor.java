package com.example.springboot.imedb.jsonClass;


import java.net.Inet4Address;

public class JsonActor {
    String name;
    String birthDate;
    private String image;
    Integer actorId;

    public String getName() {
        return name;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getImage() {
        return image;
    }
    public Integer getActorId() {return actorId;}

    public JsonActor(String name, String birthDate, String image, Integer actorId) {
        this.name = name;
        this.birthDate = birthDate;
        this.image = image;
        this.actorId = actorId;
    }


}
