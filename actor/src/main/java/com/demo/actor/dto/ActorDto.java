package com.demo.actor.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;

public record ActorDto(@NonNull String firstName, @NonNull String lastName, @NonNull LocalDate birthdate,
                       List<String> movieIds) {
  }
