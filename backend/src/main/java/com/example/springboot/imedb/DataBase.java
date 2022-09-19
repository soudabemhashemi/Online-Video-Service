package com.example.springboot.imedb;

import com.example.springboot.imedb.jsonClass.JsonActor;
import com.example.springboot.imedb.jsonClass.JsonMovie;
import com.example.springboot.repository.Actor.ActorRepository;
import com.example.springboot.repository.Cast.CastRepository;
import com.example.springboot.repository.Comment.CommentRepository;
import com.example.springboot.repository.Genre.GenreRepository;
import com.example.springboot.repository.IemdbRepository;
import com.example.springboot.repository.Movie.MovieRepository;
import com.example.springboot.repository.User.UserRepository;
import com.example.springboot.repository.Vote.VoteRepository;
import jdk.security.jarsigner.JarSigner;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DataBase {
    private static DataBase instance = null;
    private String loggedInUser = "no user";

    public static DataBase getInstance() {
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }

    private ArrayList<Actor> actors = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    public Actor getActor(int id) throws SQLException {
        return ActorRepository.getInstance().findById(id);
    }

    public Movie getMovie(int movieId) throws SQLException {
        return MovieRepository.getInstance().findById(movieId);
    }

    public ArrayList<Movie> getMoviesByGenre(String genre) throws SQLException {
        List<Genre> genres = GenreRepository.getInstance().findByGenre(genre);
        ArrayList<Movie> movies = new ArrayList<>();
        for (Genre g: genres){
            movies.add(MovieRepository.getInstance().findById(g.getMovieId()));
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByYear(Integer startYear) throws ParseException, SQLException {
        return MovieRepository.getInstance().findByYear(startYear);
    }

    public Comment getComment(int commentId) throws SQLException {
        return CommentRepository.getInstance().findById(commentId);
    }

    public ArrayList<Movie> getMovies() throws SQLException {
        return MovieRepository.getInstance().findAll();
    }

    public User getUser(String userEmail) throws SQLException {
        return UserRepository.getInstance().findByEmail(userEmail);
    }

    public void addActor(int id, String name, String birthDate, String nationality, String image) throws SQLException {
        Actor actor = getActor(id);
        if(actor == null){
            actor = new Actor(id, name, birthDate, nationality, image);
            ActorRepository.getInstance().insert(actor);
        }
        else
            ActorRepository.getInstance().update(actor);
    }

    public void addMovie(int id, String name, String summary, String releaseDate, String director, List<String> writers, List<String> genres, List<Integer> cast, float imdbRate, Integer duration, int ageLimit, String image, String coverImage) throws SQLException {
        Movie movie = getMovie(id);
        if(movie == null){
            movie = new Movie(id, name, summary, releaseDate, director,
                    imdbRate, duration, ageLimit, image, coverImage);
            MovieRepository.getInstance().insert(movie);
        }
        else
            MovieRepository.getInstance().update(movie);

        IemdbRepository.getInstance().insertMovieActor(movie);
        IemdbRepository.getInstance().insertMovieWriter(movie);
        IemdbRepository.getInstance().insertMovieGenre(movie);

    }

    public boolean addUser(String email, String password, String name, String nickname, String birthDate) throws SQLException {
        User user = getUser(email);
        if(user == null){
            user = new User(0, email, password, name, nickname, birthDate);
            user.setPasswordHash();
            UserRepository.getInstance().insert(user);
            return true;
        }
        else
            return false;
    }

    public boolean updateUser(String email, String password, String name, String nickname, String birthDate) throws SQLException {
        User user = getUser(email);
        if(user != null){
            UserRepository.getInstance().update(user);
            return true;
        }
        else
            return false;
    }

    public int addComment(String userEmail, int movieId, String text) throws SQLException {
        User user = this.getUser(userEmail);
        if (user == null)
            return StaticVariables.UserNotFound;
        Movie movie = this.getMovie(movieId);
        if (movie == null)
            return StaticVariables.MovieNotFound;
        Comment comment = new Comment(comments.size()+1, user.getUserEmail(), movie.getMovieId(), text);
        CommentRepository.getInstance().insert(comment);
        return comment.getId();
    }

    //todo
    public int rateMovie(String userEmail, int movieId, int score) throws SQLException {
        if (score > 10)
            return StaticVariables.InvalidRateScore;
        if (score < 0)
            return StaticVariables.InvalidRateScore;
        User user = this.getUser(userEmail);
        if (user == null)
            return StaticVariables.UserNotFound;
        Movie movie = this.getMovie(movieId);
        if (movie == null)
            return StaticVariables.MovieNotFound;
        movie.registerRateMovie(userEmail, score);
        movie.updateRate();
        return StaticVariables.SUCCESS;
    }

    //todo
    public int voteComment(String userEmail, int commentId, int vote) throws SQLException {
        if (!(vote == 0 | vote == 1 | vote == -1))
            return StaticVariables.InvalidVoteValue;
        User user = this.getUser(userEmail);
        if (user == null)
            return StaticVariables.UserNotFound;
        Comment comment = this.getComment(commentId);
        if (comment == null)
            return StaticVariables.CommentNotFound;
//        comment.registerVoteComment(userEmail, commentId, vote);
        Vote voteObj = new Vote(userEmail, commentId, vote);
        VoteRepository.getInstance().insert(voteObj);
//        comment.updateslikesDislikes();
        return StaticVariables.SUCCESS;
    }

    public int addToWatchList(String userEmail, int movieId) throws ParseException, SQLException {
        User user = this.getUser(userEmail);
        if (user == null)
            return StaticVariables.UserNotFound;
        Movie movie = this.getMovie(movieId);
        if (movie == null)
            return StaticVariables.MovieNotFound;

        Date d = new Date();
        int currentYear=d.getYear();
        Date birthDate=new SimpleDateFormat("yyyy-MM-dd").parse(user.getUserBirthDay());
        int birthYear = birthDate.getYear();
        if(movie.giveAgeLimit() > currentYear - birthYear)
            return StaticVariables.AgeLimitError;
        String result = user.addToWatchList(movieId);
        if (Objects.equals(result, "Exist"))
            return StaticVariables.MovieAlreadyExists;
        return StaticVariables.SUCCESS;


    }

    public int removeFromWatchList(String userEmail, int movieId) throws SQLException {
        User user = this.getUser(userEmail);
        if (user == null)
            return StaticVariables.UserNotFound;
        Movie movie = this.getMovie(movieId);
        if (movie == null)
            return StaticVariables.MovieNotFound;
        String result = user.removeFromWatchList(movieId);
        if (Objects.equals(result, "NotExist"))
            return StaticVariables.MovieNotFound;
        return StaticVariables.SUCCESS;
    }

    public ArrayList<Movie> getWatchList(String userEmail) throws SQLException {
        User user = this.getUser(userEmail);
        if (user == null)
            return null;
        return user.getWatchListMovies();
    }

    public ArrayList<JsonMovie> get_acted_movies(Actor actor) throws SQLException {
        ArrayList<JsonMovie> acted_movies = new ArrayList<>();
        List<Cast> casts = CastRepository.getInstance().findByActorId(actor.getId());
        if (casts == null)
            return acted_movies;
        for(Cast cast: casts) {
            Movie movie = getMovie(cast.getMovieId());
            acted_movies.add(new JsonMovie(movie.getName(), movie.getImdbRate(), movie.getImage(), movie.getMovieId()));
        }
        return acted_movies;
    }

    public void freshMovies(){
        movies.clear();
    }

    public void freshUsers(){
        users.clear();
    }

    public void freshComments(){
        comments.clear();
    }

    public boolean loginUser(String email, String password) throws SQLException {
        User user = getUser(email);
        if (user != null && user.getPassword().equals((DigestUtils.sha256Hex(password.getBytes())))) {
            loggedInUser = email;
            return true;
        }
        return false;
    }

    public void logoutUser(){
        loggedInUser = "no user";
    }

    public String getLoggedInUserId(){
        return loggedInUser;
    }

    public boolean isAnybodyLoggedIn(){
        return true;
    }

    public ArrayList<Movie> searchMoviesByName(String searchString) throws SQLException {
        return MovieRepository.getInstance().findBy(searchString, "name");
    }

    public ArrayList<Movie> searchMoviesByGenre(String searchString) throws SQLException {
        return getMoviesByGenre(searchString);
    }

    public ArrayList<Movie> searchMoviesByReleaseDate(String searchString) throws SQLException, ParseException {
        return getMoviesByYear(Integer.parseInt(searchString));
    }

    public ArrayList<Movie> sortMoviesByImdbRate(ArrayList<Movie> movies) throws SQLException {
        return MovieRepository.getInstance().sortMovies("imdbRate");
    }

    public ArrayList<Movie> sortMoviesByDate(ArrayList<Movie> movies) throws SQLException {
        return MovieRepository.getInstance().sortMovies("releaseDate");
    }

    //todo
    public ArrayList<Movie> getRecommendationMovies(HttpServletRequest request) throws SQLException {
        ArrayList<Movie> recommendedMovies = getMovies();
        ArrayList<Movie> watchlist = getWatchList((String) request.getAttribute("user"));
        ArrayList<Movie> returnedMovies = new ArrayList<>();
        int count = 0;
        for (Movie movie : recommendedMovies) {
            if (!watchlist.contains(movie)) {
                returnedMovies.add(movie);
                count += 1;
            }
            if (count == 3)
                return returnedMovies;
        }
        return returnedMovies;
    }

    //todo
    public int getNumberOfSameGenre(Movie movie1, Movie movie2) throws SQLException {
        int count = 0;
        for (String genre: movie1.getGenresName()){
            if(movie2.getGenresName().contains(genre))
                count += 1;
        }
        return count;
    }

//    public void setMoviesActors() throws SQLException {
//        for (Movie movie:movies){
//            for(int actorId: movie.getCastId()){
//                Actor actor = getActor(actorId);
//                movie.setActor(new JsonActor(actor.getName(), actor.getBirthDate(), actor.getImage(), actorId));
//            }
//        }
//    }


}
