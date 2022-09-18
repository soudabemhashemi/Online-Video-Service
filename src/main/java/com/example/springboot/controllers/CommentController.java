package com.example.springboot.controllers;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.StaticVariables;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @PostMapping("/{commentId}/vote")
    public ResponseEntity postVoteComment(@RequestBody String action, @PathVariable Integer commentId, HttpServletRequest request) throws SQLException {

        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            String loggedInUser = (String) request.getAttribute("user");
            int state = 0;
            if ("like=".equals(action)) {
                state = dataBase.voteComment(loggedInUser, commentId, 1);
            } else if ("dislike=".equals(action)) {
                state = dataBase.voteComment(loggedInUser, commentId, -1);
            }
            if(state == StaticVariables.InvalidVoteValue)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid vote value.\"}");
            if(state == StaticVariables.CommentNotFound)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Comment with this id not found.\"}");
            return ResponseEntity.status(HttpStatus.OK).body(dataBase.getComment(commentId));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }
}
