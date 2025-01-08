package dk.easv.privatemoviecollection.DAL;

import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                String genre = rs.getString("genre");
                String path = rs.getString("filelink");
                double lastviewed = rs.getDouble("lastview");
                double duration = rs.getDouble("duration");

                MovieCollection movie = new MovieCollection(id,name,rating,genre,path,lastviewed,duration);
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
        return null;
    }

    @Override
    public void deleteMovie(MovieCollection movie) throws Exception {

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


