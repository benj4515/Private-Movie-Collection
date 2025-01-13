package dk.easv.privatemoviecollection.BE;

public class Genre {

    private final int id;
    private String genre;

    public Genre(int id, String genre){
        this.id = id;
        this.genre = genre;
    }

    public int getId() {return id;}

    public String getGenre(){return genre;}

    public void setGenre(String genre){this.genre = genre;}

}
