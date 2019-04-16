package com.wongnai.interview.movie.search;

import com.wongnai.interview.movie.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component("invertedIndexMovieSearchService")
@DependsOn("movieDatabaseInitializer")
public class InvertedIndexMovieSearchService implements MovieSearchService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private InvertedIndexRepository invertedIndexRepository;

    @Override
    public List<Movie> search(String queryText) throws IOException, ExecutionException, InterruptedException {
        //TODO: Step 4 => Please implement in-memory inverted index to search movie by keyword.
        // You must find a way to build inverted index before you do an actual search.
        // Inverted index would looks like this:
        // -------------------------------
        // |  Term      | Movie Ids      |
        // -------------------------------
        // |  Star      |  5, 8, 1       |
        // |  War       |  5, 2          |
        // |  Trek      |  1, 8          |
        // -------------------------------
        // When you search with keyword "Star", you will know immediately, by looking at Term column, and see that
        // there are 3 movie ids contains this word -- 1,5,8. Then, you can use these ids to find full movie object from repository.
        // Another case is when you find with keyword "Star War", there are 2 terms, Star and War, then you lookup
        // from inverted index for Star and for War so that you get movie ids 1,5,8 for Star and 2,5 for War. The result that
        // you have to return can be union or intersection of those 2 sets of ids.
        // By the way, in this assignment, you must use intersection so that it left for just movie id 5.
        List<Movie> result = new ArrayList<>();
        for (String word : queryText.split(" ")) {
            InvertedIndex invertedIndex = invertedIndexRepository.findByTerm(StringUtils.capitalize(word.toLowerCase()));
            if (invertedIndex != null) {
                CompletableFuture<List<Movie>> future = movieRepository.findByIdIn(invertedIndex.getMovie_ids());
                result = intersecResult(result, future.get());
            }

        }

        return result;

    }

    private List<Movie> intersecResult(List<Movie> result, List<Movie> movieList) {
        if (result.size() == 0) {
            result.addAll(movieList);
        }
        return result.stream()
                .flatMap(x -> movieList.stream()
                        .filter(y -> x.getId().equals(y.getId())))
                .collect(Collectors.toList());
    }
}
