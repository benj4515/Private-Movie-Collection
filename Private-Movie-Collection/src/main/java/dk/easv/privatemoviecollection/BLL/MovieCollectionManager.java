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


public class MovieCollectionManager {
    private final IMovieDataAccess dataAccess;
    private final MovieSearcher movieSearcher = new MovieSearcher();

    public MovieCollectionManager() throws IOException {
        dataAccess = new MovieCollectionDAO();
    }


    public List<MovieCollection> getAllMovies() throws Exception {
        return dataAccess.getAllMovies();
    }

    public MovieCollection createMovie(MovieCollection newMovie) throws Exception {
        return dataAccess.createMovie(newMovie);
    }

    public List<MovieCollection> searchMovies(String query) throws Exception {
        List<MovieCollection> allMovies = getAllMovies();
        return movieSearcher.search(allMovies, query);
    }

    public void updateMovie(MovieCollection updatedMovie) throws Exception {
        dataAccess.updateMovie(updatedMovie);
    }

    public void deleteMovie(MovieCollection selectedMovie) throws Exception {
        dataAccess.deleteMovie(selectedMovie);
    }

    public List<Genre> getAllGenres() throws Exception {
        return dataAccess.getAllGenres();
    }

    public void createGenre(String genreName, ObservableList<MovieCollection> selectedMovies) throws Exception {
        dataAccess.createGenre(genreName, selectedMovies);
    }

    public void updateGenre(Genre genreName, ObservableList<MovieCollection> movies) throws Exception {
        dataAccess.updateGenre(genreName, movies);
    }

    public List<MovieCollection> getMoviesForGenre(int genreId) throws Exception {
        return dataAccess.getMoviesForGenre(genreId);
    }

    public List<MovieCollection> checkIfOldShit() throws Exception {
        List<MovieCollection> allMovies = getAllMovies();
        LocalDate twoYearsAgold = LocalDate.now().minusYears(2);
        Date twoYearsAgo = Date.valueOf(twoYearsAgold);
        List<MovieCollection> oldBadMovies = new ArrayList<>();
        StringBuilder message = new StringBuilder();

        for (MovieCollection movie : allMovies) {
            boolean isOld = movie.getLastviewed().before(twoYearsAgo);
            boolean isBad = movie.getRating() <= 6;
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
        if (!oldBadMovies.isEmpty()) {
            throw new Exception("This movie is either old or bad: \n" + message);
        }

        return oldBadMovies;
    }
}
