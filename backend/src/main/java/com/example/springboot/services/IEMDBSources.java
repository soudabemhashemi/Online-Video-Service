package com.example.springboot.services;


import com.example.springboot.HttpRequestHandler.HTTPRequestHandler;
import com.example.springboot.imedb.*;

import java.util.List;

import com.example.springboot.repository.IemdbRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class IEMDBSources {
    private static final String API_URL = "http://138.197.181.131:5000";

    private static final String ACTORS = "/api/v2/actors";
    private static final String COMMENTS = "/api/comments";
    private static final String MOVIES = "/api/v2/movies";
    private static final String USERS = "/api/users";
    private static IEMDBSources instance;

    public static IEMDBSources getInstance() {
        if(instance == null)
            instance = new IEMDBSources();
        return instance;
    }

    public void importDataFromWeb() throws Exception{
        try {
            System.out.println("importing info..");
//            getActors();
//            getMovies();
            getUsers();
//            getComments();
//            dataBase.setMoviesActors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getActors() throws Exception {
        String ActorsJsonString = HTTPRequestHandler.getRequest(API_URL + ACTORS);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Actor> actors = gson.fromJson(ActorsJsonString, new TypeToken<List<Actor>>() {
        }.getType());
        System.out.printf("------------------> total number of actors : %d%n", actors.size());
        for (Actor actor: actors) {
            try {
                IemdbRepository.getInstance().insertActor(actor);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void getMovies() throws Exception {

        String MoviesJsonString = HTTPRequestHandler.getRequest(API_URL + MOVIES);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Movie> movies = gson.fromJson(MoviesJsonString, new TypeToken<List<Movie>>() {
        }.getType());
        System.out.printf("------------------> total number of movies : %d%n", movies.size());
        for (Movie movie: movies) {
            try {
                IemdbRepository.getInstance().insertMovie(movie);
                IemdbRepository.getInstance().insertMovieActor(movie);
                IemdbRepository.getInstance().insertMovieWriter(movie);
                IemdbRepository.getInstance().insertMovieGenre(movie);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void getUsers() throws Exception {

        String UsersJsonString = HTTPRequestHandler.getRequest(API_URL + USERS);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = gson.fromJson(UsersJsonString, new TypeToken<List<User>>() {
        }.getType());
        System.out.printf("------------------> total number of users : %d%n", users.size());
        for (User user: users) {
            try {
                user.setPasswordHash();
                IemdbRepository.getInstance().insertUser(user);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void getComments() throws Exception {

        String CommentsJsonString = HTTPRequestHandler.getRequest(API_URL + COMMENTS);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Comment>>() {
        }.getType());
        System.out.printf("------------------> total number of comments : %d%n", comments.size());
        for (Comment comment: comments) {
            try {
                IemdbRepository.getInstance().insertComment(comment);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
