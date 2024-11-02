package com.demo.actor.controllers;

import com.demo.actor.clients.MovieClient;
import com.demo.actor.dto.ActorDto;
import com.demo.actor.dto.ActorFullDto;
import com.demo.actor.dto.ActorListDto;
import com.demo.actor.dto.request.ActorListRequst;
import com.demo.actor.dto.ActorShortDto;
import com.demo.actor.models.Actor;
import com.demo.actor.models.enums.SortField;
import com.demo.actor.services.ActorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/actor")
@RequiredArgsConstructor
@Log4j2
public class ActorController {
  private final ActorService actorService;
  private final MovieClient client;

  @PostMapping()
  public ResponseEntity<Void> save(@RequestBody @NonNull ActorDto actor) {
    actorService.saveActorToDb(actor);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/{id}")
  public Mono<Actor> getById(@PathVariable String id) throws NotFoundException {
    return Mono.just(actorService.getActorById(id));
  }

  @GetMapping("/names")
  public Mono<ActorListDto> getByName(@RequestParam String name)
      throws NotFoundException {
    return Mono.just(actorService.findByName(name));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) throws NotFoundException {
    actorService.deleteActorById(id);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/{id}")
  public Mono<Actor> update(@RequestBody ActorDto actorDto, @PathVariable String id)
      throws NotFoundException {
    return Mono.just(actorService.updateActor(actorDto, id));
  }

  @GetMapping
  public Mono<Page<ActorShortDto>>  findAllByPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "ID") SortField sortField,
      @RequestParam(defaultValue = "DESC") Direction sortDirection) {
    return Mono.just(actorService.findByPage(page, pageSize, sortDirection, sortField));
  }

  @PostMapping("/list")
  public Flux<ActorShortDto> findAllByIds(@RequestBody ActorListRequst ids)
      throws NotFoundException {
    return actorService.findByListOfIds(ids.actorIds());
  }

  @GetMapping("/{id}/full")
  public ResponseEntity<ActorFullDto> getActorFull(@PathVariable String id) {
    return ResponseEntity.ok(actorService.findActorFull(id));
  }
}
