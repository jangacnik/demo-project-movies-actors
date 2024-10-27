package com.demo.movie.controllers;

import com.demo.movie.dto.MovieDto;
import com.demo.movie.dto.MovieListDto;
import com.demo.movie.models.Movie;
import com.demo.movie.services.MovieService;
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
@RequestMapping("/movie")
@RequiredArgsConstructor
@Log4j2
public class MovieController {

  private final MovieService movieService;

  @PostMapping()
  public ResponseEntity<Void> save(@RequestBody @NonNull MovieDto actor) {
    movieService.insertMovie(actor);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Movie> getById(@PathVariable String id) throws NotFoundException {
    return ResponseEntity.ok(movieService.getById(id));
  }

  @GetMapping("/find/{title}")
  public ResponseEntity<MovieListDto> getByTitle(@PathVariable String title)
      throws NotFoundException {
    return ResponseEntity.ok(movieService.findByTitle(title));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) throws NotFoundException {
    movieService.deleteMovieById(id);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Movie> update(@RequestBody MovieDto actorDto, @PathVariable String id)
      throws NotFoundException {
    return ResponseEntity.ok(movieService.updateMovie(actorDto, id));
  }
}
