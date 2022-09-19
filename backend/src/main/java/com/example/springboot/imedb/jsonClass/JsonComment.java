package com.example.springboot.imedb.jsonClass;

import com.example.springboot.imedb.DataBase;
import com.example.springboot.imedb.User;

import java.sql.SQLException;


public class JsonComment {
    private String userName;
    private Integer movieId;
    private String text;
    private int like;
    private int dislike;
    private Integer commentId;

    public JsonComment(String user, Integer movieId, String text, int like, int dislike, Integer commentId) {
        this.userName = user;
        this.movieId = movieId;
        this.text = text;
        this.like = like;
        this.dislike = dislike;
        this.commentId = commentId;
    }

    public String getUserName(){return userName;}
    public Integer getMovieId(){return movieId;}
    public String getText(){return text;}
    public int getLike() throws SQLException {return DataBase.getInstance().getComment(commentId).getLike();}
    public int getDislike() throws SQLException {return DataBase.getInstance().getComment(commentId).getDislike();}
    public Integer getCommentId(){return commentId;}
}
