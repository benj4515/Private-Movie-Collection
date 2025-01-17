package dk.easv.privatemoviecollection.DAL;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

//this interface serves as a contract for the MovieDataAccess class, and defines most of the primary methods the MovieDataAccess is surposed to be able to do.
public interface IMovieDataAccess {

    //this method gets implemented to get all movies from the database
    List<MovieCollection> getAllMovies() throws Exception;

    //this method gets implemented to create a new movie in the database
    MovieCollection createMovie(MovieCollection newMovie) throws Exception;

    //this method gets implemented to update movies in the database
    void updateMovie(MovieCollection movie) throws Exception;

    //this method gets implemented to delete movies in the database
    void deleteMovie(MovieCollection movie) throws Exception;

    //this method gets implemented to allow for genres to be created
    void createGenre(String genre, List<MovieCollection> selectedMovies) throws Exception;

    //this method gets implemented to allow for genres to be created
    List<Genre> getAllGenres() throws Exception;

    //this method gets implemented to populate the genrelist
    List<MovieCollection> getMoviesForGenre(int genreId) throws Exception;

    //this method gets implemented to update the genre
    void updateGenre(Genre genreName, ObservableList<MovieCollection> movies) throws Exception;
}
