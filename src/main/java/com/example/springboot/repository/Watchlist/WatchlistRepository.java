package com.example.springboot.repository.Watchlist;

import com.example.springboot.imedb.Watchlist;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;

public class WatchlistRepository extends Repository<Watchlist, Integer> {
    private static final String COLUMNS = "userId, movieId";
    private static final String TABLE_NAME = "WATCHLISTS";
    private static WatchlistRepository instance;

    public static WatchlistRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    public WatchlistRepository() throws SQLException {

        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        int createTableStatement = st.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s(" +
                        "userId int,\n" +
                        "movieId int,\n PRIMARY KEY(userId, movieId),\n" +
                        " FOREIGN KEY (userId) REFERENCES USERS(id),\n" +
                        " FOREIGN KEY (movieId) REFERENCES MOVIES(id));", TABLE_NAME)
        );
        st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
        st.close();
        con.close();
    }


    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s WHERE %S.userId = ?;", TABLE_NAME,  TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s( %s ) VALUES(?,?)", TABLE_NAME, COLUMNS);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Watchlist data) throws SQLException {
        st.setInt(1, data.getUserId());
        st.setInt(2, data.getMovieId());
    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Watchlist data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Watchlist convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Watchlist(
                rs.getInt("userId"),
                rs.getInt("movieId"));
    }

    @Override
    protected ArrayList<Watchlist> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Watchlist> watchlists = new ArrayList<>();
        if(rs.isFirst())
            watchlists.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            watchlists.add(this.convertResultSetToDomainModel(rs));
        }
        return watchlists;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Watchlist obj) throws SQLException {
        st.setInt(1, obj.getUserId());
        st.setInt(2, obj.getMovieId());
    }

    @Override
    protected String getDeleteStatement() {
        return String.format("Delete FROM %s where userId=?, movieId=?;", TABLE_NAME);
    }

    public ArrayList<Watchlist> findByUserId(Integer id) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByIdStatement());
        fillFindByIdValues(st, id);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            ArrayList<Watchlist> result = convertResultSetToDomainModelList(resultSet);
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

    public Watchlist findByMovieId(Integer userId, Integer movieId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByMovieIdStatement());
        fillFindByMovieIdValues(st, userId, movieId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            Watchlist result = convertResultSetToDomainModel(resultSet);
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

    private void fillFindByMovieIdValues(PreparedStatement st, Integer userId, Integer movieId) throws SQLException {
        st.setInt(1, userId);
        st.setInt(2, movieId);
    }

    private String getFindByMovieIdStatement() {
        return String.format("Select * FROM %s where userId=?, movieId=?;", TABLE_NAME);
    }

}
