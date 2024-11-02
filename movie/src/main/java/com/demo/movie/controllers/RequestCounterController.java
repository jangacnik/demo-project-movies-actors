package com.demo.movie.controllers;

import com.demo.movie.models.RequestCounter;
import com.demo.movie.services.RequestCounterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
@Log4j2
public class RequestCounterController {
  private final RequestCounterService requestCounterService;

  @GetMapping
  public ResponseEntity<List<RequestCounter>> getAllCounters() {
    return ResponseEntity.ok(requestCounterService.getAllCounters());
  }
}
