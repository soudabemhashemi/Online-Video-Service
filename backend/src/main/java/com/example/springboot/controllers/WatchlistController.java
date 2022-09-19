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
@RequestMapping("/user/watchlist")
public class WatchlistController {

    @GetMapping("")
    public ResponseEntity<Object> getWatchlistInfo(HttpServletRequest request) throws SQLException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            String loggedInUser = (String) request.getAttribute("user");
            ArrayList<Movie> watchlistMovies = dataBase.getWatchList(loggedInUser);
            return ResponseEntity.status(HttpStatus.OK).body(watchlistMovies);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }
}
