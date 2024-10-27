package com.demo.actor.controllers;

import com.demo.actor.dto.ActorDto;
import com.demo.actor.dto.ActorListDto;
import com.demo.actor.models.Actor;
import com.demo.actor.services.ActorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actor")
@RequiredArgsConstructor
@Log4j2
public class ActorController {
  private final ActorService actorService;

  @PostMapping()
  public ResponseEntity<Void> save(@RequestBody @NonNull ActorDto actor) {
    actorService.saveActorToDb(actor);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Actor> getById(@PathVariable String id) throws NotFoundException {
    return ResponseEntity.ok(actorService.getActorById(id));
  }

  @GetMapping("/{name}")
  public ResponseEntity<ActorListDto> getByName(@PathVariable String name)
      throws NotFoundException {
    return ResponseEntity.ok(actorService.findByName(name));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) throws NotFoundException {
    actorService.deleteActorById(id);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Actor> update(@RequestBody ActorDto actorDto, @PathVariable String id)
      throws NotFoundException {
    return ResponseEntity.ok(actorService.updateActor(actorDto, id));
  }
}
