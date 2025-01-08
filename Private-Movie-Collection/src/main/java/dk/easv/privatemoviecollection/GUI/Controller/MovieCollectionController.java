package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieCollectionController implements Initializable {
    public TableView<MovieCollection>  tblMovies;
    public TableColumn<MovieCollection, String>  colMovie;
    public TableColumn<MovieCollection, String>  colGenre;
    public TableColumn<MovieCollection, String>  colDuration;
    public TableColumn<MovieCollection, String>  colLastViewed;
    public TableColumn<MovieCollection, String>  colRating;
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
}