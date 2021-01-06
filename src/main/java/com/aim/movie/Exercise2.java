package com.aim.movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.aim.movie.domain.Actor;
import com.aim.movie.domain.Director;
import com.aim.movie.domain.Movie;
import com.aim.movie.util.MySQL;

public class Exercise2 {

    private static StringBuilder sql = null;

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(MySQL.URL.value, MySQL.USER.value, MySQL.PASS.value)) {

            String movieTitle = getMovieTitleFromUser();

            Movie movie = getMovie(connection, movieTitle);

            if (movie != null) {
                System.out.format("%n%-30s %-30s %-30s %-30s %n", "Movie Title", "Director/Actors", "Genre", "Rating");
                System.out.format("%-30s %-30s %-30s %-30s %n", "-----------", "---------------", "-----", "------");
                System.out.format("%-30s ", movie.getMovieTitle());
                System.out.format("%-30s ", movie.getDirector().getFullName());
                System.out.format("%-30s ", movie.getGenre());
                System.out.format("%-30s %n", movie.getRating());

                for (Actor actor : movie.getActors()) {
                    System.out.format("%30s %-30s %n", " ", actor.getFullName());
                }

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

            sql = new StringBuilder();
            sql.append("select m.movie_name, m.movie_length, m.release_date, ");
            sql.append("d.first_name, d.last_name, r.rating, g.genre ");
            sql.append("from movies m ");
            sql.append("join directors d on d.director_id = m.director_id ");
            sql.append("join ratings r on r.rating_id = m.rating_id ");
            sql.append("join genres g on g.genre_id = m.genre_id ");
            sql.append("where m.movie_name = '" + movieTitle + "';");

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

    public static Movie getMovie(Connection connection, String movieTitle) {
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

                    List<Actor> actors = getActors(connection, movieTitle);
                    movie.setActors(actors);

                } while (resultSet.next());

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nSQL (copy the SQL statement below and run it if you're having problems): \n"
                    + sql.toString() + "\n");
        }
        return movie;
    }

    public static List<Actor> getActors(Connection connection, String movieTitle) {
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

}
