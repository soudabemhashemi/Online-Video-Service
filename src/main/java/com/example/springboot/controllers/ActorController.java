package com.example.springboot.controllers;

import com.example.springboot.imedb.Actor;
import com.example.springboot.imedb.DataBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;


@RestController
@RequestMapping("/actors")
public class ActorController {
    @CrossOrigin(origins = "http://87.247.187.217:32666")
    @GetMapping("/{actorId}")
    public ResponseEntity<Object> getActorInfo(@PathVariable Integer actorId) throws SQLException {
        DataBase dataBase = DataBase.getInstance();
        if (dataBase.isAnybodyLoggedIn()) {
            Actor actor = dataBase.getActor(actorId);
            if(actor!=null)
                return ResponseEntity.status(HttpStatus.OK).body(actor);
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Actor with this id not found.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"No user is logged in.\"}");
        }
    }
}
