package com.demo.movie.models;

import java.time.LocalDate;

public record ActorShort(String firstName, String lastName, LocalDate birthdate) {}
