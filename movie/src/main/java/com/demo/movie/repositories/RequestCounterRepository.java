package com.demo.movie.repositories;

import com.demo.movie.models.RequestCounter;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestCounterRepository extends MongoRepository<RequestCounter, String> {
  Optional<RequestCounter> findByRequestControllerAndRequestControllerMethod(
      String controller, String method);
}
