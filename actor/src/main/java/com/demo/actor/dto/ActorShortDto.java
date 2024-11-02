package com.demo.actor.dto;

import java.time.LocalDate;
import lombok.NonNull;

public record ActorShortDto(
    @NonNull String firstName, @NonNull String lastName, @NonNull LocalDate birthdate, String id) {}
