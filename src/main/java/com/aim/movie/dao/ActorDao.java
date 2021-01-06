package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aim.movie.domain.Actor;

public class ActorDao {

    public Actor getByName(Connection connection, String firstName, String lastName) {
        Actor actor = null;
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT actor_id, first_name, last_name, date_of_birth from actors where first_name = ? and last_name = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                actor = new Actor();
                actor.setId(resultSet.getInt("director_id"));
                actor.setFirstName(resultSet.getString("first_name"));
                actor.setLastName(resultSet.getString("last_name"));
                actor.setDateOfBirth(resultSet.getDate("date_of_birth"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actor;
    }

    public List<Actor> getActorsByMovieTitle(Connection connection, String movieTitle) {
        List<Actor> actors = new ArrayList<>();

        StringBuilder actorSQL = new StringBuilder();
        actorSQL.append("select a.first_name, a.last_name ");
        actorSQL.append("from actors a ");
        actorSQL.append("join movie_actors ma on ma.actor_id = a.actor_id ");
        actorSQL.append("join movies m on m.movie_id = ma.movie_id ");
        actorSQL.append("where m.movie_name = '" + movieTitle + "';");

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(actorSQL.toString());

            if (resultSet.next()) {

                do {
                    Actor actor = new Actor();
                    actor.setFirstName(resultSet.getString("first_name"));
                    actor.setLastName(resultSet.getString("last_name"));
                    actors.add(actor);
                } while (resultSet.next());

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nSQL (copy the SQL statement below and run it if you're having problems): \n"
                    + actorSQL.toString() + "\n");
        }

        return actors;

    }

    public Actor insert(Connection connection, Actor actor) {

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT IGNORE INTO actors (first_name, last_name) VALUES (?, ?)");
            ps.setString(1, actor.getFirstName());
            ps.setString(2, actor.getLastName());
            int i = ps.executeUpdate();
            if (i == 1) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                if (resultSet.next()) {
                    actor.setId(resultSet.getInt(1));
                }
                return actor;
            } else {
                return getByName(connection, actor.getFirstName(), actor.getLastName());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return actor;
    }
}