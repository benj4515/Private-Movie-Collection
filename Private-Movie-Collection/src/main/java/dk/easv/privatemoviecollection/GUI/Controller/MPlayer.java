package dk.easv.privatemoviecollection.GUI.Controller;

//mediaPlayer imports

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MPlayer extends BorderPane {
    Media media;
    public MediaPlayer player;
    MediaView view;
    Pane mpane;
    MediaBar bar;

    public MPlayer(String file) { //constructor
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
        player.play(); // Making the video play
    }
}

