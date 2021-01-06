package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aim.movie.domain.Rating;

public class RatingDao {

    public Rating getById(Connection connection, int id) {
        Rating rating = null;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT rating_id, rating, description from ratings where rating_id = ?");
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                rating = new Rating();
                rating.setId(resultSet.getInt("rating_id"));
                rating.setRating(resultSet.getString("rating"));
                rating.setDescription(resultSet.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rating;
    }

    public Rating getByRating(Connection connection, String rating) {
        Rating record = null;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT rating_id, rating, description from ratings where rating = ?");
            ps.setString(1, rating);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                record = new Rating();
                record.setId(resultSet.getInt("rating_id"));
                record.setRating(resultSet.getString("rating"));
                record.setDescription(resultSet.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }

}