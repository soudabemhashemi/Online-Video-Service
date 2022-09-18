package com.example.springboot.repository.Writer;


import com.example.springboot.imedb.Cast;
import com.example.springboot.imedb.Movie;
import com.example.springboot.imedb.Writer;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WriterRepository extends Repository<Writer, Integer> {
    private static final String TABLE_NAME = "MOVIEWRITER";
    private static WriterRepository instance;

    public static WriterRepository getInstance() {
        if (instance == null) {
            try {
                instance = new WriterRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in WriterRepository.create query.");
            }
        }
        return instance;
    }



    public WriterRepository() throws SQLException {
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
        return String.format("INSERT INTO %s( " +
                "movieId, writerName)" +
                " VALUES(?,?)", TABLE_NAME);
    }


    protected String getFindByMovieIdStatement(){
        return String.format("select * from %s where movieId = ? ;", TABLE_NAME);
    }

    protected void fillFindByMovieIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Writer data) throws SQLException {
        st.setInt(1, data.getMovieId());
        st.setString(2, data.getName());

    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Writer data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }

    @Override
    protected Writer convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Writer( rs.getInt(1), rs.getString(2));
    }

    @Override
    protected ArrayList<Writer> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Writer> writers = new ArrayList<>();
        if(rs.isFirst())
            writers.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            writers.add(this.convertResultSetToDomainModel(rs));
        }
        return writers;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Writer obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public List<Writer> findByMovieId(int id) throws SQLException {
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
            List<Writer> result = convertResultSetToDomainModelList(resultSet);
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

    public void insert(Movie movie) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        List<String> writersList = movie.getWritersName();
        for (String writer: writersList){
            String writerName = '"' + writer + '"';
            st.execute(String.format("INSERT IGNORE INTO MOVIEWRITER(movieId, writerName) VALUES(%d, %s)", movie.getMovieId(), writerName));
        }
        st.close();
        con.close();
    }
}

