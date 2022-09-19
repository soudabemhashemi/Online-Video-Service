package com.example.springboot.controllers;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/movies")
public class suggestedMoviesController {
    @GetMapping("/suggested")
    public ResponseEntity<Object> getSuggestedMovies(HttpServletRequest request) throws SQLException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            ArrayList<Movie> movies = dataBase.getRecommendationMovies(request);
            return ResponseEntity.status(HttpStatus.OK).body(movies);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }
}
