package com.example.springboot.controllers;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.Movie;
import com.example.springboot.imedb.StaticVariables;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @GetMapping("/{movieId}")
    public ResponseEntity<Object> getMovieInfo(@PathVariable Integer movieId) throws InterruptedException, SQLException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            TimeUnit.SECONDS.sleep(4);
            Movie movie = dataBase.getMovie(movieId);
            if(movie!=null)
                return ResponseEntity.status(HttpStatus.OK).body(movie);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Movie with this id not found.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }

    @PostMapping("/{movieId}/comment")
    public ResponseEntity postAddComment(@RequestBody String commentText, @PathVariable Integer movieId, HttpServletRequest request) throws SQLException {

        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            commentText = commentText.substring(0, commentText.length()-1);
            String loggedInUser = (String) request.getAttribute("user");
            dataBase.addComment(loggedInUser, movieId, commentText );
            return ResponseEntity.status(HttpStatus.OK).body(dataBase.getMovie(movieId).giveComments());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }

    @GetMapping("/{movieId}/watchlist")
    public ResponseEntity postAddTOWatchList(@PathVariable Integer movieId, HttpServletRequest request) throws ParseException, SQLException {

        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            String loggedInUser = (String) request.getAttribute("user");
            int resultOfAdd = dataBase.addToWatchList(loggedInUser, movieId);
            if( resultOfAdd == StaticVariables.AgeLimitError)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\": \"Age Limit Error\"}");
            if(resultOfAdd == StaticVariables.MovieNotFound)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Movie with this id not found.\"}");
            if(resultOfAdd == StaticVariables.MovieAlreadyExists)
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("{\"message\": \"Movie already exists.\"}");
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Movie added to watchlist successfully.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }

    @DeleteMapping("/{movieId}/watchlist")
    public ResponseEntity deleteFromWatchList(@PathVariable Integer movieId, HttpServletRequest request) throws ParseException, InterruptedException, SQLException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            String loggedInUser = (String) request.getAttribute("user");
            int resultOfDelete = dataBase.removeFromWatchList(loggedInUser, movieId);
            if(resultOfDelete == StaticVariables.MovieNotFound)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Movie with this id not found.\"}");
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Movie deleted from watchlist successfully.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }


    @PostMapping("/{movieId}/rate")
    public ResponseEntity postRateMovie(@PathVariable Integer movieId, @RequestBody Integer rate, HttpServletRequest request) throws ParseException, SQLException {

        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            String loggedInUser = (String) request.getAttribute("user");
            int state = dataBase.rateMovie(loggedInUser, movieId, rate);
            if (state == StaticVariables.InvalidRateScore)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid rate score.\"}");
            if (state == StaticVariables.MovieNotFound)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Movie with this id not found.\"}");
            Movie movie = dataBase.getMovie(movieId);
            return ResponseEntity.status(HttpStatus.OK).body(movie);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }


}
