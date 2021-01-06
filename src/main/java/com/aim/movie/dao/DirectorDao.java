package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.aim.movie.domain.Director;

public class DirectorDao {

    public Director getByName(Connection connection, String firstName, String lastName) {
        Director director = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement
                    .executeQuery("SELECT director_id, first_name, last_name, date_of_birth from directors");
            if (resultSet.next()) {
                director = new Director();
                director.setId(resultSet.getInt("director_id"));
                director.setFirstName(resultSet.getString("first_name"));
                director.setLastName(resultSet.getString("last_name"));
                director.setDateOfBirth(resultSet.getDate("date_of_birth"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return director;
    }

    public Director insert(Connection connection, Director director) {

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT IGNORE INTO directors (first_name, last_name) VALUES (?, ?)");
            ps.setString(1, director.getFirstName());
            ps.setString(2, director.getLastName());
            int i = ps.executeUpdate();
            if (i == 1) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                if (resultSet.next()) {
                    director.setId(resultSet.getInt(1));
                }
                return director;
            } else {
                return getByName(connection, director.getFirstName(), director.getLastName());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return director;
    }

}