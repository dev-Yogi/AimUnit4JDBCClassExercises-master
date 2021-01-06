package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieActorsDao {
    public boolean insert(Connection connection, int movieId, int actorId) {

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT IGNORE INTO movie_actors (movie_id, actor_id) VALUES (?, ?)");
            ps.setInt(1, movieId);
            ps.setInt(2, actorId);
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}