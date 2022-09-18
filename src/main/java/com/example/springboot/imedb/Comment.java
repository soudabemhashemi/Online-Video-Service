package com.example.springboot.imedb;

import com.example.springboot.imedb.jsonClass.JsonMovie;
import com.example.springboot.repository.Comment.CommentRepository;
import com.example.springboot.repository.IemdbRepository;
import com.example.springboot.repository.Movie.MovieRepository;
import com.example.springboot.repository.User.UserRepository;
import com.example.springboot.repository.Vote.VoteRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class Comment {
    private int id;
    private User user;
    private String userEmail;
    private JsonMovie movie;
    private Integer movieId;
    private String text;
    private String registerTime;
//    private HashMap<String, Integer> votes = new HashMap<>();
    private int like;
    private int dislike;


    public Comment(int id, String userEmail, int movieId, String text) {
        this.id = id;
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.text = text;
        this.registerTime =  LocalDateTime.now().toString();

    }

    public int getId() {
        return id;
    }

    public int getLike() {
        return like;
    }
    public int getLike1() throws SQLException {
        int likes = 0;
        List<Vote> votes = VoteRepository.getInstance().findByCommentId(id);
        if (votes == null){
            return 0;
        }
        for (Vote vote: votes){
            if(vote.getVote() == 1){
                likes++;
            }
        }
        return likes;
    }

    public User getUser() {
        return user;
    }
//    public User getUserObj() throws SQLException {
//        return UserRepository.getInstance().findById(user.getUserEmail());
//    }                      
    public int getDislike() {
        return dislike;
    }

    public int getDislike1() throws SQLException {
        int dislikes = 0;
        List<Vote> votes = VoteRepository.getInstance().findByCommentId(id);
        if (votes == null){
            return 0;
        }
        for (Vote vote: votes){
            if(vote.getVote() == -1){
                dislikes++;
            }
        }
        return dislikes;
    }

    public String getText() {
        return text;
    }
    public JsonMovie getMovie() {return movie;}
//    public Movie getMovieObj() throws SQLException {return MovieRepository.getInstance().findById(movie.getMovieId());}
    public String getUserEmail() {return userEmail;}
    public Integer getMovieId() {return movieId;}
    public String getRegisterTime(){return registerTime;}

    public void registerVoteComment(String userEmail, int commentId, int vote) throws SQLException {
        IemdbRepository.getInstance().insertUserComment(userEmail, commentId, vote);

//        if (!votes.containsKey(userEmail)){
////            votes.put(userEmail, vote);
//        }else{
//            votes.replace(userEmail, vote);
//        }
    }

//    public void updateslikesDislikes(){
//        like = 0;
//        dislike = 0;
//        for (int vote: votes.values()){
//            if (vote == 1)
//                like += 1;
//            else if (vote == -1)
//                dislike +=1;
//        }
//    }

}
