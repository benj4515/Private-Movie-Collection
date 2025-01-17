package dk.easv.privatemoviecollection.DAL;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


//this class is used to access the database and perform CRUD operations on the Movie table - implements the IMovieDataAccess interface
public class MovieCollectionDAO implements IMovieDataAccess  {

    //private instance of DBConnector
    private final DBConnector dbConnector = new DBConnector();

    //constructor for MovieCollectionDAO
    public MovieCollectionDAO() throws IOException {}


    //this method gets all movies from the database
    @Override
    public List<MovieCollection> getAllMovies() throws Exception{

        //create a new arraylist to store the movies
        ArrayList<MovieCollection> movieCollections = new ArrayList<>();

        //try with resources to connect to the database and execute a query to get all movies from the database
        try (Connection conn = dbConnector.getConnection();
             Statement statement = conn.createStatement()){
            String sql = "select * from dbo.Movies";
            ResultSet rs = statement.executeQuery(sql);

            //while loop to go through the resultset and add the movies to the arraylist
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                double rating = rs.getDouble("rating");
                String path = rs.getString("filelink");
                Date lastviewed = rs.getDate("lastview");
                String genre = rs.getString("genre");
                double duration = rs.getDouble("duration");

                MovieCollection movie = new MovieCollection(id,name,rating,path,lastviewed,genre,duration);
                movieCollections.add(movie);
            }

            //return the arraylist, or throw an exception if it fails
            return movieCollections;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't get movies from database");
        }

    }


    //this method creates a new movie in the database
    @Override
    public MovieCollection createMovie(MovieCollection newMovie) throws Exception {

        //create a string with the sql statement to insert a new movie into the database
        String sql ="INSERT into dbo.Movies (name, rating, filelink, lastview, genre, duration) VALUES (?, ?, ?, ?, ?, ?)";
        DBConnector dbConnector = new DBConnector();

        //try with resources to connect to the database and execute the insert statement
        try (Connection conn = dbConnector.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, newMovie.getName());
            stmt.setDouble(2, newMovie.getRating());
            stmt.setString(3, newMovie.getPath());
            stmt.setDate(4, newMovie.getLastviewed());
            stmt.setString(5, newMovie.getGenre());
            stmt.setDouble(6, newMovie.getDuration());
            stmt.executeUpdate();

            //return the new movie, or throw an exception if it fails
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Couldn't create movie in database");
        }

        return null;
    }

    //this method updates movies in the database
    @Override
    public void updateMovie(MovieCollection movie) throws Exception {

        //create a string with the sql statement to update a movie in the database
      String sql = "UPDATE dbo.Movies SET name = ?, rating = ?, filelink = ?, lastview = ?, genre = ?, duration = ? WHERE id = ?";

            //try with resources to connect to the database and execute the update statement
            try(Connection conn = dbConnector.getConnection()){
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1,movie.getName());
                stmt.setDouble(2,movie.getRating());
                stmt.setString(3,movie.getPath());
                stmt.setDate(4,movie.getLastviewed());
                stmt.setString(5,movie.getGenre());
                stmt.setDouble(6,movie.getDuration());
                //set the id of the movie to update

                //Execute the update statement, or throw an exception if it fails
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new Exception("Couldn't update movie in database",e);
            }
    }


    //this method deletes movies in the database
@Override
public void deleteMovie(MovieCollection movie) throws Exception {

        //create a string with the sql statement to delete a movie from the database
        String sql = "DELETE FROM dbo.Movies WHERE id = ?;";

        //try with resources to connect to the database and execute the delete statement
        try (Connection conn = dbConnector.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, movie.getId());

        stmt.executeUpdate();

        //throw an exception if it fails
        } catch (SQLException e) {
        throw new Exception("Couldn't delete movie from database", e);
    }
}

    //this method creates a new list of movies with a selected genre
    @Override
    public void createGenre(String genre, List<MovieCollection> selectedMovies) throws Exception {

        //creates strings with the sql statements to insert a new genre and the movies with the genre into the database
        String insertGenre = "INSERT INTO dbo.Category(name) VALUES (?)";
        String insertGenreMovies = "INSERT INTO dbo.CatMovie(catId, movieID) VALUES (?,?)";

        //try with resources to connect to the database and execute the insert statements
        try (Connection conn = dbConnector.getConnection()){

           //set autocommit to false to ensure that both statements are executed
            conn.setAutoCommit(false);

            //try with resources to insert the genre into the database
            int catId;
            try (PreparedStatement stmt = conn.prepareStatement(insertGenre, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, genre);
                stmt.executeUpdate();

                //get the generated key for the genre
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        catId = rs.getInt(1);
                    } else {
                        throw new Exception("Couldn't get id for the genre");
                    }
                }
            }

            //try with resources to insert the movies with the genre into the database
            try (PreparedStatement stmt = conn.prepareStatement(insertGenreMovies)) {
                for (MovieCollection movie : selectedMovies) {
                    stmt.setInt(1, catId);
                    stmt.setInt(2, movie.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            //commit the transaction
            conn.commit();

            //throw an exception if it fails
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't create genre in database", e);
        }
    }

    //this method gets all genres from the data access
    public List<Genre> getAllGenres() throws Exception{

        //create a string with the sql statement, and a new arraylist to store the genres
        String query = "SELECT * FROM dbo.Category";
        List<Genre> genres = new ArrayList<>();

        //try with resources to connect to the database and execute the query to get all genres from the database
        try (Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            //while loop to go through the resultset and add the genres to the arraylist
            while (rs.next()) {
                int id = rs.getInt("id");
                String genre = rs.getString("name");
                Genre category = new Genre(id, genre);
                genres.add(category);
            }
        //return the arraylist, or throw an exception if it fails
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("Could not fetch Genres", ex);
        }
        return genres;
    }


    //this method gets implemented to populate the genrelist
    public List<MovieCollection> getMoviesForGenre(int genreId) throws Exception {

        //create a string with the sql statement, and a new arraylist to store the movies
        String query = "SELECT m.id, m.name, m.rating, m.filelink, m.lastview, m.genre, m.duration " +
                "From dbo.Movies m " +
                "JOIN dbo.CatMovie cm ON m.id = cm.movieID " +
                "WHERE cm.catId = ?";

        List<MovieCollection> movies = new ArrayList<>();

        //try with resources to connect to the database and execute the query to get all movies with a given genre from the database
        try (Connection conn = dbConnector.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);{
                stmt.setInt(1, genreId);

                //while loop to go through the resultset and add the movies to the arraylist
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        double rating = rs.getDouble("rating");
                        String path = rs.getString("filelink");
                        Date lastview = rs.getDate("lastview");
                        String genre = rs.getString("genre");
                        double duration = rs.getDouble("duration");

                        movies.add(new MovieCollection(id, name, rating, path, lastview, genre, duration));
                    }
                }
            }

        //return the arraylist, or throw an exception if it fails
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Couldn't fetch movies", ex);
        }
        return movies;
    }


    //this method gets implemented to update the genre
    @Override
    public void updateGenre(Genre genreName, ObservableList<MovieCollection> movies) throws Exception {

        //create strings with the sql statements to update the genre and the movies with the genre in the database
        String updateGenreSql = "UPDATE dbo.Category SET name = ? WHERE id = ?";
        String deleteGenreMoviesSql = "DELETE FROM dbo.CatMovie WHERE catId = ?";
        String insertGenreMoviesSql = "INSERT INTO dbo.CatMovie (catId, movieID) VALUES (?,?)";

        //try with resources to connect to the database and execute the update statements
        try (Connection conn = dbConnector.getConnection()){
            conn.setAutoCommit(false);

            //try with resources to update the genre in the database
            try (PreparedStatement stmt = conn.prepareStatement(updateGenreSql)) {
                stmt.setString(1, genreName.getGenre());
                stmt.setInt(2, genreName.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteGenreMoviesSql)) {
                stmt.setInt(1, genreName.getId());
                stmt.executeUpdate();
            }


            try (PreparedStatement stmt = conn.prepareStatement(insertGenreMoviesSql)) {
                //loop through the movies and add them to the database
                for (MovieCollection movie : movies) {
                    stmt.setInt(1, genreName.getId());
                    stmt.setInt(2, movie.getId());
                    stmt.addBatch();

                }
                stmt.executeBatch();
            }
            conn.commit();

        //throw an exception if it fails
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't update genre in database", e);
        }
    }


    //this method deletes genres in the database
    public void deleteGenre(Genre genreName) throws Exception {

        //create strings with the sql statements to delete the genre and the movies with the genre from the database
        String deleteGenreMoviesSql = "DELETE FROM dbo.CatMovie WHERE catId = ?";
        String deleteGenreSql = "DELETE FROM dbo.Category WHERE id = ?";

        //try with resources to connect to the database and execute the delete statements
        try (Connection conn = dbConnector.getConnection()){
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deleteGenreMoviesSql)) {
                stmt.setInt(1, genreName.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteGenreSql)) {
                stmt.setInt(1, genreName.getId());
                stmt.executeUpdate();
            }

            conn.commit();

        //throw an exception if it fails
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Couldn't delete genre in database", e);
        }
    }
}