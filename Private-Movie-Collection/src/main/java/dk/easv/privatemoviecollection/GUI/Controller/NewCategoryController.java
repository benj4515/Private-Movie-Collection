package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.Parent;

public class NewCategoryController {
    public TextField categoryField;
    public Button addCategoryButton;
    public Button cancelButton;

    MovieCollectionModel MovieCollectionModel;
    MovieCollectionController movieCollectionController;
    private int currentCategoryID;

    private void displayError(Throwable t) {
        // This display if any error occours
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }



    public void handleSubmit(ActionEvent actionEvent) {
        String category = categoryField.getText();
        if (category.isEmpty()) {
            showError("Genre is required");
            return;
        }

        try{
            Genre existingGenre = MovieCollectionModel.getGenreById(currentCategoryID);
            if(existingGenre != null){
                showError("Genre already exists");
            } else {
                MovieCollectionModel.createGenre(category);
            }

            movieCollectionController.tableRefresh();
            closeWindow();
        } catch (Exception e) {
            showError("Error saving genre" + e.getMessage());
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
    }

    private void showError(String message) {
        // this shows error if any occurs
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        // this closes the window
        Stage stage = (Stage) categoryField.getScene().getWindow();
        stage.close();
    }

    public void setParent(MovieCollectionController parentParam) {
        this.movieCollectionController = parentParam;
    }

}
