package com.demo.movie.services;

import com.demo.movie.models.RequestCounter;
import com.demo.movie.repositories.RequestCounterRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestCounterService {
  private final RequestCounterRepository requestCounterRepository;

  public void incrementCounter(String requestController, String requestControllerMethod) {
    Optional<RequestCounter> requestCounterOptional =
        requestCounterRepository.findByRequestControllerAndRequestControllerMethod(
            requestController, requestControllerMethod);
    if (requestCounterOptional.isEmpty()) {
      requestCounterRepository.insert(
          new RequestCounter(requestController, requestControllerMethod, 1, LocalDateTime.now()));
    } else {
      RequestCounter requestCounter = requestCounterOptional.get();
      requestCounter.setCounter(requestCounter.getCounter() + 1);
      requestCounter.setLastRequest(LocalDateTime.now());
      requestCounterRepository.save(requestCounter);
    }
  }

  public List<RequestCounter> getAllCounters() {
    return requestCounterRepository.findAll();
  }
}
