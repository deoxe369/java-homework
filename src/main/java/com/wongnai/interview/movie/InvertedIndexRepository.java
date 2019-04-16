package com.wongnai.interview.movie;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvertedIndexRepository extends CrudRepository<InvertedIndex, String> {

	InvertedIndex findByTerm(String term);
}
