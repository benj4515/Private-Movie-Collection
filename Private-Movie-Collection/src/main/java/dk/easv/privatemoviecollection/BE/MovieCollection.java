package dk.easv.privatemoviecollection.BE;

public class MovieCollection {

    private final int id;
    private String name;
    private String genre;
    private double rating;
    private String path;
    private double lastviewed;
    private double duration;


    public MovieCollection(int id, String name, double rating,String path,double lastviewed, String genre, double duration) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.genre = genre;
        this.path = path;
        this.lastviewed = lastviewed;
        this.duration = duration;
    }



    public int getId() {
        return id;
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
