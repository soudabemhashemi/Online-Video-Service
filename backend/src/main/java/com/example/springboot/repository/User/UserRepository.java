package com.example.springboot.repository.User;

import com.example.springboot.imedb.User;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;

public class UserRepository extends Repository<User, String> {
    private static final String COLUMNS = "id, email, password, name, nickname, birthDate";
    private static final String TABLE_NAME = "USERS";
    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance == null) {
            try {
                instance = new UserRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in UserRepository.create query.");
            }
        }
        return instance;
    }

    public UserRepository() throws SQLException {

        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        int createTableStatement = st.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s(" +
                        "id int primary key AUTO_INCREMENT,\n" +
                        "email CHAR(255) unique,\n" +
                        "password CHAR(225),\n" +
                        "name CHAR(255) ,\n" +
                        "nickname CHAR(225), \n" +
                        "birthDate CHAR(225));", TABLE_NAME)
        );
        st.execute(String.format("ALTER TABLE %s CHARACTER SET utf8 COLLATE utf8_general_ci;", TABLE_NAME));
        st.close();
        con.close();
    }

    @Override
    protected String getFindByIdStatement() {
        return String.format("SELECT* FROM %s WHERE %S.id = ?;", TABLE_NAME,  TABLE_NAME);
    }

    protected String getFindByEmailStatement() {
        return String.format("SELECT* FROM %s WHERE %S.email = ?;", TABLE_NAME,  TABLE_NAME);
    }

    @Override
    protected void fillFindByIdValues(PreparedStatement st, String email) throws SQLException {
        st.setString(1, email);

    }

    protected void fillFindByEmailValues(PreparedStatement st, String email) throws SQLException {
        st.setString(1, email);

    }



    @Override
    protected String getInsertStatement() {
        return String.format("INSERT IGNORE INTO %s( %s ) VALUES(?,?,?,?, ?, ?)", TABLE_NAME, COLUMNS);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, User data) throws SQLException {
        st.setInt(1, 0);
        st.setString(2, data.getUserEmail());
        st.setString(3, data.getPassword());
        st.setString(4, data.getName());
        st.setString(5, data.getNickname());
        st.setString(6, data.getUserBirthDay());
//        st.setArray(6, (Array) data.getActed_in());
    }

    @Override
    protected String getUpdateStatement() {
        return null;
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, User data) throws SQLException {

    }


    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected User convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getString("nickName"),
                rs.getString("birthDate"));
    }

    @Override
    protected ArrayList<User> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        if(rs.isFirst())
            users.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            users.add(this.convertResultSetToDomainModel(rs));
        }
        return users;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, User obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }

    public User findByEmail(String email) throws SQLException {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement(getFindByEmailStatement());
        fillFindByEmailValues(st, email);
        try {
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                st.close();
                con.close();
                return null;
            }
            User result = convertResultSetToDomainModel(resultSet);
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
