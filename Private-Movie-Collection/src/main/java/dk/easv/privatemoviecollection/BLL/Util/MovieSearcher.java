package dk.easv.privatemoviecollection.BLL.Util;

import dk.easv.privatemoviecollection.BE.MovieCollection;

import java.util.ArrayList;
import java.util.List;

public class MovieSearcher {
    public List<MovieCollection> search(List<MovieCollection> searchBase, String query){

        List<MovieCollection> searchResult = new ArrayList<>();

        for(MovieCollection movie : searchBase) {
            if(compareToMovieName(query, movie) || compareToMovieGenre(query, movie)) {
                searchResult.add(movie);
            }

        }
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
