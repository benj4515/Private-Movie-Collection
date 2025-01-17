package dk.easv.privatemoviecollection.GUI.Controller;

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Date;

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
    private TextField TmdbUrlField;

    MovieCollectionModel MovieCollectionModel;
    @FXML
    private Button addMovieButton;
    @FXML
    private Button cancelButton;
    private MovieCollectionController movieCollectionController;
    @FXML
    private ComboBox genreDropDown;

    @FXML
    public void initialize() {
        genreDropDown.getItems().addAll("Action", "Adventure", "Comedy", "Crime", "Drama", "Fantasy", "Historical", "Horror", "Mystery", "Philosophical", "Political", "Romance", "Saga", "Satire", "Science fiction", "Social", "Speculative", "Thriller", "Urban", "Western");
    }



    private void displayError(Throwable t) {
        // This display if any error occours
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    public NewMovieWindowController(){

        try{
            MovieCollectionModel = new MovieCollectionModel();

        } catch(Exception e){
            displayError(e);
            e.printStackTrace();
        }
    }
    public void setMovieCollectionController(MovieCollectionController movieCollectionController) {
        this.movieCollectionController = movieCollectionController;
    }

    @FXML
    private void handleSubmit() throws Exception {

        String name = movieNameField.getText();
        String genre = genreDropDown.getValue().toString();
        String sduration = durationField.getText();
        String slastviewed = lastOpenedField.getText();
        String srating = ratingField.getText();
        String path = fileLocationField.getText();
        

        if (name.isEmpty() || genre.isEmpty() || sduration.isEmpty() || slastviewed.isEmpty() || srating.isEmpty() || path.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
            return;
        }



        try {
            int durationInt = Integer.parseInt(sduration);
            if (durationInt <= 0) {
                showAlert("Validation Error", "Duration must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Duration must be a number.");
            return;
        }

        try {
            double ratingDouble = Double.parseDouble(srating);
            if (ratingDouble < 0 || ratingDouble > 10) {
                showAlert("Validation Error", "Rating must be between 0 and 10.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Rating must be a number.");
            return;
        }




        System.out.println("Movie Name: " + name);
        System.out.println("Genre: " + genre);
        System.out.println("Duration: " + sduration);
        System.out.println("Last Opened: " + slastviewed);
        System.out.println("Rating: " + srating);
        System.out.println("File Location: " + path);

        double rating = Double.parseDouble(srating);
        double duration = Double.parseDouble(sduration);
        Date lastviewed = Date.valueOf(slastviewed);

        MovieCollection newMovie = new MovieCollection(-1,name,rating,path,lastviewed,genre,duration);

        MovieCollectionModel.createMovie(newMovie);
        System.out.println("New Movie Added" + newMovie);



        if(movieCollectionController != null){
            movieCollectionController.tableRefresh();
        }

        Stage stage = (Stage) addMovieButton.getScene().getWindow();
        stage.close();
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

    public void setParent(MovieCollectionController parentParam) {
        this.movieCollectionController = parentParam;
    }
}