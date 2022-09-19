package com.example.springboot.services;

import com.example.springboot.controllers.models.Login;
import com.example.springboot.controllers.models.SignUp;
import com.example.springboot.controllers.models.SignUpWithGit;
import com.example.springboot.imedb.DataBase;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AuthService {
    private static DataBase dataBase = DataBase.getInstance();
    public static void authUser(Login login, HttpServletRequest request) throws Exception{
        boolean state = dataBase.loginUser(login.getEmail(), login.getPassword());
        if (state)
            System.out.println("logged in user is : " + (String) request.getAttribute("user"));
        else {
            System.out.println("Email or password is not correct.");
            throw new Exception();
        }
    }

    public static void logoutUser() throws  Exception{
        dataBase.logoutUser();
        System.out.println("User logged out successfully.");
    }

    public static void signupUser(SignUp data, HttpServletRequest request) throws Exception{
        boolean state = dataBase.addUser(data.getEmail(), data.getPassword(), data.getName(), data.getNickname(), data.getBirthDate());
        if (state) {
            dataBase.loginUser(data.getEmail(), data.getPassword());
            System.out.println("logged in user is : " + (String) request.getAttribute("user"));
        }
        else {
            System.out.println("Email already registered.");
            throw new Exception();
        }
    }

    public static void signUpWithGithub(SignUpWithGit data, HttpServletRequest request) throws Exception{
        LocalDate birthdate = LocalDate.parse(data.getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")).minusYears(18);
        boolean state = dataBase.addUser(data.getEmail(), "", "", data.getLogin(), birthdate.toString());
        dataBase.loginUser(data.getEmail(), "");
        System.out.println("logged in user is : " + (String) request.getAttribute("user"));
    }

}
