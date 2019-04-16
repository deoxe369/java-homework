package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
    @Autowired
    private MovieDataService movieDataService;

    @Override
    public List<Movie> search(String queryText) {
        //TODO: Step 2 => Implement this method by using data from MovieDataService
        // All test in SimpleMovieSearchServiceIntegrationTest must pass.
        // Please do not change @Component annotation on this class

        List<Movie> movieList = movieDataService.fetchAll().stream()
                .filter(m -> nameContain(m.getTitle(), queryText))
                .map(m -> {
                    Movie movie = new Movie(m.getTitle());
                    movie.setActors(m.getCast());
                    return movie;
                }).collect(Collectors.toList());

        return movieList;
    }

    private boolean nameContain(String title, String queryText) {

        if (StringUtils.containsIgnoreCase(title, queryText)) {
            for (String word : title.split(" ")) {
                if (StringUtils.equalsIgnoreCase(word, queryText)) {
                    return true;
                }
            }
        }
        return false;
    }
}
