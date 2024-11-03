package com.demo.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.movie.dto.MovieDto;
import com.demo.movie.dto.MovieShortDto;
import com.demo.movie.models.Movie;
import com.demo.movie.models.enums.SortField;
import com.demo.movie.repositories.MovieRepository;
import com.demo.movie.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoQueryException;
import com.mongodb.MongoWriteException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
@TestPropertySource("/application-dev.properties")
class MovieMvcTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MovieService movieService;
  @Mock
  private MovieRepository movieRepository;

  @Autowired private ObjectMapper objectMapper;


  private MovieDto movieDtoValid;
  private Movie movieValid;

  @BeforeEach
  void before() {
    movieDtoValid = new MovieDto("Look back", "2024", "Lorem ipsum", List.of("actorId1"),"coverImage.jpeg", List.of("img1.jpg", "img2.jpg"));
    movieValid = new Movie("Look back", "2024", "Lorem ipsum", List.of("actorId1"), "coverImage.jpeg", List.of("img1.jpg", "img2.jpg"), "movieId");
  }


  @Test
  void saveMovieToDbErrorTest() throws Exception {
    doThrow(MongoWriteException.class).when(movieRepository).insert((Movie) any());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/movie")
                .content(objectMapper.writeValueAsString(movieDtoValid)))
        .andExpect(status().is(415));
  }

  @Test
  void getMovieByIdTestHappyPath() throws Exception {
    doReturn(movieValid).when(movieService).getById(any());
    MvcResult result =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/movie/{id}", "id1"))
            .andExpect(status().isOk())
            .andReturn();
    assertNotNull(result.getResponse().getContentAsString());
    Movie movieResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), Movie.class);
    assertEquals(movieValid, movieResponse);
  }

  @Test
  void getMovieByIdTestError() throws Exception {
    doThrow(MongoQueryException.class).when(movieService).getById(any());
    mockMvc
        .perform(MockMvcRequestBuilders.get("/movie/{id}", "id1"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void findAllByPageTestHappyPathNoParams() throws Exception {
    List<MovieShortDto> actorList = new ArrayList<>();
    actorList.add(new MovieShortDto("Look back", "2024", "Lorem ipsum"));
    Page<MovieShortDto> pagedResponse = new PageImpl<>(actorList);
    when(movieService.findByPage(anyInt(), anyInt(), any(), any())).thenReturn(pagedResponse);
    ArgumentCaptor<Integer> captorInt = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Direction> captorDirection = ArgumentCaptor.forClass(Direction.class);
    ArgumentCaptor<SortField> captorSortField = ArgumentCaptor.forClass(SortField.class);
    MvcResult mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/movie?page="))
            .andExpect(status().isOk())
            .andReturn();
    verify(movieService, times(1))
        .findByPage(
            captorInt.capture(),
            captorInt.capture(),
            captorDirection.capture(),
            captorSortField.capture());
    assertEquals(0, captorInt.getAllValues().get(0));
    assertEquals(10, captorInt.getAllValues().get(1));
    assertEquals(SortField.ID, captorSortField.getValue());
    assertEquals(Direction.DESC, captorDirection.getValue());
    assertFalse(mvcResult.getResponse().getContentAsString().isEmpty());
  }

  @Test
  void findAllByPageTestHappyPathWithParams() throws Exception {
    List<MovieShortDto> movieList = new ArrayList<>();
    movieList.add(new MovieShortDto("Look back", "2024", "Lorem ipsum"));
    Page<MovieShortDto> pagedResponse = new PageImpl<>(movieList);
    when(movieService.findByPage(anyInt(), anyInt(), any(), any())).thenReturn(pagedResponse);
    ArgumentCaptor<Integer> captorInt = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Direction> captorDirection = ArgumentCaptor.forClass(Direction.class);
    ArgumentCaptor<SortField> captorSortField = ArgumentCaptor.forClass(SortField.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(
                    "/movie?page=3&pageSize=20&sortField=TITLE&sortDirection=ASC"))
            .andExpect(status().isOk())
            .andReturn();
    verify(movieService, times(1))
        .findByPage(
            captorInt.capture(),
            captorInt.capture(),
            captorDirection.capture(),
            captorSortField.capture());
    assertEquals(3, captorInt.getAllValues().get(0));
    assertEquals(20, captorInt.getAllValues().get(1));
    assertEquals(SortField.TITLE, captorSortField.getValue());
    assertEquals(Direction.ASC, captorDirection.getValue());
    assertFalse(mvcResult.getResponse().getContentAsString().isEmpty());
  }

  @Test
  void findAllByPageTestInvalidParams() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/movie?sortField=BIRTH_DATE&sortDirection=ASC"))
        .andExpect(status().isBadRequest())
        .andReturn();
  }
}
