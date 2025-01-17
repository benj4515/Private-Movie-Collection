package dk.easv.privatemoviecollection.BE;



public class Genre {

    // Genre class with id and genre - instance variables
    private final int id;
    private String genre;

    // Constructor for Genre class
    public Genre(int id, String genre){
        this.id = id;
        this.genre = genre;
    }

    //getters and setters for Genre class
    public int getId() {return id;}

    public String getGenre(){return genre;}

    public void setGenre(String genre){this.genre = genre;}

}
