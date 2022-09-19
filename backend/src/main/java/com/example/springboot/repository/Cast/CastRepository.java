package com.example.springboot.repository.Cast;

import com.example.springboot.imedb.Cast;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CastRepository extends Repository<Cast, Integer> {
    private static final String TABLE_NAME = "MOVIEACTOR";
    private static CastRepository instance;

    public static CastRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CastRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in castRepository.create query.");
            }
        }
        return instance;
    }



    public CastRepository() throws SQLException {
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
        return String.format("INSERT INTO %s(movieId, actorId)" +
                " VALUES(?,?)", TABLE_NAME);
    }


    protected String getFindByMovieIdStatement(){
        return String.format("select * from %s where movieId = ? ;", TABLE_NAME);
    }

    protected void fillFindByMovieIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    protected String getFindByActorIdStatement(){
        return String.format("select * from %s where actorId = ? ;", TABLE_NAME);
    }

    protected void fillFindByActorIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Cast data) throws SQLException {
        st.setInt(1, data.getMovieId());
        st.setInt(2, data.getActorId());

    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Cast data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }

    @Override
    protected Cast convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Cast(rs.getInt(1), rs.getInt(2));
    }

    @Override
    protected ArrayList<Cast> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Cast> casts = new ArrayList<>();
        if(rs.isFirst())
            casts.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            casts.add(this.convertResultSetToDomainModel(rs));
        }
        return casts;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Cast obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public List<Cast> findByMovieId(int id) throws SQLException {
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
            List<Cast> result = convertResultSetToDomainModelList(resultSet);
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

    public List<Cast> findByActorId(int actorId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByActorIdStatement());
        fillFindByActorIdValues(st, actorId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            List<Cast> result = convertResultSetToDomainModelList(resultSet);
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
}


