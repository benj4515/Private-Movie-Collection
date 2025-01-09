package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieCollectionController implements Initializable {
    public TableView<MovieCollection> tblMovies;
    public TableColumn<MovieCollection, String> colMovie;
    public TableColumn<MovieCollection, String> colGenre;
    public TableColumn<MovieCollection, String> colDuration;
    public TableColumn<MovieCollection, String> colLastViewed;
    public TableColumn<MovieCollection, String> colRating;
    private MovieCollectionModel movieCollectionModel;

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

    /*public void initialize(MovieCollectionModel movieCollectionModel) {





    }

     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        colMovie.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colLastViewed.setCellValueFactory(new PropertyValueFactory<>("lastViewed"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        tblMovies.setItems(movieCollectionModel.getObservableMovies());

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
            controller.setParent(this); // this refers to this MainWindowController object

            // Set the modality to Application (you must close Window1 before going to the parent window
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            displayError(e);
        }
        return null;
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
        System.out.println("Number of songs: " + MovieCollection.size());
        tblMovies.setItems(null); // Clear the table
        tblMovies.setItems(MovieCollection); // Reset the items
        tblMovies.refresh(); // Refresh the table
    }
}
