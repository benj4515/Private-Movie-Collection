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
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public ListView lstGenreMovies;
    private MovieCollectionModel movieCollectionModel;
    @FXML
    private TextField txtSearchMovie;
    @FXML
    private TextField txtSearchGenre;
    @FXML
    private TableView<Genre> tblGenre;
    public TableColumn<Genre, String> colCat;
    private MovieCollection selectedMovie;



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

        tblGenre.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if ( newValue != null ) {
                try {
                    lstGenreMovies.setItems(movieCollectionModel.getMoviesForGenre(newValue));
                    lstGenreMovies.getSelectionModel().selectedItemProperty().addListener((_,_,newMovie) ->{
                        if ( newMovie != null ) {
                            selectedMovie = (MovieCollection) newMovie;
                            //System.out.println("Selected song from genre: " + selectedMovie().getAddres);
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


    @FXML
    private NewMovieWindowController onNewMovieButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/New.fxml"));

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


    @FXML
    private void onNewGenreButtonClick() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/NewCategorylistWindow.fxml"));

            Parent scene = loader.load();

            NewCategorylistWindowController playlistController = loader.getController();
            playlistController.setMovieCollectionModel(movieCollectionModel);
            playlistController.setMovieCollectionController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(scene));
            stage.setTitle("Add Genre");




            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }

    }


    MPlayer player;
    FileChooser fileChooser;

    @FXML
    public MPlayer onPlayButtonClick(ActionEvent actionEvent) {

        try {
            /*
            // setting up the stages
            MenuItem open = new MenuItem("Open");
            Menu file = new Menu("File");
            MenuBar menu = new MenuBar();

            // Connecting the above three
            file.getItems().add(open); // it would connect open with file
            menu.getMenus().add(file);

            // here you can choose any video
            player = new MPlayer("fix file here so it uses "<----);

            // Setting the menu at the top
            player.setTop(menu);

            // Adding player to the Scene
            Scene scene = new Scene(player, 720, 535, Color.BLACK);

            Stage stage = new Stage();
            // height and width of the video player
            // background color set to Black
            stage.setScene(scene); // Setting the scene to stage
            stage.show(); // Showing the stage
*/

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/dk/easv/privatemoviecollection/onPlayButtonWindow.fxml"));


        Parent scene = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(scene));
        stage.setTitle("MediaPlayer");

        MPlayer controller = loader.getController();

        controller.setParent(this);


        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        }
        return null;
    }

    public MovieCollection selectedMovie(){

        MovieCollection selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        return selectedMovie;
    }
}
