package dk.easv.privatemoviecollection.GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class NewMovieWindowController {

    @FXML
    private TextField movieNameField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField lastOpenedField;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField fileLocationField;

    @FXML
    private void handleSubmit() {
        String movieName = movieNameField.getText();
        String genre = genreField.getText();
        String duration = durationField.getText();
        String lastOpened = lastOpenedField.getText();
        String rating = ratingField.getText();
        String fileLocation = fileLocationField.getText();

        if (movieName.isEmpty() || genre.isEmpty() || duration.isEmpty() || lastOpened.isEmpty() || rating.isEmpty() || fileLocation.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }

        try {
            int durationInt = Integer.parseInt(duration);
            if (durationInt <= 0) {
                showAlert("Validation Error", "Duration must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Duration must be a number.");
            return;
        }

        try {
            double ratingDouble = Double.parseDouble(rating);
            if (ratingDouble < 0 || ratingDouble > 10) {
                showAlert("Validation Error", "Rating must be between 0 and 10.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Rating must be a number.");
            return;
        }


        System.out.println("Movie Name: " + movieName);
        System.out.println("Genre: " + genre);
        System.out.println("Duration: " + duration);
        System.out.println("Last Opened: " + lastOpened);
        System.out.println("Rating: " + rating);
        System.out.println("File Location: " + fileLocation);
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) movieNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}