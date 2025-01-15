package dk.easv.privatemoviecollection.GUI.Controller;

//mediaPlayer imports

import dk.easv.privatemoviecollection.BE.MovieCollection;
import dk.easv.privatemoviecollection.GUI.Model.MovieCollectionModel;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MPlayer extends BorderPane {

    @FXML
    Media media;
    @FXML
    public MediaPlayer player;

    @FXML
    MediaView view;
    @FXML
    Pane mpane;
    @FXML
    MediaBar bar;


    MovieCollectionModel MovieCollectionModel;
    MovieCollection movieCollection;
    MovieCollectionController movieCollectionController;

    public MPlayer() { //constructor
        media = new Media(movieCollectionController.selectedMovie().getPath());
        player = new MediaPlayer(media);
        view = new MediaView(player);
        mpane = new Pane();
        mpane.getChildren().add(view); // Calling the function getChildren

        /*
        // inorder to add the view
        setCenter(mpane);
        bar = new MediaBar(player); // Passing the player to MediaBar
        setBottom(bar); // Setting the MediaBar at bottom
        setStyle("-fx-background-color:#bfc2c7"); // Adding color to the mediabar
        //player.play(); // Making the video play
        */

    }
    public void initialize(String file){
        media = new Media(file);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        mpane = new Pane();
        mpane.getChildren().add(view); // Calling the function getChildren


        // inorder to add the view
        setCenter(mpane);
        bar = new MediaBar(player); // Passing the player to MediaBar
        setBottom(bar); // Setting the MediaBar at bottom
        setStyle("-fx-background-color:#bfc2c7"); // Adding color to the mediabar
        //player.play(); // Making the video play
    }
    public void setParent(MovieCollectionController parentParam) {
        this.movieCollectionController = parentParam;
    }
}


