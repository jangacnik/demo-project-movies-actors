package com.demo.movie.clients;

import com.demo.movie.models.ActorShort;
import com.demo.movie.models.request.ActorIdListRequest;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ActorClient {
  private final WebClient client;
  @Value("${client-actor-endpoint}")
  private String url;

  ActorClient() {
    client = WebClient.create(url);
  }

  public Flux<ActorShort> findActorsShort(List<String> ids) {
    return client
        .post()
        .uri(url + "/actor/list")
        .body(BodyInserters.fromValue(new ActorIdListRequest(ids)))
        .retrieve()
        .bodyToFlux(ActorShort.class)
        .cache(Duration.ofDays(1));
  }
}
