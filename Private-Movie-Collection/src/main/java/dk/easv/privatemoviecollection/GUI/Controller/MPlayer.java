package dk.easv.privatemoviecollection.GUI.Controller;

//mediaPlayer imports

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class MPlayer extends BorderPane {

    @FXML
    Media media;
    @FXML
    public MediaPlayer player;


    private MediaView view;
    @FXML
    Pane mpane;
    @FXML
    MediaBar bar;


    MovieCollectionModel MovieCollectionModel;
    MovieCollection movieCollection;
    MovieCollectionController movieCollectionController;


    public MPlayer() { //constructor
        //movieCollectionController = new MovieCollectionController();
    }
    public void initialize() {
    }
    public void setParent(MovieCollectionController parentParam) {
        this.movieCollectionController = parentParam;
    }


    public void setup() {
       String path = movieCollectionController.selectedMovie().getPath();

       //String path = "\\github\\Private-Movie-Collection\\Private-Movie-Collection\\src\\main\\resources\\Movies\\Borat.mp4";
        media = new Media(new File(path).toURI().toString());
        player = new MediaPlayer(media);
        view = new MediaView(player);

        // Set the MediaView to the MediaPlayer
        view.setMediaPlayer(player);

        // Set the size of the MediaView
        view.setFitWidth(800);
        view.setFitHeight(600);
        view.setPreserveRatio(true); // Maintain aspect ratio
        // Maintain aspect ratio
        mpane.getChildren().clear();
        mpane.getChildren().add(view);

        //view.setFitHeight(this.getHeight());
        //view.setFitWidth(this.getWidth());
        ;
        // inorder to add the view
        //setCenter(mpane);
        // bar = new MediaBar(player); // Passing the player to MediaBar
        // setBottom(bar); // Setting the MediaBar at bottom
        //setStyle("-fx-background-color:#bfc2c7"); // Adding color to the mediabar
        //player.Play()
        player.setAutoPlay(true); // Making the video play
    }
}


