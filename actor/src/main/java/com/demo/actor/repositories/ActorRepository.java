package com.demo.actor.repositories;

import com.demo.actor.models.Actor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActorRepository extends MongoRepository<Actor, String> {
  Optional<List<Actor>> findAllByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrderByLastName(
      String firstName, String lastName);

  Optional<List<Actor>> findByIdIn(List<String> actorIds);
}
