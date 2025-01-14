package dk.easv.privatemoviecollection.DAL;

import dk.easv.privatemoviecollection.BE.Genre;
import dk.easv.privatemoviecollection.BE.MovieCollection;

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
                String lastviewed = rs.getString("lastview");
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
            stmt.setString(4, newMovie.getLastviewed());
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
                stmt.setString(4,movie.getLastviewed());
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



   /* @Override
    public List<Genre> getAllGenre() throws Exception {
        return List.of();
    }

    */


    @Override
    public List<MovieCollection> getMovieCollectionsByGenre(String genre) throws Exception {
        return List.of();
    }

    public List<Genre> getAllGenres() throws Exception{

        String query = "SELECT * FROM dbo.Category";
        List<Genre> genres = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            while (rs.next()) {
                int id = rs.getInt("id");
                String genre = rs.getString("name");
                genres.add(new Genre(id, genre));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
            throw new Exception("Could not fetch Genres", ex);
        }
        return genres;
    }

    public void createGenre(String genre) throws Exception {
        String insertCategory = "INSERT INTO dbo.Category (name) VALUES (?)";

        try (Connection conn = dbConnector.getConnection()){
            conn.setAutoCommit(false);

            int CategoryId;
            try (PreparedStatement stmt = conn.prepareStatement(insertCategory, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, genre);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        CategoryId = rs.getInt(1);
                    } else{
                        throw new Exception("Failed to get CategoryId");
                    }
                }
            }
            conn.commit();
        }catch (SQLException e){
            e.printStackTrace();
            throw new Exception("Couldn't create category in database");
        }
    }

/*    @Override
    public void updateGenre(Genre genre, List<MovieCollection> selectedMovies) throws Exception {

    }*/

}




