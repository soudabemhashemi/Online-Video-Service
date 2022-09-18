package com.example.springboot.controllers.models;

public class SignUp {
    String name;
    String nickname;
    String birthDate;
    String email;
    String password;

    public String getEmail() {
        return email;
    }
    public String getPassword(){return password;}
    public String getName(){return name;}
    public String getNickname(){return nickname;}
    public String getBirthDate(){return birthDate;}
}
