package com.example.springboot.repository.Genre;

import com.example.springboot.imedb.Genre;
import com.example.springboot.imedb.Movie;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreRepository extends Repository<Genre, Integer> {
    private static final String TABLE_NAME = "MOVIEGENRE";
    private static GenreRepository instance;

    public static GenreRepository getInstance() {
        if (instance == null) {
            try {
                instance = new GenreRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in GenreRepository.create query.");
            }
        }
        return instance;
    }



    public GenreRepository() throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        //st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
//        st.executeUpdate(String.format(
//                "CREATE TABLE IF NOT EXISTS %s (\n" +
//                        "    id int AUTO_INCREMENT,\n" +
//                        "    movieId int not null,\n" +
//                        "    writerName varchar(255) not null,\n" +
//                        "PRIMARY KEY(movieId, writerName),\n" +
//                        "FOREIGN KEY (movieId) REFERENCES MOVIES(id),"+
//                        ");",
//                TABLE_NAME));
//        st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
        st.close();
        con.close();

    }

    @Override
    protected String getFindByIdStatement() {
        return null;
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {

    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(" +
                "movieId, genreName)" +
                " VALUES(?,?)", TABLE_NAME);
    }


    protected String getFindByMovieIdStatement(){
        return String.format("select distinct * from %s where movieId = ? ;", TABLE_NAME);
    }

    protected void fillFindByMovieIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Genre data) throws SQLException {
        st.setInt(1, data.getMovieId());
        st.setString(2, data.getName());

    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Genre data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }

    @Override
    protected Genre convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Genre( rs.getInt(1), rs.getString(2));
    }

    @Override
    protected ArrayList<Genre> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Genre> genres = new ArrayList<>();
        if(rs.isFirst())
            genres.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            genres.add(this.convertResultSetToDomainModel(rs));
        }
        return genres;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Genre obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public List<Genre> findByMovieId(int id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByMovieIdStatement());
        fillFindByMovieIdValues(st, id);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            List<Genre> result = convertResultSetToDomainModelList(resultSet);
            st.close();
            con.close();
            return result;
        } catch (Exception e) {
            st.close();
            con.close();
            System.out.println("error in Repository.find query.");
            e.printStackTrace();
            throw e;
        }
    }


    protected String getFindByGenreStatement() {
        return String.format("select * from %s where genre = ? ;", TABLE_NAME);
    }

    protected void fillFindByGenreValues(PreparedStatement st, String genre) throws SQLException {
        st.setString(1, genre);
    }

    public List<Genre> findByGenre(String genre) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByGenreStatement());
        fillFindByGenreValues(st, genre);
        try {
            ResultSet resultSet = st.executeQuery();
            if (resultSet == null) {
                st.close();
                con.close();
                return new ArrayList<>();
            }
            List<Genre> result = convertResultSetToDomainModelList(resultSet);
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


