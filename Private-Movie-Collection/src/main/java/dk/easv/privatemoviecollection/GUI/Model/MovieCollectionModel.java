package dk.easv.privatemoviecollection.GUI.Model;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.BLL.MovieCollectionManager;
import dk.easv.privatemoviecollection.DAL.MovieCollectionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class MovieCollectionModel {

    private final ObservableList<MovieCollection> moviesToBeViewed;

    private final MovieCollectionManager movieCollectionManager;

    private final MovieCollectionDAO movieCollectionDAO = new MovieCollectionDAO();

    public MovieCollectionModel() throws Exception {
        movieCollectionManager = new MovieCollectionManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieCollectionManager.getAllMovies());
    }
    public ObservableList<MovieCollection> getObservableMovies() { return moviesToBeViewed; }
}
