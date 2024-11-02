package com.demo.movie.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document("movies")
public class Movie {
  @NonNull private final String title;
  @NonNull private final String year;
  @NonNull private final String description;
  @NonNull private final List<String> actorIds;
  private final String coverImage;
  private final List<String> images;
  @Id private String id;
}
