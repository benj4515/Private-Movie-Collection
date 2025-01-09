package dk.easv.privatemoviecollection.BE;

import dk.easv.privatemoviecollection.BLL.MovieCollectionManager;

public class MovieCollection {

    private String name;
    private String genre;
    private double rating;
    private String path;
    private double lastviewed;
    private double duration;


    public MovieCollection(String name, double rating,String path,double lastviewed, String genre, double duration) {
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.path = path;
        this.lastviewed = lastviewed;
        this.duration = duration;
    }

    /*public MovieCollection createMovie (new MovieCollection newMovie) {
        MovieCollection movie = null;
        try {
            movie = new MovieCollection.createMovie(newMovie);
            return MovieCollectionManager.movie(newMovie);
        }

    }

     */





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

    public double getLastviewed() {
        return lastviewed;
    }

    public void setLastviewed(double lastviewed) {
        this.lastviewed = lastviewed;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
