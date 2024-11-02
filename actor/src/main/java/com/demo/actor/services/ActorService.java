package com.demo.actor.services;

import com.demo.actor.clients.MovieClient;
import com.demo.actor.dto.ActorDto;
import com.demo.actor.dto.ActorFullDto;
import com.demo.actor.dto.ActorListDto;
import com.demo.actor.dto.ActorShortDto;
import com.demo.actor.mappers.ActorMapper;
import com.demo.actor.models.Actor;
import com.demo.actor.models.MovieShort;
import com.demo.actor.models.enums.SortField;
import com.demo.actor.repositories.ActorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class ActorService {
  private final ActorRepository actorRepository;
  private final ActorMapper actorMapper;
  private final MovieClient movieClient;

  /**
   * Save new actor to DB, does not update actors if one exists with same id;
   *
   * @param actor Actor to be inserted of type {@link ActorDto}
   */
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
    Optional<Actor> actorOptional = actorRepository.findById(id);
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

  public Page<ActorShortDto> findByPage(
      int pageNumber, int pageSize, Direction sortDirection, SortField sortField) {
    Pageable pageable =
        PageRequest.of(pageNumber, pageSize, sortDirection, sortField.getDatabaseFieldName());
    return actorRepository.findAll(pageable).map(actorMapper::toActorShortDto);
  }

  public Flux<ActorShortDto> findByListOfIds(List<String> ids) throws NotFoundException {
    Optional<List<Actor>> optionalActorList = actorRepository.findByIdIn(ids);
    if (optionalActorList.isEmpty()) {
      throw new NotFoundException();
    }
    return Flux.fromIterable(
        optionalActorList.get().stream().map(actorMapper::toActorShortDto).toList());
  }

  private Optional<Actor> findById(String id) {
    return actorRepository.findById(id);
  }

  public ActorFullDto findActorFull(String id) {
    Actor actor = actorRepository.findById(id).orElseThrow();
    List<MovieShort> moveiShortList = new ArrayList<>();
    Flux<MovieShort> movieShortFlux = movieClient.findMoviesShort(actor.getMovieIds());
    Mono<Void> m = Mono.when(movieShortFlux);
    movieShortFlux.collectList().subscribe(moveiShortList::addAll);
    m.block();
    return actorMapper.toActorFullDto(actor, moveiShortList);
  }

  /**
   * Converts ActorDto to Actor object
   *
   * @param actorDto Actor data of type {@link ActorDto}
   * @param id Actor ID
   * @return returns {@link Actor}
   */
  private Actor convertToActor(ActorDto actorDto, @Nullable String id) {
    return new Actor(
        actorDto.firstName(), actorDto.lastName(), actorDto.birthdate(), actorDto.movieIds(), id);
  }
}
