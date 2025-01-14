package dk.easv.privatemoviecollection.GUI.Model;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.BLL.MovieCollectionManager;
import dk.easv.privatemoviecollection.DAL.MovieCollectionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;


public class MovieCollectionModel {

    private final ObservableList<MovieCollection> moviesToBeViewed;

    private final MovieCollectionManager movieCollectionManager;

    private final MovieCollectionDAO movieCollectionDAO = new MovieCollectionDAO();


    public MovieCollectionModel() throws Exception {
        movieCollectionManager = new MovieCollectionManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieCollectionManager.getAllMovies());
    }

    public ObservableList<MovieCollection> getObservableMovies() {
        return moviesToBeViewed;
    }

    public void createMovie(MovieCollection newMovie) throws Exception {
        MovieCollection m = null;
        try {
            m = movieCollectionManager.createMovie(newMovie);
            moviesToBeViewed.add(m);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public void refreshMovies() throws Exception {
        // this refreshes the tableview with the method from myTunesManager
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(movieCollectionManager.getAllMovies());
    }

    public void searchMovies(String query) throws Exception {

        List<MovieCollection> searchResults = movieCollectionManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResults);
    }

    public void updateMovie(MovieCollection updatedMovie) throws Exception {
        try {
            movieCollectionManager.updateMovie(updatedMovie);

            //Find the movie in the observable list and update it
            int index = moviesToBeViewed.indexOf(updatedMovie);

            if (index != -1) {
                //Match, update movie
                MovieCollection m = moviesToBeViewed.get(index);
                m.setName(updatedMovie.getName());
                m.setRating(updatedMovie.getRating());
                m.setGenre(updatedMovie.getGenre());
                m.setPath(updatedMovie.getPath());
                m.setLastviewed(updatedMovie.getLastviewed());
                m.setDuration(updatedMovie.getDuration());

            } else {
                //No match, throw exception
                throw new Exception("Movie not found in the list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(MovieCollection selectedMovie) throws Exception {
        //delete movie from database
        movieCollectionManager.deleteMovie(selectedMovie);
        //delete movie from observable list
        moviesToBeViewed.remove(selectedMovie);
    }

    public ObservableList<Genre> getAllGenres() throws Exception{

        List<Genre> genres = movieCollectionManager.getAllGenres();
        return FXCollections.observableArrayList(genres);
    }

    public void createGenre(String genreName) throws Exception {
        movieCollectionManager.createGenre(genreName);
    }

    public Genre getGenreById(int id) throws Exception {
        List<Genre> genres = movieCollectionManager.getAllGenres();
        for (Genre g : genres) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }



}