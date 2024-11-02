package com.demo.actor;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.demo.actor.clients.MovieClient;
import com.demo.actor.dto.ActorFullDto;
import com.demo.actor.dto.ActorListDto;
import com.demo.actor.models.Actor;
import com.demo.actor.models.MovieShort;
import com.demo.actor.repositories.ActorRepository;
import com.demo.actor.services.ActorService;
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
class ActorServiceTest {
  @MockBean private ActorRepository actorRepository;
  @MockBean private MovieClient movieClient;
  @Autowired private ActorService actorService;
  private static Actor actorValid;

  @BeforeAll
  static void beforeAll() {
    actorValid = new Actor("John", "Doe", LocalDate.of(1999, 1, 20), List.of("id1", "id2"), "id1");
  }

  @Test
  void findByNameHappyPathTest() throws NotFoundException {
    when(actorRepository.findAllByFirstNameOrLastNameOrderByLastName(any(), any()))
        .thenReturn(Optional.of(List.of(actorValid)));
    ActorListDto actorList = actorService.findByName("John");
    assertEquals(1, actorList.actorList().size());
    verify(actorRepository, times(1)).findAllByFirstNameOrLastNameOrderByLastName(any(), any());
  }

  @Test
  void findByNameNotFoundTest() {
    when(actorRepository.findAllByFirstNameOrLastNameOrderByLastName(any(), any()))
        .thenReturn(Optional.empty());
    assertThrowsExactly(NotFoundException.class, () -> actorService.findByName("John"));
  }

  @Test
  void findByIdNotFoundTest() {
    when(actorRepository.findById(any())).thenReturn(Optional.empty());
    assertThrowsExactly(NotFoundException.class, () -> actorService.getActorById("John"));
  }

  @Test
  void findActorFullHappyPathTest() {
    when(actorRepository.findById(any())).thenReturn(Optional.of(actorValid));
    MovieShort movieShort = new MovieShort("Test Movie", "1999", "Test lorem ipsum");
    when(movieClient.findMoviesShort(any())).thenReturn(Flux.just(movieShort));
    ActorFullDto actorFullDto = actorService.findActorFull("id1");
    assertNotNull(actorFullDto);
    assertEquals(1, actorFullDto.movies().size());
    assertEquals(movieShort, actorFullDto.movies().get(0));
    assertEquals(actorValid, actorFullDto.actor());
  }

  @Test
  void findActorFullNoMoviesTest() {
    when(actorRepository.findById(any())).thenReturn(Optional.of(actorValid));
    when(movieClient.findMoviesShort(any())).thenReturn(Flux.empty());
    ActorFullDto actorFullDto = actorService.findActorFull("id1");
    assertNotNull(actorFullDto);
    assertEquals(0, actorFullDto.movies().size());
    assertEquals(actorValid, actorFullDto.actor());
  }

  @Test
  void findActorFullActorNotExistTest() {
    when(actorRepository.findById(any())).thenReturn(Optional.empty());
    assertThrowsExactly(NoSuchElementException.class, () -> actorService.findActorFull("id1"));
  }
}
