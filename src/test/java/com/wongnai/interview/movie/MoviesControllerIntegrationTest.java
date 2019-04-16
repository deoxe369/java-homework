package com.wongnai.interview.movie;

import java.util.Arrays;

import org.checkerframework.checker.units.qual.A;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class MoviesControllerIntegrationTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private InvertedIndexRepository invertedIndexRepository;

	@Autowired
	private MovieRepository movieRepository;

	@After
	public void teardown() {
		invertedIndexRepository.deleteAll();
		movieRepository.deleteAll();
	}

	@Test
	public void testSearchWithRestApi() throws Exception {
		mvc.perform(get("/movies/search?q=Glorious"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()", Matchers.equalTo(7)))
				.andExpect(jsonPath("$[?(@.name == 'The Glorious Fool')].actors[*]",
						Matchers.equalTo(Arrays.asList("Helene Chadwick", "Richard Dix"))));
	}
}
