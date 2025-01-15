package dk.easv.privatemoviecollection.BE;

import dk.easv.privatemoviecollection.BLL.MovieCollectionManager;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class MovieCollection {

    private String name;
    private String genre;
    private double rating;
    private String path;
    private String lastviewed;
    private double duration;
    private int id;


    /*public MovieCollection(String name, double rating,String path,String lastviewed, String genre, double duration) {
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.path = path;
        this.lastviewed = lastviewed;
        this.duration = duration;
    }

     */





    //Overloaded constructor to allow us to use getId
    public MovieCollection(int id, String name, double rating,String path, String lastviewed, String genre, double duration) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.path = path;
        this.lastviewed = lastviewed;
        this.duration = duration;
    }









    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastviewed() {
        return lastviewed;
    }

    public void setLastviewed(String lastviewed) {
        this.lastviewed = lastviewed;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getId() { return id; }
}