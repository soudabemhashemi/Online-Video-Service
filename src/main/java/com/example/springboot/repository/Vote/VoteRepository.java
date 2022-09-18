package com.example.springboot.repository.Vote;

import com.example.springboot.imedb.*;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoteRepository extends Repository<Vote, String> {
    private static final String COLUMNS = "userEmail, commentId, vote";
    private static final String TABLE_NAME = "VOTES";
    private static VoteRepository instance;

    public static VoteRepository getInstance() {
        if (instance == null) {
            try {
                instance = new VoteRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in VoteRepository.create query.");
            }
        }
        return instance;
    }

    public VoteRepository() throws SQLException {

        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        int createTableStatement = st.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s(userEmail CHAR(50),\ncommentId int,\nvote int, PRIMARY KEY(userEmail, commentId),\n" +
                        "FOREIGN KEY (userEmail) REFERENCES USERS(email), \n" +
                        "FOREIGN KEY (commentId) REFERENCES COMMENTS(id));", TABLE_NAME)
        );
        st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
        st.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s WHERE %S.userEmail = ?;", TABLE_NAME,  TABLE_NAME);
    }

    protected String getFindByCommentIdStatement(){
        return String.format("SELECT* FROM %s WHERE %S.commentId = ?;", TABLE_NAME,  TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String userEmail) throws SQLException {
        st.setString(1, userEmail);
    }

    protected void fillFindByCommentIdValues(PreparedStatement st, int commentId) throws SQLException {
        st.setInt(1, commentId);
    }



    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s( %s ) VALUES(?,?,?)", TABLE_NAME, COLUMNS);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Vote data) throws SQLException {
        st.setString(1, data.getUserEmail());
        st.setInt(2, data.getCommentId());
        st.setInt(3, data.getVote());
    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Vote data) throws SQLException {

    }


    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Vote convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Vote(rs.getString(1), rs.getInt(2), rs.getInt(3));
    }

    @Override
    protected ArrayList<Vote> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Vote> votes = new ArrayList<>();
        if(rs.isFirst())
            votes.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            votes.add(this.convertResultSetToDomainModel(rs));
        }
        return votes;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Vote obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public void insertUserComment(String userEmail, int commentId, int vote) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.execute(String.format("INSERT IGNORE INTO votes(userEmail, commentId, vote) VALUES(%s, %d, %d)", userEmail, commentId, vote));
        st.close();
        con.close();
    }


    public List<Vote> findByCommentId(int commentId) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByCommentIdStatement());
        fillFindByCommentIdValues(st, commentId);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            List<Vote> result = convertResultSetToDomainModelList(resultSet);
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

//    @Override
//    protected User convertResultSetToDomainModel(ResultSet rs) throws SQLException {
//        return new User(rs.getString(1), rs.getString(2), rs.getString(3));
//    }

//    @Override
//    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
//        ArrayList<User> users = new ArrayList<>();
//        while (rs.next()) {
//            users.add(this.convertResultSetToDomainModel(rs));
//        }
//        return users;
//    }
}

