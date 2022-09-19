package com.example.springboot.imedb;

public class Vote {
    private String userEmail;
    private int commentId;
    private int vote;


    public Vote(String userEmail, int commentId, int vote){
        this.userEmail = userEmail;
        this.commentId  = commentId;
        this.vote = vote;
    }


    public String getUserEmail() {return userEmail;}
    public int getCommentId() {return commentId;}
    public int getVote() {return vote;}
}
