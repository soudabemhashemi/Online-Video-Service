package com.example.springboot.repository.Movie;

import com.example.springboot.imedb.Actor;
import com.example.springboot.imedb.Cast;
import com.example.springboot.imedb.Movie;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository extends Repository<Movie, Integer> {
    private static final String TABLE_NAME = "MOVIES";
    private static MovieRepository instance;

    public static MovieRepository getInstance() {
        if (instance == null) {
            try {
                instance = new MovieRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in MovieRepository.create query.");
            }
        }
        return instance;
    }

    public MovieRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        //st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        st.executeUpdate(String.format(
                "CREATE TABLE IF NOT EXISTS %s (\n" +
                        "    id int primary key,\n" +
                        "    name varchar(255) not null,\n" +
                        "    summary text not null,\n" +
                        "    releaseDate datetime not null,\n" +
                        "    director varchar(255) not null,\n" +
//                           "    writers varchar(255) not null,\n" +
//                           "    genres varchar(255) not null,\n" +
//                           "    cast varchar(255) not null,\n" +
                        "    imdbRate FLOAT not null,\n" +
                        "    duration  int not null,\n" +
                        "    ageLimit int not null,\n" +
                        "    image varchar(255) not null,\n" +
                        "    coverImage varchar(255) not null\n" +
                            ");",
                TABLE_NAME));
        st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
        st.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s WHERE %S.id = ?;", TABLE_NAME,  TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s(id, " +
                "name, summary, releaseDate, director," +
//                " writers, genres, cast," +
                " imdbRate, duration," +
                " ageLimit, image, coverImage)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Movie data) throws SQLException {
        st.setInt(1, data.getMovieId());
        st.setString(2, data.getName());
        st.setString(3, data.getSummary());
        st.setString(4, data.getReleaseDate());
        st.setString(5, data.getDirector());
//        st.setArray(6, (Array) data.getWriters());
//        st.setArray(7, (Array) data.getGenres());
//        st.setArray(8, (Array) data.getCast());
        st.setFloat(6, data.getImdbRate());
        st.setInt(7, data.getDuration());
        st.setInt(8, data.getAgeLimit());
        st.setString(9, data.getImage());
        st.setString(10, data.getCoverImage());


    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Movie data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT* FROM %s;", TABLE_NAME);
    }

    @Override
    protected Movie convertResultSetToDomainModel(ResultSet rs) throws SQLException {
//        ArrayList<String> writers = MovieRepository.getInstance().findWriterById(rs.getInt("id"));
        return new Movie(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("summary"),
                rs.getString("releaseDate"),
                rs.getString("director"),
                rs.getFloat("imdbRate"),
                rs.getInt("duration"),
                rs.getInt("ageLimit"),
                rs.getString("image"),
                rs.getString("coverImage")
                );
    }

    @Override
    protected ArrayList<Movie> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();
        if(rs.isFirst())
            movies.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            movies.add(this.convertResultSetToDomainModel(rs));
        }
        return movies;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Movie obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public void insertMovieWriter(Movie movie) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        List<String> writersList = movie.getWritersName();
        for (String writer: writersList){
            String writerName = '"' + writer+ '"';
            st.execute(String.format("INSERT IGNORE INTO MOVIEWRITER(movieId, writerName) VALUES(%d, %s)", movie.getMovieId(), writerName));
        }
        st.close();
        con.close();
    }

    public void insertMovieActor(Movie movie) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        List<Integer> actorsId = movie.getCastId();
        for (Integer actorId: actorsId){
            st.execute(String.format("INSERT IGNORE INTO MOVIEACTOR(movieId, actorId) VALUES(%d, %d)", movie.getMovieId(), actorId));
        }
        st.close();
        con.close();
    }



    public void insertMovieGenre(Movie movie) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        List<String> genres = movie.getGenresName();
        for (String genre: genres){
            genre = '"' + genre + '"';
//            System.out.println(genre);
            st.execute(String.format("INSERT IGNORE INTO MOVIEGENRE(movieId, genre) VALUES(%d, %s)", movie.getMovieId(), genre));
        }
        st.close();
        con.close();
    }

    public void findWriterById(){

    }

    protected String getFindByYearStatement() {
        return String.format("SELECT * FROM %s WHERE YEAR(releaseDate) > ? ;", TABLE_NAME);
    }

    protected void fillFindByYearValues(PreparedStatement st, int startYear) throws SQLException {
        st.setInt(1, startYear);
    }

    protected String getFindByStatement(String stringInput, String based_on) {
        return String.format("SELECT * FROM %s WHERE %s LIKE \"%% %s %%\";", TABLE_NAME, based_on, stringInput);
    }

    protected String getSortByStatement(String base) {
        return String.format("SELECT * FROM %s ORDER BY %s DESC;", TABLE_NAME, base);
    }


    public ArrayList<Movie> findByYear(int startYear) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByYearStatement());
        fillFindByYearValues(st, startYear);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<Movie> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<Movie> findBy(String searchString, String based_on) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByStatement(searchString, based_on));
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<Movie> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<Movie> sortMovies(String based_on) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getSortByStatement(based_on));
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            ArrayList<Movie> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.findAll query.");
            e.printStackTrace();
            throw e;
        }
    }

}
