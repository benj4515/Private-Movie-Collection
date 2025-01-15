package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class MovieCollectionController implements Initializable {
    public TableView<MovieCollection> tblMovies;
    public TableColumn<MovieCollection, String> colMovie;
    public TableColumn<MovieCollection, String> colGenre;
    public TableColumn<MovieCollection, String> colDuration;
    public TableColumn<MovieCollection, String> colLastViewed;
    public TableColumn<MovieCollection, String> colRating;
    public Button btnDeleteMovie;
    public Button btnAddGenre;
    private MovieCollectionModel movieCollectionModel;
    @FXML
    private TextField txtSearchMovie;
    @FXML
    private TextField txtSearchGenre;
    @FXML
    private TableView<Genre> tblGenre;
    public TableColumn<Genre, String> colCat;
    @FXML
    private MediaView mdpPlayer;
    @FXML
    private StackPane stackPlayer;


    public MovieCollectionController() {

        try {
            movieCollectionModel = new MovieCollectionModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
        System.out.println(movieCollectionModel.getObservableMovies());
        //initialize( movieCollectionManager);
    }

    private void displayError(Exception e) {
        // displays if any error occours
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        colMovie.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastviewed"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // Bind the fitWidth and fitHeight properties of the MediaView to the HBox
        mdpPlayer.fitWidthProperty().bind(stackPlayer.widthProperty());
        mdpPlayer.fitHeightProperty().bind(stackPlayer.heightProperty());

        tblMovies.setItems(movieCollectionModel.getObservableMovies());

        txtSearchMovie.textProperty().addListener((_, _, newValue) -> {
            try {
                movieCollectionModel.searchMovies(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });

        try {
            tblGenre.setItems(movieCollectionModel.getAllGenres());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private NewMovieWindowController onNewMovieButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/NewMovieWindow.fxml"));

            Parent scene = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.setTitle("Add movie");

            // Get the controller reference
            NewMovieWindowController controller = loader.getController();

            // Send a reference to the parent to MyTunesController
            controller.setParent(this); // this refers to this MovieCollectionController object

            // Set the modality to Application (you must close "Add movie" before going to the parent window
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
        return null;
    }

    @FXML
    private void onDeleteMovieButtonClick(ActionEvent actionEvent) throws Exception {
        //Confirmation dialog
        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this movie?", "Delete movie", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            // Get the selected movie
            MovieCollection selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                //delete the movie
                movieCollectionModel.deleteMovie(selectedMovie);
                System.out.println("Movie deleted");
                tableRefresh();
            } else {
                JOptionPane.showMessageDialog(null, "Please select a movie to delete", "No movie selected", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void tableRefresh() {
        // this method updates the tableview with songs with latest dataset
        System.out.println("tableRefresh called");
        try {
            movieCollectionModel.refreshMovies();
        } catch (Exception e) {
            displayError(e);
        }
        ObservableList<MovieCollection> MovieCollection = movieCollectionModel.getObservableMovies();
        System.out.println("Number of Movies: " + MovieCollection.size());
        tblMovies.setItems(null); // Clear the table
        tblMovies.setItems(MovieCollection); // Reset the items
        tblMovies.refresh(); // Refresh the table
    }

    @FXML
    private NewCategoryController onNewGenreButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/NewCategoryWindow.fxml"));

            Parent scene = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.setTitle("Add Genre");

            NewCategoryController controller = loader.getController();

            controller.setParent(this);


            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
        return null;
    }

    @FXML
    private void onPlayButtonClick(ActionEvent actionEvent) {
        try {
            String filePath = "file:///C:/Users/benja/Videos/Movies/28_YEARS_LATER_Official_Trailer_HD.mp4";
            Media media = new Media(filePath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mdpPlayer.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        } catch (Exception e) {
            displayError(e);
        }
    }
}
