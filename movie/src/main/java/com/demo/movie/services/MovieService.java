package com.demo.movie.services;

import com.demo.movie.clients.ActorClient;
import com.demo.movie.dto.MovieDto;
import com.demo.movie.dto.MovieFullDto;
import com.demo.movie.dto.MovieListDto;
import com.demo.movie.dto.MovieShortDto;
import com.demo.movie.mappers.MovieMapper;
import com.demo.movie.models.ActorShort;
import com.demo.movie.models.Movie;
import com.demo.movie.models.enums.SortField;
import com.demo.movie.repositories.MovieRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieService {
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final ActorClient actorClient;
  public void insertMovie(MovieDto actor) {
    movieRepository.insert(convertToMovie(actor, null));
  }

  public Movie updateMovie(MovieDto movieDto, String id) throws NotFoundException {
    Movie movie = convertToMovie(movieDto, id);
    Optional<Movie> actorOptional = findById(movie.getId());
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    return movieRepository.save(movie);
  }

  public void deleteMovieById(String id) throws NotFoundException {
    Optional<Movie> actorOptional = findById(id);
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    movieRepository.delete(actorOptional.get());
  }

  public Movie getById(String id) throws NotFoundException {
    Optional<Movie> actorOptional = findById(id);
    if (actorOptional.isEmpty()) {
      throw new NotFoundException();
    }
    return actorOptional.get();
  }

  public MovieListDto findByTitle(String title) throws NotFoundException {
    Optional<List<Movie>> movieList =
        movieRepository.findAllByTitleContainingOrderByTitle(title);
    if (movieList.isEmpty()) {
      throw new NotFoundException();
    }
    return new MovieListDto(movieList.get(), movieList.get().size());
  }

  public Page<MovieShortDto> findByPage(
      int pageNumber, int pageSize, Direction sortDirection, SortField sortField) {
    Pageable pageable =
        PageRequest.of(pageNumber, pageSize, sortDirection, sortField.getDatabaseFieldName());
    return movieRepository.findAll(pageable).map(movieMapper::toMovieShortDto);
  }

  public Flux<MovieShortDto> findByListOfIds(List<String> ids) throws NotFoundException {
    Optional<List<Movie>> optionalMovieList = movieRepository.findByIdIn(ids);
    if (optionalMovieList.isEmpty()) {
      throw new NotFoundException();
    }
    return Flux.fromIterable(optionalMovieList.get().stream().map(movieMapper::toMovieShortDto).
        toList());
  }

  public MovieFullDto findMovieFull(String id) {
    Movie movie = movieRepository.findById(id).orElseThrow();
    List<ActorShort> actorShortList = new ArrayList<>();
    Flux<ActorShort> movieShortFlux = actorClient.findActorsShort(movie.getActorIds());
    Mono<Void> m = Mono.when(movieShortFlux);
    movieShortFlux.collectList().subscribe(actorShortList::addAll);
    m.block();
    return movieMapper.toMovieFullDto(movie, actorShortList);
  }

  private Optional<Movie> findById(String id) {
    return movieRepository.findById(id);
  }

  private Movie convertToMovie(MovieDto movieDto, @Nullable String id) {
    return new Movie(movieDto.title(), movieDto.year(), movieDto.description(), movieDto.actorIds() ,movieDto.coverImage(), movieDto.images(),id);
  }
}
