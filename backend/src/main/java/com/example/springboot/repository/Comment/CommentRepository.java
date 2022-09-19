package com.example.springboot.repository.Comment;

import com.example.springboot.imedb.Comment;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository extends Repository<Comment, Integer> {
    private static final String TABLE_NAME = "COMMENTS";
    private static CommentRepository instance;

    public static CommentRepository getInstance() {
        if (instance == null) {
            try {
                instance = new CommentRepository(true);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in CommentRepository.create query.");
            }
        }
        return instance;
    }



    public CommentRepository(Boolean doManage) throws SQLException {
        if (doManage) {
            Connection con = ConnectionPool.getConnection();
            Statement st = con.createStatement();
            //st.executeUpdate(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
            st.executeUpdate(String.format(
                    "CREATE TABLE IF NOT EXISTS %s (\n" +
                            "    id int primary key AUTO_INCREMENT,\n" +
                            "    user CHAR(255) not null,\n" +
                            "    movie int not null,\n" +
                            "    text text not null,\n" +
//                            "    registerTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                            "FOREIGN KEY (user) REFERENCES USERS(email),"+
                            "FOREIGN KEY (movie) REFERENCES MOVIES(id)"+
                            ");",
                    TABLE_NAME));
            st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
            st.close();
            con.close();
        }
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s WHERE %S.id = ?;", TABLE_NAME,  TABLE_NAME);
    }

        protected String getFindByMovieIdStatement(){
            return String.format("select * from %s where movie = ? ;", TABLE_NAME);
        }

        protected void fillFindByMovieIdValues(PreparedStatement st, Integer id) throws SQLException {
            st.setInt(1, id);
        }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, Integer id) throws SQLException {
        st.setInt(1, id);
    }

    @Override
    protected String getInsertStatement() {
        return String.format("INSERT INTO %s(id, " +
                "user, movie, text)" +
                " VALUES(?,?,?,?)", TABLE_NAME);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Comment data) throws SQLException {
        st.setInt(1, 0);
        st.setString(2, data.getUserEmail());
        st.setInt(3, data.getMovieId());
        st.setString(4, data.getText());
//        st.setString(5, data.getRegisterTime());
    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Comment data) throws SQLException {

    }

    @Override
    protected String getFindAllStatement() {
        return null;
    }

    @Override
    protected Comment convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Comment(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
    }

    @Override
    protected ArrayList<Comment> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        if(rs.isFirst())
            comments.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            comments.add(this.convertResultSetToDomainModel(rs));
        }
        return comments;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Comment obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }


    public List<Comment> findByMovieId(int id) throws SQLException {
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
            List<Comment> result = convertResultSetToDomainModelList(resultSet);
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
