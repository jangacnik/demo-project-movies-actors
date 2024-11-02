package com.demo.movie.repositories;

import com.demo.movie.models.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
  Optional<List<Movie>> findAllByTitleContainingOrderByTitle(String title);

  Optional<List<Movie>> findByIdIn(List<String> movieIds);
}
