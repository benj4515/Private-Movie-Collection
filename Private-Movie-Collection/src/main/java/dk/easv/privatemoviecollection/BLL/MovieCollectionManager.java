package dk.easv.privatemoviecollection.BLL;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dk.easv.privatemoviecollection.BLL.Util.MovieSearcher;
import dk.easv.privatemoviecollection.DAL.IMovieDataAccess;
import dk.easv.privatemoviecollection.DAL.MovieCollectionDAO;
import javafx.collections.ObservableList;



// This class is used to manage the movie collection
public class MovieCollectionManager {

   // This class has a private instance of IMovieDataAccess and a MovieSearcher
    private final IMovieDataAccess dataAccess;
    private final MovieSearcher movieSearcher = new MovieSearcher();

    // This constructor creates a new instance of MovieCollectionDAO
    public MovieCollectionManager() throws IOException {
        dataAccess = new MovieCollectionDAO();
    }


    // This method gets all movies from the data access
    public List<MovieCollection> getAllMovies() throws Exception {
        return dataAccess.getAllMovies();
    }

    // This method creates a new movie in the data access
    public MovieCollection createMovie(MovieCollection newMovie) throws Exception {
        return dataAccess.createMovie(newMovie);
    }

    // This method searches for movies in the searchbar
    public List<MovieCollection> searchMovies(String query) throws Exception {
        List<MovieCollection> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    // This method updates a movie in the data access
    public void updateMovie(MovieCollection updatedMovie) throws Exception {
        dataAccess.updateMovie(updatedMovie);
    }

    // This method deletes a movie in the data access
    public void deleteMovie(MovieCollection selectedMovie) throws Exception {
        dataAccess.deleteMovie(selectedMovie);
    }

    // This method gets all genres from the data access
    public List<Genre> getAllGenres() throws Exception {
        return dataAccess.getAllGenres();
    }

    //this method creates a new list of movies with a selected genre
    public void createGenre(String genreName, ObservableList<MovieCollection> selectedMovies) throws Exception {
        dataAccess.createGenre(genreName, selectedMovies);
    }

    //this method updates the genrelist
    public void updateGenre(Genre genreName, ObservableList<MovieCollection> movies) throws Exception {
        dataAccess.updateGenre(genreName, movies);
    }

    //this method gets all movies with a given genre
    public List<MovieCollection> getMoviesForGenre(int genreId) throws Exception {
        return dataAccess.getMoviesForGenre(genreId);
    }

    //this method checks if there is any old or poorly rated movies within the collection
    public List<MovieCollection> checkIfOldShit() throws Exception {

        //create a list of all movies, a date two years ago and a list of old and bad movies and a stringbuilder for the message
        List<MovieCollection> allMovies = getAllMovies();
        LocalDate twoYearsAgold = LocalDate.now().minusYears(2);
        Date twoYearsAgo = Date.valueOf(twoYearsAgold);
        List<MovieCollection> oldBadMovies = new ArrayList<>();
        StringBuilder message = new StringBuilder();

        //loop through all movies and check if they are old or bad
        for (MovieCollection movie : allMovies) {
            boolean isOld = movie.getLastviewed().before(twoYearsAgo);
            boolean isBad = movie.getRating() <= 6;

            //if the movie is old or bad, add it to the list and append the message
            if (isOld || isBad) {
                oldBadMovies.add(movie);
                message.append("Movie: ").append(movie.getName()).append(" - ");
            }
            if (isOld) {
                message.append("Unseen for Two Years ");
            }
            if (isBad) {
                message.append("Rating is 6 or lower ");
            }
            message.append("\n");
        }

        //if there are any old or bad movies, throw an exception with the message
        if (!oldBadMovies.isEmpty()) {
            throw new Exception("This movie is either old or bad: \n" + message);
        }

        return oldBadMovies;
    }
}
