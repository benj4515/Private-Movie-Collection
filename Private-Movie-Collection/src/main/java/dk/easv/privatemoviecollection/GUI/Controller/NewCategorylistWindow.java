package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.GUI.Controller.MovieCollectionController;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NewCategorylistWindow {

    @FXML
    private TextField genrelistNameField;
    @FXML
    private TableView<MovieCollection> tblAvailableMovies;
    @FXML
    private TableColumn<MovieCollection, String> colTitle;
    @FXML
    private TableColumn<MovieCollection, String> colRating;
    @FXML
    private TableColumn<MovieCollection, String> colCategory;
    @FXML
    private ListView<MovieCollection> lstSelectedSongs;

    private final ObservableList<MovieCollection> availableMovies = FXCollections.observableArrayList();
    private final ObservableList<MovieCollection> selectedMovies = FXCollections.observableArrayList();

    private MovieCollectionModel movieCollectionModel;
    private MovieCollectionController movieCollectionController;
    private int currentGenreId;

    public void setMovieCollectionModel(MovieCollectionModel model) {
        this.movieCollectionModel = model;
        loadAvailableSongs();
    }

    public void loadPlaylistData(Genre genre, ObservableList<MovieCollection> songs) {
        // this method
        genrelistNameField.setText(genre.getGenre());
        selectedMovies.setAll(songs);
        currentGenreId = genre.getId();
    }

    public void setMovieCollectionController(MovieCollectionController movieCollectionController) {
        this.movieCollectionController = movieCollectionController;
    }

    @FXML
    public void initialize() {
        // this method sets the value of the textfields in the window
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        tblAvailableMovies.setItems(availableMovies);
        lstSelectedSongs.setItems(selectedMovies);
    }

    private void loadAvailableSongs() {
        // This method loads all available songs from the songs list
        try {
            //availableMovies.setAll(MovieCollectionModel.getObservableMovies());
        } catch (Exception e) {
            showError("Error loading songs: " + e.getMessage());
        }
    }

    @FXML
    private void onAddSong() {
        // this button adds the selected song from available song to the made or selected playlist
        MovieCollection selectedMovie = tblAvailableMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            boolean songExists = selectedMovies.stream()
                    .anyMatch(song -> song.getId() == selectedMovie.getId());
            if (!songExists) {
                selectedMovies.add(selectedMovie);
            }
        }
    }

    @FXML
    private void onRemoveSong() {
        // this button removes the selected song from the selected playlist
        MovieCollection selectedMovie = movieCollectionController.selectedMovie();
        if (selectedMovie != null) {
            selectedMovies.remove(selectedMovie);
        }
    }

    @FXML
    private void onSavePlaylist() {
        // this button saves the collection of the playlist to the list and database if new it makes a new playlist if existing it updates with new dataset
        String playlistName = genrelistNameField.getText();
        if (playlistName.isEmpty()) {
            showError("Playlist name is required!");
            return;
        }

        try {
            /*
            Genre existingPlaylist = MovieCollectionModel.getGenrelistById(currentGenreId);
            if (existingPlaylist != null) {
                // Update the existing playlist
                existingPlaylist.setName(playlistName);
                MovieCollectionModel.updateGenrelist(existingGenrelist, selectedMovies);
            } else {
                // Create a new playlist
                MovieCollectionModel.createGenrelist(playlistName, selectedMovies);
            }
            MovieCollectionController.tableRefresh();
            */
            closeWindow();
        } catch (Exception e) {
            showError("Error saving playlist: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        // this closes the window
        Stage stage = (Stage) genrelistNameField.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        // this shows error if any occurs
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onAddMovie(ActionEvent actionEvent) {
    }
}
