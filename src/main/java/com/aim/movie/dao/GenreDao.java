package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.aim.movie.domain.Genre;

public class GenreDao {
    public Genre getById(Connection connection, int id) {
        Genre genre = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT genre_id, genre from genres where genre_id = ?");
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                genre = new Genre();
                genre.setId(resultSet.getInt("genre_id"));
                genre.setGenre(resultSet.getString("genre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genre;
    }

    public Genre getByGenre(Connection connection, String genre) {
        Genre record = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT genre_id, genre from genres where genre = ?");
            ps.setString(1, genre);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                record = new Genre();
                record.setId(resultSet.getInt("genre_id"));
                record.setGenre(resultSet.getString("genre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }
}