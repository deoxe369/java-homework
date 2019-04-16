package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.google.common.base.Splitter;
import com.wongnai.interview.movie.InvertedIndex;
import com.wongnai.interview.movie.InvertedIndexRepository;
import com.wongnai.interview.movie.Movie;
import org.apache.commons.lang3.StringUtils;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private InvertedIndexRepository invertedIndexRepository;

	@Transactional
	//Async
	public void forceSync() {
		List<InvertedIndex> invertedIndexList = new ArrayList<>();
		List<InvertedIndex> invertedIndexList2 = new ArrayList<>();

		movieDataService.fetchAll().stream().forEach(m -> {
			Movie movie = new Movie(m.getTitle());
			movie.setActors(m.getCast());
			movieRepository.save(movie);

			for (String word : m.getTitle().split(" ")) {
				List<Long> ids = new ArrayList<>();
				ids.add(movie.getId());
				InvertedIndex invertedIndex = new InvertedIndex(StringUtils.capitalize(word),ids);
				invertedIndexList.add(invertedIndex);
			}
		});

		Map<String, String> distinctList = invertedIndexList.stream()
				.collect(Collectors.groupingBy(m -> m.getTerm(),
						Collectors.mapping(j -> j.getMovie_ids().get(0).toString(),
								Collectors.joining(","))));

		for (Map.Entry<String, String> entry : distinctList.entrySet()) {
			List<String> list = Lists.newArrayList(Splitter.on(",").split(entry.getValue()));
			List<Long> newList = list.stream()
					.map(s -> Long.parseLong(s))
					.collect(Collectors.toList());

			InvertedIndex invertedIndex = new InvertedIndex(entry.getKey(),newList);
			invertedIndexList2.add(invertedIndex);
		}

		invertedIndexRepository.saveAll(invertedIndexList2);

        invertedIndexList.clear();
        invertedIndexList2.clear();
        invertedIndexList.clear();
	}
}
