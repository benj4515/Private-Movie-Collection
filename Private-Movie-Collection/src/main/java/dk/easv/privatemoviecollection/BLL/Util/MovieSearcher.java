package dk.easv.privatemoviecollection.BLL.Util;

import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.util.ArrayList;
import java.util.List;


// This class is used to search for movies in the searchbar
public class MovieSearcher {

    // This method searches for movies from the searchbar - it takes in a list of movies and a query
    public List<MovieCollection> search(List<MovieCollection> searchBase, String query){

        // This list is used to store the search results
        List<MovieCollection> searchResult = new ArrayList<>();

        // This loop goes through the searchbase and compares the searchbar with the movie names
        for(MovieCollection movie : searchBase) {
            if(compareToMovieName(query, movie) || compareToMovieGenre(query, movie)) {
                searchResult.add(movie);
            }

        }
        // This returns the search results
        return searchResult;
    }


    private boolean compareToMovieGenre(String query, MovieCollection movie) {
        // this method compares the searchbar with the artist values
        return movie.getGenre().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToMovieName(String query, MovieCollection movie) {
        // this method compares the searchbar with the artist values
        return movie.getName().toLowerCase().contains(query.toLowerCase());
    }


}
