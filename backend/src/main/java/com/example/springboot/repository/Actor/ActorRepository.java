package com.example.springboot.repository.Actor;

import com.example.springboot.imedb.Actor;
import com.example.springboot.repository.ConnectionPool;
import com.example.springboot.repository.Repository;

import java.sql.*;
import java.util.ArrayList;

public class ActorRepository extends Repository<Actor, Integer> {
    private static final String COLUMNS = " id, name, birthDate, nationality, image";
    private static final String TABLE_NAME = "ACTORS";
    private static ActorRepository instance;

    public static ActorRepository getInstance() {
        if (instance == null) {
            try {
                instance = new ActorRepository();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("error in ActorRepository.create query.");
            }
        }
        return instance;
    }

    public ActorRepository() throws SQLException {

        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        int createTableStatement = st.executeUpdate(
                String.format("CREATE TABLE IF NOT EXISTS %s(id int(50),\nname CHAR(225),\nbirthDate CHAR(255) ,\nnationality CHAR(225), \nimage CHAR(225),\nPRIMARY KEY(id));", TABLE_NAME)
        );
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
        return String.format("INSERT IGNORE INTO %s( %s ) VALUES(?,?,?, ?, ?)", TABLE_NAME, COLUMNS);
    }

    @Override
    protected void fillInsertValues(PreparedStatement st, Actor data) throws SQLException {
        st.setInt(1, data.getId());
        st.setString(2, data.getName());
        st.setString(3, data.getBirthDate());
        st.setString(4, data.getNationality());
        st.setString(5, data.getImage());
    }

    @Override
    protected String getUpdateStatement() {
        return String.format("Update %s Set name=?, birthDate=?, nationality=?, image=? where id=?", TABLE_NAME);
    }

    @Override
    protected void fillUpdateValues(PreparedStatement st, Actor data) throws SQLException {
        st.setString(1, data.getName());
        st.setString(2, data.getBirthDate());
        st.setString(3, data.getNationality());
        st.setString(4, data.getImage());
        st.setInt(5, data.getId());
    }


    @Override
    protected String getFindAllStatement() {
        return String.format("SELECT * FROM %s;", TABLE_NAME);
    }

    @Override
    protected Actor convertResultSetToDomainModel(ResultSet rs) throws SQLException {
        return new Actor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
    }

    @Override
    protected ArrayList<Actor> convertResultSetToDomainModelList(ResultSet rs) throws SQLException {
        ArrayList<Actor> actors = new ArrayList<>();
        if(rs.isFirst())
            actors.add(this.convertResultSetToDomainModel(rs));
        while (rs.next()) {
            actors.add(this.convertResultSetToDomainModel(rs));
        }
        return actors;
    }

    @Override
    protected void fillDeleteValues(PreparedStatement st, Actor obj) {

    }

    @Override
    protected String getDeleteStatement() {
        return null;
    }


}
