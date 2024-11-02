package com.demo.movie.dto;

import com.demo.movie.models.ActorShort;
import com.demo.movie.models.Movie;
import java.util.List;

public record MovieFullDto(Movie movie, List<ActorShort> actorShortList) {}
