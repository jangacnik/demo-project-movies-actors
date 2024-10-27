package com.demo.actor.services;

import com.demo.actor.dto.ActorDto;
import com.demo.actor.dto.ActorListDto;
import com.demo.actor.models.Actor;
import com.demo.actor.repositories.ActorRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActorService {
  private final ActorRepository actorRepository;

  public void saveActorToDb(ActorDto actor) {
    actorRepository.insert(convertToActor(actor, null));
  }

  public Actor updateActor(ActorDto actorDto, String id) throws NotFoundException {
    Actor actor = convertToActor(actorDto, id);
    Optional<Actor> actorOptional = findById(actor.getId());
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    return actorRepository.save(actor);
  }

  public void deleteActorById(String id) throws NotFoundException {
    Optional<Actor> actorOptional = findById(id);
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    actorRepository.delete(actorOptional.get());
  }

  public Actor getActorById(String id) throws NotFoundException {
    Optional<Actor> actorOptional = findById(id);
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    return actorOptional.get();
  }

  public ActorListDto findByName(String name) throws NotFoundException {
    Optional<List<Actor>> actorList =
        actorRepository.findAllByFirstNameOrLastNameOrderByLastName(name, name);
    if (actorList.isEmpty()) {
      throw new NotFoundException();
    }
    return new ActorListDto(actorList.get(), actorList.get().size());
  }

  private Optional<Actor> findById(String id) {
    return actorRepository.findById(id);
  }

  private Actor convertToActor(ActorDto actorDto, @Nullable String id) {
    return new Actor(actorDto.firstName(), actorDto.lastName(), actorDto.birthdate(), actorDto.movieIds(), id);
  }
}
