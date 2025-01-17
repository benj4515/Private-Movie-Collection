package dk.easv.privatemoviecollection.GUI;

import dk.easv.privatemoviecollection.DAL.MovieCollectionDAO;
import dk.easv.privatemoviecollection.GUI.Controller.MPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.MalformedURLException;
import java.io.File;


public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/MovieCollection.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1020, 740);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}