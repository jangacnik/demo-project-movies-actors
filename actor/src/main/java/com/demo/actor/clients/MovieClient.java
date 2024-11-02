package com.demo.actor.clients;

import com.demo.actor.dto.request.MovieIdListRequest;
import com.demo.actor.models.MovieShort;
import java.time.Duration;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Log4j2
@Component
public class MovieClient {

  @Value("${client-movie-endpoint}")
  private String url;
  private WebClient client;

  MovieClient() {
    client = WebClient.create(url);
  }
  public Flux<MovieShort> findMoviesShort(List<String> ids) {
    return client
            .post()
            .uri(url+"/movie/list")
            .body(BodyInserters.fromValue(new MovieIdListRequest(ids)))
            .retrieve()
            .bodyToFlux(MovieShort.class)
            .cache(Duration.ofDays(1));
  }
}
