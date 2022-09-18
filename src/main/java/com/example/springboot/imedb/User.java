package com.example.springboot.imedb;

import com.example.springboot.repository.Watchlist.WatchlistRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String birthDate;

    public User(int id, String email, String password, String name, String nickname, String birthDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
    }

    public String getUserEmail() {
        return email;
    }
    public String getNickname() {
        return nickname;
    }
    public String getName() {
        return name;
    }
    public String getUserBirthDay() {
        return birthDate;
    }
    public String getPassword() {return password;}

    public String addToWatchList(int movieId) throws SQLException {

        for (Movie movie: getWatchListMovies()){
            if(movie.getMovieId() == movieId)
                return "Exist";
        }
        WatchlistRepository.getInstance().insert(new Watchlist(id, movieId));
        return "Done";

    }

    public String removeFromWatchList(int movieId) throws SQLException {
        Watchlist watchlist = WatchlistRepository.getInstance().findByMovieId(id, movieId);
        WatchlistRepository.getInstance().delete(watchlist);
        return "NotExist";
    }

    public ArrayList<Movie> getWatchListMovies() throws SQLException {
        ArrayList<Watchlist> watchlists = WatchlistRepository.getInstance().findByUserId(id);
        ArrayList<Movie> watchlistsMovies = new ArrayList<>();
        if(watchlists== null)
            return watchlistsMovies;
        for (Watchlist i: watchlists){
            watchlistsMovies.add(DataBase.getInstance().getMovie(i.getMovieId()));
        }
        return watchlistsMovies;
    }

    public void setPasswordHash() {
        this.password = DigestUtils.sha256Hex(this.password.getBytes());
    }

}
