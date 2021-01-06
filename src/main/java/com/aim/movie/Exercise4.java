package com.aim.movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.aim.movie.dao.ActorDao;
import com.aim.movie.dao.DirectorDao;
import com.aim.movie.dao.MovieActorsDao;
import com.aim.movie.dao.MovieDao;
import com.aim.movie.domain.Actor;
import com.aim.movie.domain.Director;
import com.aim.movie.domain.Movie;
import com.aim.movie.util.MySQL;

import org.apache.commons.lang3.StringUtils;

public class Exercise4 {

    public static void main(String[] args) {
        Scanner input = null;

        try (Connection connection = DriverManager.getConnection(MySQL.URL.value, MySQL.USER.value, MySQL.PASS.value)) {

            input = new Scanner(System.in);
            String movieTitle = getMovieTitleFromUser(input);

            MovieDao movieDao = new MovieDao();
            Movie movie = movieDao.getMovie(connection, movieTitle);

            if (movie != null) {
                System.out.format("%n%-30s %-30s %-30s %-30s %n", "Movie Title", "Director/Actors", "Genre", "Rating");
                System.out.format("%-30s %-30s %-30s %-30s %n", "-----------", "---------------", "-----", "------");
                System.out.format("%-30s ", StringUtils.abbreviate(movie.getMovieTitle(), 25));
                System.out.format("%-30s ", movie.getDirector().getFullName());
                System.out.format("%-30s ", movie.getGenre());
                System.out.format("%-30s %n", movie.getRating());

                for (Actor actor : movie.getActors()) {
                    System.out.format("%30s %-30s %n", " ", actor.getFullName());
                }
            } else {
                if (askUserToAddMovie(input)) {
                    Movie newMovie = getMovie(input, movieTitle);
                    createMovie(connection, newMovie);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Program failed.");
        } finally {
            input.close();
            System.out.println("\nGoodbye ...");
        }
    }

    public static String getMovieTitleFromUser(Scanner input) {
        System.out.println("\nThis program displays the Director, Genre and Rating of a movie.\n");
        System.out.print("Please enter a movie title: ");
        String movieTitle = input.nextLine();
        return movieTitle;
    }

    public static boolean askUserToAddMovie(Scanner input) {
        System.out.print("\nThis movie does not exist. Would you like to add movie (y/n)? ");

        try {

            String addMovie = input.nextLine();
            if (addMovie.equalsIgnoreCase("y")) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static Movie getMovie(Scanner input, String movieTitle) {

        Movie movie = new Movie();

        System.out.println("Please enter the information for the movie.");
        System.out.println("Movie title (" + movieTitle + ")" + ": ");
        movie.setMovieTitle(input.nextLine());

        System.out.print("Movie length: ");
        movie.setMovieLength(Integer.parseInt(input.nextLine()));

        System.out.print("Release Date (mm/dd/yyyy): ");
        String releaseDate = input.nextLine();
        movie.setReleaseDate(getFormattedDate(releaseDate));

        System.out.print("Genre: ");
        movie.setGenre(input.nextLine());

        System.out.print("Rating (G, PG, PG-13, R): ");
        movie.setRating(input.nextLine());

        System.out.print("Director: ");
        Director director = new Director();
        director.setFullName(input.nextLine());
        movie.setDirector(director);

        System.out.print("Actor: ");
        Actor actor = new Actor();
        actor.setFullName(input.nextLine());
        List<Actor> actors = new ArrayList<>();
        actors.add(actor);
        movie.setActors(actors);

        System.out.println("\nMovie: " + movie);
        return movie;
    }

    public static void createMovie(Connection connection, Movie movie) {

        // Create Director - getId
        DirectorDao directorDao = new DirectorDao();
        Director director = directorDao.insert(connection, movie.getDirector());
        movie.setDirector(director);

        // Create Actor - getId
        ActorDao actorDao = new ActorDao();
        List<Actor> actors = new ArrayList<>();
        for (Actor actor : movie.getActors()) {
            actors.add(actorDao.insert(connection, actor));
        }
        movie.setActors(actors);

        // Create Movie
        MovieDao movieDao = new MovieDao();
        movie = movieDao.insert(connection, movie);

        // Create Relationship between Movies and Actors
        MovieActorsDao movieActorsDao = new MovieActorsDao();
        for (Actor actor : actors) {
            movieActorsDao.insert(connection, movie.getId(), actor.getId());
        }

    }

    public static Date getFormattedDate(String mmddyyyy) {
        return new Date();
    }

}
