package com.aim.movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.aim.movie.domain.Director;
import com.aim.movie.domain.Movie;
import com.aim.movie.util.MySQL;

public class Exercise1 {

    private static StringBuilder sql = null;

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(MySQL.URL.value, MySQL.USER.value, MySQL.PASS.value)) {

            String movieTitle = getMovieTitleFromUser();

            ResultSet resultSet = getMovieResultSet(connection, movieTitle);
            if (resultSet == null) {
                System.out.println("Program darn messed up. Goodbye ...");
                System.exit(-1);
            }

            Movie movie = getMovie(resultSet);

            if (movie != null) {
                System.out.format("%n%-30s  %-30s %-30s %-30s %n", "Movie Title", "Director", "Genre", "Rating");
                System.out.format("%-30s  %-30s %-30s %-30s %n", "----------", "--------", "-----", "------");
                System.out.format("%-31s ", movie.getMovieTitle());
                System.out.format("%-30s ", movie.getDirector().getFullName());
                System.out.format("%-30s ", movie.getGenre());
                System.out.format("%-30s %n", movie.getRating());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection failure.");
        } finally {
            System.out.println("\nGoodbye ...");
        }
    }

    public static String getMovieTitleFromUser() {
        System.out.println("\nThis program displays the Director, Genre and Rating of a movie.\n");
        System.out.print("Please enter a movie title: ");

        Scanner input = new Scanner(System.in);
        String movieTitle = input.nextLine();
        input.close();
        return movieTitle;
    }

    public static ResultSet getMovieResultSet(Connection connection, String movieTitle) {
        try {

            Statement statement = connection.createStatement();
            sql = new StringBuilder();
            sql.append("select m.movie_name, m.movie_length, m.release_date, ");
            sql.append("d.first_name, d.last_name, r.rating, g.genre ");
            sql.append("from movies m ");
            sql.append("join directors d on d.director_id = m.director_id ");
            sql.append("join ratings r on r.rating_id = m.rating_id ");
            sql.append("join genres g on g.genre_id = m.genre_id ");
            sql.append("where m.movie_name = '" + movieTitle + "';");

            ResultSet resultSet = statement.executeQuery(sql.toString());
            return resultSet;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\nSQL (copy the SQL statement below and run it if you're having problems): \n"
                    + sql.toString() + "\n");
        }
        return null;
    }

    public static Movie getMovie(ResultSet resultSet) {
        Movie movie = null;

        try {

            if (resultSet.next()) {
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
                } while (resultSet.next());

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nSQL (copy the SQL statement below and run it if you're having problems): \n"
                    + sql.toString() + "\n");
        }
        return movie;
    }

}
