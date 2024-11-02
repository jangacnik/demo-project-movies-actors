package com.demo.actor;

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

import com.demo.actor.dto.ActorDto;
import com.demo.actor.dto.ActorShortDto;
import com.demo.actor.models.Actor;
import com.demo.actor.models.enums.SortField;
import com.demo.actor.repositories.ActorRepository;
import com.demo.actor.services.ActorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoQueryException;
import com.mongodb.MongoWriteException;
import java.time.LocalDate;
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
class ActorMvcTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ActorService actorService;
  @Mock private ActorRepository actorRepository;

  @Autowired private ObjectMapper objectMapper;

  private ActorDto actorDtoValid;
  private Actor actorValid;

  @BeforeEach
  void before() {
    actorDtoValid = new ActorDto("John", "Doe", LocalDate.of(1999, 1, 20), List.of("id1", "id2"));
    actorValid = new Actor("John", "Doe", LocalDate.of(1999, 1, 20), List.of("id1", "id2"), "id1");
  }

  @Test
  void saveActorToDbErrorTest() throws Exception {
    doThrow(MongoWriteException.class).when(actorRepository).insert((Actor) any());
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/actor")
                .content(objectMapper.writeValueAsString(actorDtoValid)))
        .andExpect(status().is(415));
  }

  @Test
  void getActorByIdTestHappyPath() throws Exception {
    doReturn(actorValid).when(actorService).getActorById(any());
    MvcResult result =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/actor/{id}", "id1"))
            .andExpect(status().isOk())
            .andReturn();
    assertNotNull(result.getResponse().getContentAsString());
    Actor actorResponse =
        objectMapper.readValue(result.getResponse().getContentAsString(), Actor.class);
    assertEquals(actorValid, actorResponse);
  }

  @Test
  void getActorByIdTestError() throws Exception {
    doThrow(MongoQueryException.class).when(actorService).getActorById(any());
    mockMvc
        .perform(MockMvcRequestBuilders.get("/actor/{id}", "id1"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void findAllByPageTestHappyPathNoParams() throws Exception {
    List<ActorShortDto> actorList = new ArrayList<>();
    actorList.add(new ActorShortDto("test", "test", LocalDate.of(1999, 1, 20), "test"));
    Page<ActorShortDto> pagedResponse = new PageImpl<>(actorList);
    when(actorService.findByPage(anyInt(), anyInt(), any(), any())).thenReturn(pagedResponse);
    ArgumentCaptor<Integer> captorInt = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Direction> captorDirection = ArgumentCaptor.forClass(Direction.class);
    ArgumentCaptor<SortField> captorSortField = ArgumentCaptor.forClass(SortField.class);
    MvcResult mvcResult =
        mockMvc
            .perform(MockMvcRequestBuilders.get("/actor?page="))
            .andExpect(status().isOk())
            .andReturn();
    verify(actorService, times(1))
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
    List<ActorShortDto> actorList = new ArrayList<>();
    actorList.add(new ActorShortDto("test", "test", LocalDate.of(1999, 1, 20), "test"));
    Page<ActorShortDto> pagedResponse = new PageImpl<>(actorList);
    when(actorService.findByPage(anyInt(), anyInt(), any(), any())).thenReturn(pagedResponse);
    ArgumentCaptor<Integer> captorInt = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Direction> captorDirection = ArgumentCaptor.forClass(Direction.class);
    ArgumentCaptor<SortField> captorSortField = ArgumentCaptor.forClass(SortField.class);
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get(
                    "/actor?page=3&pageSize=20&sortField=LAST_NAME&sortDirection=ASC"))
            .andExpect(status().isOk())
            .andReturn();
    verify(actorService, times(1))
        .findByPage(
            captorInt.capture(),
            captorInt.capture(),
            captorDirection.capture(),
            captorSortField.capture());
    assertEquals(3, captorInt.getAllValues().get(0));
    assertEquals(20, captorInt.getAllValues().get(1));
    assertEquals(SortField.LAST_NAME, captorSortField.getValue());
    assertEquals(Direction.ASC, captorDirection.getValue());
    assertFalse(mvcResult.getResponse().getContentAsString().isEmpty());
  }

  @Test
  void findAllByPageTestInvalidParams() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get(
                "/actor?sortField=BIRTH_DATE&sortDirection=ASC"))
        .andExpect(status().isBadRequest())
        .andReturn();
  }
}
