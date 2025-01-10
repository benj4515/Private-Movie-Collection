package dk.easv.privatemoviecollection.DAL;

import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                double lastviewed = rs.getDouble("lastview");
                String genre = rs.getString("genre");
                double duration = rs.getDouble("duration");

                MovieCollection movie = new MovieCollection(name,rating,path,lastviewed,genre,duration);
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
            stmt.setDouble(4, newMovie.getLastviewed());
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
                stmt.setDouble(4,movie.getLastviewed());
                stmt.setString(5,movie.getGenre());
                stmt.setDouble(6,movie.getDuration());
                //stmt.setInt(7,movie.getId());

                //Execute the update statement
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new Exception("Couldn't update movie in database",e);
            }
    }

    /*
    @Override
    public void deleteMovie(MovieCollection movie) throws Exception {
        //Deletes a movie from the database - movie table
        String sql = "DELETE FROM dbo.Movies WHERE id = ?"
                + "DELETE FROM dbo.CatMovies WHERE id = ?";

        try(Connection conn = dbConnector.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,movie.getId());
            stmt.setInt(2,movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Couldn't delete movie from database",e);
        }

    }

     */

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

/*    @Override
    public void updateGenre(Genre genre, List<MovieCollection> selectedMovies) throws Exception {

    }*/

}


