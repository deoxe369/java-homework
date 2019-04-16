package com.wongnai.interview.movie;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class InvertedIndex {

    @Id
    private String term;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> movie_ids = new ArrayList<>();

    protected InvertedIndex() {
    }

    public InvertedIndex(String term, List<Long> movie_ids) {

        this.term = term;
        this.movie_ids = movie_ids;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Long> getMovie_ids() {
        return movie_ids;
    }

    public void setMovie_ids(List<Long> movie_ids) {
        this.movie_ids = movie_ids;
    }
}

