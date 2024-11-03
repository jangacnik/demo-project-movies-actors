package com.demo.movie;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.movie.clients.ActorClient;
import com.demo.movie.dto.MovieFullDto;
import com.demo.movie.models.ActorShort;
import com.demo.movie.models.Movie;
import com.demo.movie.repositories.MovieRepository;
import com.demo.movie.services.MovieService;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;

@SpringBootTest
@ContextConfiguration
@TestPropertySource("/application-dev.properties")
class MovieServiceTest {
  @MockBean
  private MovieRepository movieRepository;
  @MockBean private ActorClient actorClient;
  @Autowired
  private MovieService movieService;
  private static Movie movieValid;

  @BeforeAll
  static void beforeAll() {
    movieValid = new Movie("Look back", "2024", "Lorem ipsum", List.of("actorId1"), "coverImage.jpeg", List.of("img1.jpg", "img2.jpg"), "movieId");
  }

  @Test
  void findByTitleHappyPathTest() throws NotFoundException {
    when(movieRepository.findAllByTitleContainingOrderByTitle(any()))
        .thenReturn(Optional.of(List.of(movieValid)));
    movieService.findByTitle("LookBack");
    verify(movieRepository, times(1)).findAllByTitleContainingOrderByTitle(any());
  }

  @Test
  void findByTitleNotFoundTest() {
    when(movieRepository.findAllByTitleContainingOrderByTitle(any()))
        .thenReturn(Optional.empty());
    assertThrowsExactly(NotFoundException.class, () -> movieService.findByTitle(("Look back")));
  }

  @Test
  void findByIdNotFoundTest() {
    when(movieRepository.findById(any())).thenReturn(Optional.empty());
    assertThrowsExactly(NotFoundException.class, () -> movieService.getById("movieId123"));
  }

  @Test
  void findMovieFullHappyPathTest() {
    when(movieRepository.findById(any())).thenReturn(Optional.of(movieValid));
    ActorShort actorShort = new ActorShort("John", "Doe", LocalDate.of(1999,1,20));
    when(actorClient.findActorsShort(any())).thenReturn(Flux.just(actorShort));
    MovieFullDto movieFullDto = movieService.findMovieFull("movieId");
    assertNotNull(movieFullDto);
    assertEquals(1, movieFullDto.actorShortList().size());
    assertEquals(movieValid, movieFullDto.movie());
  }

  @Test
  void findMovieFullNoActorsTest() {
    when(movieRepository.findById(any())).thenReturn(Optional.of(movieValid));
    when(actorClient.findActorsShort(any())).thenReturn(Flux.empty());
    MovieFullDto actorFullDto = movieService.findMovieFull("id1");
    assertNotNull(actorFullDto);
    assertEquals(0, actorFullDto.actorShortList().size());
    assertNotNull(actorFullDto.movie());
  }

  @Test
  void findActorFullActorNotExistTest() {
    when(movieRepository.findById(any())).thenReturn(Optional.empty());
    assertThrowsExactly(NoSuchElementException.class, () -> movieService.findMovieFull("id1"));
  }
}
