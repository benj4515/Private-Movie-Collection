package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MovieCollectionController implements Initializable {

    // This class is used to control the MovieCollection.fxml file
    public TableView<MovieCollection> tblMovies;
    public TableColumn<MovieCollection, String> colMovie;
    public TableColumn<MovieCollection, String> colGenre;
    public TableColumn<MovieCollection, String> colDuration;
    public TableColumn<MovieCollection, String> colLastViewed;
    public TableColumn<MovieCollection, String> colRating;
    public Button btnDeleteMovie;
    public Button btnAddGenre;
    public ListView lstGenreMovies;
    public Button btnEditGenre;
    public TableColumn<Genre, String> colCat;

    private MovieCollectionModel movieCollectionModel;
    @FXML
    private TextField txtSearchMovie;
    @FXML
    private TableView<Genre> tblGenre;
    private MovieCollection selectedMovie;



    // This is the constructor for the MovieCollectionController
    public MovieCollectionController() {

        try {
            movieCollectionModel = new MovieCollectionModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
        System.out.println(movieCollectionModel.getObservableMovies());
    }

    // This method displays an error message
    private void displayError(Exception e) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }


    // This method initializes the MovieCollection.fxml file
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //runs the OldShittyMovies method on initialize to look for old and/or bad movies.
        oldShittyMovies();

        // This sets the cell value factory for the tableview
        colMovie.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastviewed"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colCat.setCellValueFactory(new PropertyValueFactory<>("genre"));

        // This sets the items in the tableview and adds a listener to the search bar
        tblMovies.setItems(movieCollectionModel.getObservableMovies());
        txtSearchMovie.textProperty().addListener((_, _, newValue) -> {

            // This tries to search for movies. If it fails, it displays an error message
            try {
                movieCollectionModel.searchMovies(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });


        //this sets the items for the genre table.
        try {
            tblGenre.setItems(movieCollectionModel.getAllGenres());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //this checks if a genre is selected and sets the items for the movie list.
        tblGenre.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if ( newValue != null ) {
                try {

                    lstGenreMovies.setItems(movieCollectionModel.getMoviesForGenre(newValue));
                    lstGenreMovies.getSelectionModel().selectedItemProperty().addListener((_,_,newMovie) ->{
                        if ( newMovie != null ) {
                            selectedMovie = (MovieCollection) newMovie;
                        }


                    });


                } catch (Exception e) {
                    displayError(e);
                }
            } else {
                lstGenreMovies.setItems(FXCollections.observableArrayList());
            }
        });



    }

    // This method is called when the "Add movie" button is clicked
    @FXML
    private NewMovieWindowController onNewMovieButtonClick(ActionEvent actionEvent) {
        try {

            // This creates a new FXMLLoader object and loads the NewMovieWindow.fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/NewMovieWindow.fxml"));

            // This creates a new parent object and a new stage object. It sets the scene to the parent object and the title to "Add movie"
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
            e.printStackTrace();
            displayError(e);
        }
        return null;
    }

    // This method is called when the "Delete movie" button is clicked
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
            ObservableList<Genre> Genre = movieCollectionModel.getAllGenres();
            tblGenre.setItems(null);
            tblGenre.setItems(Genre);
        } catch (Exception e) {
            displayError(e);
        }
        ObservableList<MovieCollection> MovieCollection = movieCollectionModel.getObservableMovies();
        System.out.println("Number of Movies: " + MovieCollection.size());
        tblMovies.setItems(null); // Clear the table
        tblMovies.setItems(MovieCollection); // Reset the items
        tblMovies.refresh(); // Refresh the table




    }


    //this method is called when the New button in the lower left corner is clicked
    @FXML
    private void onNewGenreButtonClick() throws IOException {
        try {

            //this creates a new FXMLLoader object and loads the NewCategorylistWindow.fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/NewCategorylistWindow.fxml"));

            //this creates a new parent object and a new stage object. It sets the scene to the parent object and the title to "Add Genre" and the controller to the loader
            Parent scene = loader.load();
            NewCategorylistWindowController playlistController = loader.getController();
            playlistController.setMovieCollectionModel(movieCollectionModel);
            playlistController.setMovieCollectionController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.setTitle("Add Genre");



            //this sets the modality to Application (you must close "Add Genre" before going to the parent window)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }

    }


    //this method is called when the play button is clicked
    @FXML
    public MPlayer onPlayButtonClick(ActionEvent actionEvent) {

        try {

            //this creates a new FXMLLoader object and loads the onPlayButtonWindow.fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/onPlayButtonWindow.fxml"));

        //this creates a new parent object and a new stage object. It sets the scene to the parent object and the title to "MediaPlayer"
        Parent scene = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(scene, 800,600));
        stage.setTitle("MediaPlayer");

        // Get the controller reference and set the parent
       MPlayer controller = loader.getController();
        controller.setParent(this);
        controller.setup();

        // Set the modality to Application (you must close "MediaPlayer" before going to the parent window)
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
        return null;
    }

    //this returns the selected movie in the tableview
    public MovieCollection selectedMovie(){

        MovieCollection selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        return selectedMovie;
    }


    //this method is called when the edit button is clicked
    public void OnEditGenreClicked(ActionEvent actionEvent) throws Exception {

        //this gets the selected genre
        Genre selectedGenre = tblGenre.getSelectionModel().getSelectedItem();

        //this creates a new FXMLLoader object and loads the NewCategorylistWindow.fxml file
        if (selectedGenre != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/privatemoviecollection/NewCategorylistWindow.fxml"));

            //this creates a new parent object and a new stage object. It sets the scene to the parent object and the title to "Edit Genre", the controller to the loader and the modality to Application
            Parent scene = loader.load();
            NewCategorylistWindowController categoryController = loader.getController();
            categoryController.setMovieCollectionModel(movieCollectionModel);
            categoryController.setMovieCollectionController(this);
            categoryController.loadGenreData(selectedGenre, movieCollectionModel.getMoviesForGenre(selectedGenre));

            Stage stage = new Stage();
            stage.setTitle("Edit Genre");
            stage.setScene(new Scene(scene));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } else {
            System.out.println("No genre selected");
        }

    }

    //this method is called when the delete button is clicked
    @FXML
    private void onDeleteGenreButtonPressed() throws Exception {

        //this gets the selected genre
        Genre selectedGenre = tblGenre.getSelectionModel().getSelectedItem();

        //this deletes the genre and refreshes the table
        if (selectedGenre != null) {
            movieCollectionModel.deleteGenre(selectedGenre);
            tableRefresh();
        }else {
            System.out.println("No genre selected");
        }
    }

    //this method is called at the start of the application to check if there is any old and/or bad movies.
  public void oldShittyMovies() {
    try {
        //call the method from within the movieCollectionModel
        movieCollectionModel.checkIfOldShit();
    }
    //if the check finds anything, it will throw an exception and display an alert
    catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Old or bad movie");
        alert.setHeaderText("This movie either has a score of less than 6, or has not been viewed in the last 2 years");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
  }
}