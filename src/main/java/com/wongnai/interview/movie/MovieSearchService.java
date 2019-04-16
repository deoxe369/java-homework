package com.wongnai.interview.movie;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MovieSearchService {
	List<Movie> search(String queryText) throws IOException, ExecutionException, InterruptedException;
}
