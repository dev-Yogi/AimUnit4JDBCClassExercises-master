package com.aim.movie.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.aim.movie.domain.Actor;
import com.aim.movie.domain.Director;
import com.aim.movie.domain.Movie;

public class MovieDao {

    ActorDao actorDao = new ActorDao();
    RatingDao ratingDao = new RatingDao();
    GenreDao genreDao = new GenreDao();

    public ResultSet getMovieResultSet(Connection connection, String movieTitle) {
        StringBuilder sql = new StringBuilder();
        try {

            sql.append("select m.movie_name, m.movie_length, m.release_date, ");
            sql.append("d.first_name, d.last_name, r.rating, g.genre ");
            sql.append("from movies m ");
            sql.append("join directors d on d.director_id = m.director_id ");
            sql.append("join ratings r on r.rating_id = m.rating_id ");
            sql.append("join genres g on g.genre_id = m.genre_id ");
            sql.append("where m.movie_name like '" + movieTitle + "%';");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql.toString());
            return resultSet;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nSQL (copy the SQL statement below and run it if you're having problems): \n"
                    + sql.toString() + "\n");
        }
        return null;
    }

    public Movie getMovie(Connection connection, String movieTitle) {
        Movie movie = null;

        try {

            ResultSet resultSet = getMovieResultSet(connection, movieTitle);

            if (resultSet != null && resultSet.next()) {
                movie = new Movie();

                do {
                    movie.setMovieTitle(resultSet.getString("m.movie_name"));
                    movie.setMovieLength(resultSet.getInt("m.movie_length"));
                    movie.setReleaseDate(resultSet.getDate("m.release_date"));
                    movie.setGenre(resultSet.getString("g.genre"));
                    movie.setRating(resultSet.getString("r.rating"));

                    // Since movie has a Director object as a member variable, lets create one
                    Director director = new Director();
                    director.setFirstName(resultSet.getString("d.first_name"));
                    director.setLastName(resultSet.getString("d.last_name"));

                    // Now add the director object to the movie object
                    movie.setDirector(director);

                    List<Actor> actors = actorDao.getActorsByMovieTitle(connection, movie.getMovieTitle());
                    movie.setActors(actors);

                } while (resultSet.next());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public Movie insert(Connection connection, Movie movie) {

        try {

            int ratingId = ratingDao.getByRating(connection, movie.getRating()).getId();
            int genreId = genreDao.getByGenre(connection, movie.getGenre()).getId();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO movies (movie_name, movie_length, release_date, director_id, rating_id, genre_id) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, movie.getMovieTitle());
            ps.setInt(2, movie.getMovieLength());
            ps.setDate(3, new Date(movie.getReleaseDate().getTime()));
            ps.setInt(4, movie.getDirector().getId());
            ps.setInt(5, ratingId);
            ps.setInt(6, genreId);

            int i = ps.executeUpdate();
            if (i == 1) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID();");
                if (resultSet.next()) {
                    movie.setId(resultSet.getInt(1));
                }
                return movie;
            } else {
                return getMovie(connection, movie.getMovieTitle());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return movie;
    }
}