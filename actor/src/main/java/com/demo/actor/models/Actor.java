package com.demo.actor.models;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("actors")
public class Actor {
  @NonNull
  private final String firstName;
  @NonNull
  private final String lastName;
  @NonNull
  private final LocalDate birthdate;
  private final List<String> movieIds;
  @Id
  private String id;
}
