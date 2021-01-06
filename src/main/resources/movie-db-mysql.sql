CREATE DATABASE IF NOT EXISTS movie;
USE movie;

-- Since movie contains foreign key to directors, the movie table must be dropped before directors
DROP TABLE IF EXISTS movies;

DROP TABLE IF EXISTS directors;
CREATE TABLE directors (
	-- director_id SERIAL PRIMARY KEY,
	director_id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	date_of_birth DATE
);
insert into directors (first_name, last_name, date_of_birth) values ('James', 'Cameron', '1954-08-16');
insert into directors (first_name, last_name, date_of_birth) values ('J.J.', 'Abrams', '1966-06-27');
insert into directors (first_name, last_name, date_of_birth) values ('Stephen', 'Spielberg', '1946-12-18');
insert ignore into directors (first_name, last_name, date_of_birth) values ('James', 'Cameron', '1954-08-16');

DROP TABLE IF EXISTS ratings;
CREATE TABLE ratings (
	rating_id INT AUTO_INCREMENT PRIMARY KEY,
	rating VARCHAR(5) NOT NULL,
	description VARCHAR(50) NOT NULL
);
insert into ratings (rating, description) values ('G', 'General Audiences');
insert into ratings (rating, description) values ('PG', 'Parental Guidance Suggested');
insert into ratings (rating, description) values ('PG-13', 'Parents Strongly Cautioned');
insert into ratings (rating, description) values ('R', 'Restricted');

DROP TABLE IF EXISTS genres;
CREATE TABLE genres (
	genre_id INT AUTO_INCREMENT PRIMARY KEY,
	genre VARCHAR(25) NOT NULL
);
insert into genres (genre) values ('Action');
insert into genres (genre) values ('Animation');
insert into genres (genre) values ('Comedy');
insert into genres (genre) values ('Sci-Fi');
insert into genres (genre) values ('Drama, Sci-Fi');

DROP TABLE IF EXISTS actors;
CREATE TABLE actors (
	actor_id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	date_of_birth DATE
);
insert into actors (first_name, last_name, date_of_birth) values ('Sam', 'Worthington', '1976-08-02');
insert into actors (first_name, last_name, date_of_birth) values ('Zoe', 'Saldana', '1978-06-19');
insert into actors (first_name, last_name, date_of_birth) values ('Sigourney', 'Weaver', '1949-10-08');
insert into actors (first_name, last_name, date_of_birth) values ('John', 'Cho', '1972-06-16');
insert into actors (first_name, last_name, date_of_birth) values ('Chris', 'Pine', '1980-08-26');
insert into actors (first_name, last_name, date_of_birth) values ('Richard', 'Dreyfuss', '1947-10-29');

CREATE TABLE movies (
	movie_id INT AUTO_INCREMENT PRIMARY KEY,
	director_id INT,
    rating_id INT NOT NULL,
    genre_id INT NOT NULL,
	movie_name VARCHAR(50) NOT NULL,
	movie_length INT,
	release_date DATE,
	FOREIGN KEY (director_id) REFERENCES directors (director_id)
);

INSERT INTO movies (movie_name, movie_length, release_date, director_id, rating_id, genre_id)
values ('Avatar', 162, '2009-12-18', 1, 3, 3);
INSERT INTO movies (movie_name, movie_length, release_date, director_id, rating_id, genre_id)
values ('Star Trek', 127, '2009-05-08', 2, 3, 3);
INSERT INTO movies (movie_name, movie_length, release_date, director_id, rating_id, genre_id)
values ('Close Encounters of the Third Kind', 135, '1977-11-16', 3, 2, 4);

DROP TABLE IF EXISTS movie_actors;
CREATE TABLE movie_actors (
	id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    actor_id INT NOT NULL
);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (1, 1);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (1, 2);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (1, 3);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (2, 3);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (2, 4);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (2, 5);
INSERT INTO movie_actors (movie_id, actor_id) VALUES (3, 6);
