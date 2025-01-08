package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieCollectionController implements Initializable {
    public TableView tblMovies;
    private MovieCollectionModel movieCollectionManager;

    public MovieCollectionController() {

        try {
            movieCollectionManager = new MovieCollectionModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
        initialize( movieCollectionManager);
    }

    private void displayError(Exception e) {
        // displays if any error occours
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
    }

    public void initialize(MovieCollectionModel movieCollectionModel) {
        tblMovies.setItems(movieCollectionModel.getObservableMovies());

    }

@Override
    public void initialize(URL location, ResourceBundle resources) {

    }

   /* @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    */
}