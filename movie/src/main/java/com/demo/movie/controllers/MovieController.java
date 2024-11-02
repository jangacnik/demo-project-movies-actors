package com.demo.movie.controllers;

import com.demo.movie.dto.MovieDto;
import com.demo.movie.dto.MovieFullDto;
import com.demo.movie.dto.MovieListDto;
import com.demo.movie.dto.MovieShortDto;
import com.demo.movie.models.Movie;
import com.demo.movie.models.enums.SortField;
import com.demo.movie.models.request.MovieListRequest;
import com.demo.movie.services.MovieService;
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
  public Mono<Movie> getById(@PathVariable String id) throws NotFoundException {
    return Mono.just(movieService.getById(id));
  }

  @GetMapping("/find/{title}")
  public Mono<MovieListDto> getByTitle(@PathVariable String title) throws NotFoundException {
    return Mono.just(movieService.findByTitle(title));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable String id) throws NotFoundException {
    movieService.deleteMovieById(id);
    return ResponseEntity.ok(null);
  }

  @PutMapping("/{id}")
  public Mono<Movie> update(@RequestBody MovieDto actorDto, @PathVariable String id)
      throws NotFoundException {
    return Mono.just(movieService.updateMovie(actorDto, id));
  }

  @PostMapping("/list")
  public Flux<MovieShortDto> findAllByIds(@RequestBody MovieListRequest ids)
      throws NotFoundException {
    return movieService.findByListOfIds(ids.movieIds());
  }

  @GetMapping
  public Mono<Page<MovieShortDto>>  findAllByPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "ID") SortField sortField,
      @RequestParam(defaultValue = "DESC") Direction sortDirection) {
    return Mono.just(movieService.findByPage(page, pageSize, sortDirection, sortField));
  }

  @GetMapping("/{id}/full")
  public ResponseEntity<MovieFullDto> getActorFull(@PathVariable String id) {
    return ResponseEntity.ok(movieService.findMovieFull(id));
  }
}
