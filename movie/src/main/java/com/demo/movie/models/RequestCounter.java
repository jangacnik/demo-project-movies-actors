package com.demo.movie.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("requestCounter")
@NoArgsConstructor
@AllArgsConstructor
public class RequestCounter {
  @Id private String id;
  private String requestController;
  private String requestControllerMethod;
  private int counter;
  private LocalDateTime lastRequest;

  public RequestCounter(
      String requestController,
      String requestControllerMethod,
      int counter,
      LocalDateTime lastRequest) {
    this.requestController = requestController;
    this.requestControllerMethod = requestControllerMethod;
    this.counter = counter;
    this.lastRequest = lastRequest;
  }
}
