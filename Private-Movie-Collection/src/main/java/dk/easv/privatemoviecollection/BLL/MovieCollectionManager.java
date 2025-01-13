package dk.easv.privatemoviecollection.BLL;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.io.IOException;
import java.util.List;

import dk.easv.privatemoviecollection.BLL.Util.MovieSearcher;
import dk.easv.privatemoviecollection.DAL.IMovieDataAccess;
import dk.easv.privatemoviecollection.DAL.MovieCollectionDAO;



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
}
