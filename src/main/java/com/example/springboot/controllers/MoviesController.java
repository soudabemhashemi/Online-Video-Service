package com.example.springboot.controllers;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
@RequestMapping("/movies")
public class MoviesController {

    @GetMapping("")
    public ResponseEntity<Object> getMoviesInfo(@RequestParam(required = false) String q, @RequestParam(required = false) String sort_by, @RequestParam(required = false) String search_by,
                                                HttpServletRequest request, HttpServletResponse response
                                                ) throws SQLException, ParseException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            ArrayList<Movie> movies = dataBase.sortMoviesByImdbRate(dataBase.getMovies());
            if (q != null)
                movies = dataBase.searchMoviesByName(q);
                if(search_by != null) {
                    if (search_by.equals("genre"))
                        movies = dataBase.searchMoviesByGenre(q);
                    if (search_by.equals("releaseDate"))
                        movies = dataBase.searchMoviesByReleaseDate(q);
                }
            if (sort_by != null) {
                if (sort_by.equals("imdb"))
                    movies = dataBase.sortMoviesByImdbRate(movies);
                if (sort_by.equals("date"))
                    movies = dataBase.sortMoviesByDate(movies);
            }
            return ResponseEntity.status(HttpStatus.OK).body(movies);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }

}
