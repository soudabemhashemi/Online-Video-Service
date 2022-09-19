package com.example.springboot.imedb;


import com.example.springboot.imedb.jsonClass.JsonActor;
import com.example.springboot.imedb.jsonClass.JsonComment;
import com.example.springboot.repository.Actor.ActorRepository;
import com.example.springboot.repository.Cast.CastRepository;
import com.example.springboot.repository.Comment.CommentRepository;
import com.example.springboot.repository.Genre.GenreRepository;
import com.example.springboot.repository.Writer.WriterRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Movie {
    private int id;
    private String name;
    private String summary;
    private String releaseDate;
    private String director;
    private List<String> writers;
    private List<String> genres = new ArrayList<>();
    private List<Integer> cast;
    private List<JsonActor> actors = new ArrayList<>();
    private float imdbRate;
    private Integer duration;
    private int ageLimit;
    private float rating;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<JsonComment> jsonComments = new ArrayList<>();
    private HashMap <String, Integer> rates = new HashMap<>();
    private String image;
    private String coverImage;


    public Movie(int id, String name, String summary, String releaseDate, String director, float imdbRate, int duration, int ageLimit, String image, String coverImage) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.director = director;
        this.imdbRate = imdbRate;
        this.duration = duration;
        this.ageLimit = ageLimit;
        this.image = image;
        this.coverImage = coverImage;
    }

    public int getMovieId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<String> getWritersName()  {
        return writers;
    }

    public List<Writer> getWriters() throws SQLException {
        return WriterRepository.getInstance().findByMovieId(id);
    }
    public List<Integer> getCastId() {
        return cast;
    }
    public float getImdbRate() {
        return imdbRate;
    }
    public Integer getDuration() {
        return duration;
    }
    public int getAgeLimit() {
        return ageLimit;
    }
    public String getDirector() {return director;}
    public List<String> getGenresName() throws SQLException {return genres;}
    public List<Genre> getGenres() throws SQLException {
        return GenreRepository.getInstance().findByMovieId(id);}
    public float getRating() {return rating;}
    public int giveAgeLimit() {
        return ageLimit;
    }
    public String giveReleaseDate() {return releaseDate;}
    public String getImage(){return image;}
    public String getCoverImage(){return coverImage;}
    public void setActor(JsonActor actor){actors.add(actor);}
    public List<JsonActor> getActors() throws SQLException {
        ArrayList<JsonActor> actors = new ArrayList<>();
        List<Cast> casts = CastRepository.getInstance().findByMovieId(id);
        if (casts == null){
            return actors;
        }
        for(Cast cast: casts){
            Actor actor = ActorRepository.getInstance().findById(cast.getActorId());
            actors.add(new JsonActor(actor.getName(), actor.getBirthDate(), actor.getImage(), actor.getId()));
        }
        return actors;
    }
    public ArrayList<Comment> giveComments(){return comments;}
    public List<Comment> getComments() throws SQLException {return  CommentRepository.getInstance().findByMovieId(id);}


    public void registerRateMovie(String userEmail, int score){
        if (!rates.containsKey(userEmail)){
            rates.put(userEmail, score);
        }else{
            rates.replace(userEmail, score);
        }
    }

    public void updateRate(){
        int sum = 0;
        int count = 0;
        for (int rate: rates.values()){
            sum += rate;
            count+=1;

        }
        rating = sum/ count;
    }

    public void updateMovie(String name, String summary, String releaseDate, String director, List<String> writers, List<String> genres, List<Integer> cast, float imdbRate, Integer duration, int ageLimit){
        this.name = name;
        this.summary = summary;
        this.releaseDate = releaseDate;
        this.director = director;
        this.writers = writers;
        this.genres = genres;
        this.cast = cast;
        this.imdbRate = imdbRate;
        this.duration = duration;
        this.ageLimit = ageLimit;

    }

    public boolean isInThisGenre(String genre){
        return genres.contains(genre);
    }

    public void registerComment(Comment comment){
        comments.add(comment);
        jsonComments.add(new JsonComment(comment.getUserEmail(), comment.getMovieId(), comment.getText(), comment.getLike(), comment.getDislike(), comment.getId()));

    }

    public boolean isInThisPeriod(int startYear, int endYear) throws ParseException {
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(this.giveReleaseDate());
        int releaseYear = releaseDate.getYear() + 1900;
        return releaseYear > startYear & releaseYear < endYear;
    }

    public float getScore(HttpServletRequest request) throws SQLException {
        int genre_similarity_with_watchlist = 0;
        DataBase dataBase = DataBase.getInstance();
        ArrayList<Movie> watchlist = dataBase.getWatchList((String) request.getAttribute("user"));
        if (watchlist != null) {
            for (Movie movie : watchlist)
                genre_similarity_with_watchlist += dataBase.getNumberOfSameGenre(this, movie);
        }
        return 3 * genre_similarity_with_watchlist + imdbRate + rating;
    }
}
