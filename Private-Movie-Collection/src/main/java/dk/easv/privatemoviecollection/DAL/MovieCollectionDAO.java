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


public class MovieCollectionDAO implements IMovieDataAccess  {

    private final DBConnector dbConnector = new DBConnector();

    public MovieCollectionDAO() throws IOException {}

    @Override
    public List<MovieCollection> getAllMovies() throws Exception{

        ArrayList<MovieCollection> movieCollections = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
             Statement statement = conn.createStatement()){
            String sql = "select * from dbo.Movies";
            ResultSet rs = statement.executeQuery(sql);

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
            return movieCollections;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't get movies from database");
        }

    }

    @Override
    public MovieCollection createMovie(MovieCollection newMovie) throws Exception {

        String sql ="INSERT into dbo.Movies (name, rating, filelink, lastview, genre, duration) VALUES (?, ?, ?, ?, ?, ?)";
        DBConnector dbConnector = new DBConnector();

        try (Connection conn = dbConnector.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, newMovie.getName());
            stmt.setDouble(2, newMovie.getRating());
            stmt.setString(3, newMovie.getPath());
            stmt.setDate(4, newMovie.getLastviewed());
            stmt.setString(5, newMovie.getGenre());
            stmt.setDouble(6, newMovie.getDuration());

            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Couldn't create movie in database");
        }

        return null;
    }

    @Override
    public void updateMovie(MovieCollection movie) throws Exception {

        //Collect edited data from movie object and updates the value in the database ( Movie table)
      String sql = "UPDATE dbo.Movies SET name = ?, rating = ?, filelink = ?, lastview = ?, genre = ?, duration = ? WHERE id = ?";

            try(Connection conn = dbConnector.getConnection()){
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1,movie.getName());
                stmt.setDouble(2,movie.getRating());
                stmt.setString(3,movie.getPath());
                stmt.setDate(4,movie.getLastviewed());
                stmt.setString(5,movie.getGenre());
                stmt.setDouble(6,movie.getDuration());
                //stmt.setInt(7,movie.getId());

                //Execute the update statement
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new Exception("Couldn't update movie in database",e);
            }
    }


@Override
public void deleteMovie(MovieCollection movie) throws Exception {
    // Deletes a movie from the database - movie table
    String sql = "DELETE FROM dbo.Movies WHERE id = ?;";
    //+ " DELETE FROM dbo.CatMovies WHERE id = ?";

    try (Connection conn = dbConnector.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, movie.getId());
        //stmt.setInt(2, movie.getId());
        stmt.executeUpdate();
    } catch (SQLException e) {
        throw new Exception("Couldn't delete movie from database", e);
    }
}

    @Override
    public void createGenre(String genre, List<MovieCollection> selectedMovies) throws Exception {
        String insertGenre = "INSERT INTO dbo.Category(name) VALUES (?)";
        String insertGenreMovies = "INSERT INTO dbo.CatMovie(catId, movieID) VALUES (?,?)";

        try (Connection conn = dbConnector.getConnection()){
            conn.setAutoCommit(false);

            int catId;
            try (PreparedStatement stmt = conn.prepareStatement(insertGenre, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, genre);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        catId = rs.getInt(1);
                    } else {
                        throw new Exception("Couldn't get id for the genre");
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(insertGenreMovies)) {
                for (MovieCollection movie : selectedMovies) {
                    stmt.setInt(1, catId);
                    stmt.setInt(2, movie.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't create genre in database", e);
        }
    }



   /* @Override
    public List<Genre> getAllGenre() throws Exception {
        return List.of();
    }

    */


   /* @Override
    public List<MovieCollection> getMovieCollectionsByGenre(String genre) throws Exception {
        return List.of();
    }

    */



    public List<Genre> getAllGenres() throws Exception{

        String query = "SELECT * FROM dbo.Category";
        List<Genre> genres = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while (rs.next()) {
                int id = rs.getInt("id");
                String genre = rs.getString("name");
                Genre category = new Genre(id, genre);
                genres.add(category);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("Could not fetch Genres", ex);
        }
        return genres;
    }


    public List<MovieCollection> getMoviesForGenre(int genreId) throws Exception {
        String query = "SELECT m.id, m.name, m.rating, m.filelink, m.lastview, m.genre, m.duration " +
                "From dbo.Movies m " +
                "JOIN dbo.CatMovie cm ON m.id = cm.movieID " +
                "WHERE cm.catId = ?";

        List<MovieCollection> movies = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(query);{
                stmt.setInt(1, genreId);
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
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Couldn't fetch movies", ex);
        }
        return movies;
    }

    @Override
    public void updateGenre(Genre genreName, ObservableList<MovieCollection> movies) throws Exception {

        String updateGenreSql = "UPDATE dbo.Category SET name = ? WHERE id = ?";
        String deleteGenreMoviesSql = "DELETE FROM dbo.CatMovie WHERE catId = ?";
        String insertGenreMoviesSql = "INSERT INTO dbo.CatMovie (catId, movieID) VALUES (?,?)";

        try (Connection conn = dbConnector.getConnection()){
            conn.setAutoCommit(false);

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
                for (MovieCollection movie : movies) {
                    stmt.setInt(1, genreName.getId());
                    stmt.setInt(2, movie.getId());
                    stmt.addBatch();

                }
                stmt.executeBatch();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Couldn't update genre in database", e);
        }
    }




    public void deleteGenre(Genre genreName) throws Exception {

        String deleteGenreMoviesSql = "DELETE FROM dbo.CatMovie WHERE catId = ?";
        String deleteGenreSql = "DELETE FROM dbo.Category WHERE id = ?";

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
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Couldn't delete genre in database", e);
        }
    }







}




