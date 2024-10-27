package com.demo.movie.services;

import com.demo.movie.dto.MovieDto;
import com.demo.movie.dto.MovieListDto;
import com.demo.movie.models.Movie;
import com.demo.movie.repositories.MovieRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
  private final MovieRepository movieRepository;
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

  private Optional<Movie> findById(String id) {
    return movieRepository.findById(id);
  }

  private Movie convertToMovie(MovieDto movieDto, @Nullable String id) {
    return new Movie(movieDto.title(), movieDto.year(), movieDto.description(), movieDto.actorIds() ,movieDto.coverImage(), movieDto.images(),id);
  }
}
