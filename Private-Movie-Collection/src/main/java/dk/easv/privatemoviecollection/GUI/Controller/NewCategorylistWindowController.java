package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NewCategorylistWindowController {




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
    private ListView<MovieCollection> lstSelectedMovies;

    private final ObservableList<MovieCollection> availableMovies = FXCollections.observableArrayList();
    private final ObservableList<MovieCollection> selectedMovies = FXCollections.observableArrayList();

    private MovieCollectionModel movieCollectionModel;
    private MovieCollectionController movieCollectionController;
    private int currentGenreId;

    public void setMovieCollectionModel(MovieCollectionModel model) {
        this.movieCollectionModel = model;
        loadAvailableMovies();
    }

    public void loadGenreData(Genre genre, ObservableList<MovieCollection> movies) {
        // this method
        genrelistNameField.setText(genre.getGenre());
        selectedMovies.setAll(movies);
        currentGenreId = genre.getId();
    }

    public void setMovieCollectionController(MovieCollectionController movieCollectionController) {
        this.movieCollectionController = movieCollectionController;
    }

    @FXML
    public void initialize() {
        // this method sets the value of the textfields in the window
        colTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("genre"));

        tblAvailableMovies.setItems(availableMovies);
        lstSelectedMovies.setItems(selectedMovies);
    }

    private void loadAvailableMovies() {
        // This method loads all available songs from the songs list
        try {
            availableMovies.setAll(movieCollectionModel.getObservableMovies());
        } catch (Exception e) {
            showError("Error loading songs: " + e.getMessage());
        }
    }

    @FXML
    private void onAddMovie() {
        // this button adds the selected song from available song to the made or selected playlist
        MovieCollection selectedMovie = tblAvailableMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            boolean movieExists = selectedMovies.stream()
                    .anyMatch(movie -> movie.getId() == selectedMovie.getId());
            if (!movieExists) {
                selectedMovies.add(selectedMovie);
            }
        }
    }

    @FXML
    private void onRemoveSong() {
        // this button removes the selected song from the selected playlist
        MovieCollection selectedMovie = lstSelectedMovies.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            selectedMovies.remove(selectedMovie);
        }
    }

    @FXML
    private void onSavePlaylist() {
        // this button saves the collection of the playlist to the list and database if new it makes a new playlist if existing it updates with new dataset
        String genreName = genrelistNameField.getText();
        if (genreName.isEmpty()) {
            showError("Genre name is required!");
            return;
        }

        try {

            Genre existingGenrelist = movieCollectionModel.getGenreById(currentGenreId);
            if (existingGenrelist != null) {
                // Update the existing playlist
                existingGenrelist.setGenre(genreName);
                movieCollectionModel.updateGenre(existingGenrelist, selectedMovies);
            } else {
                // Create a new playlist
                movieCollectionModel.createGenre(genreName, selectedMovies);
            }
            movieCollectionController.tableRefresh();

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


}
