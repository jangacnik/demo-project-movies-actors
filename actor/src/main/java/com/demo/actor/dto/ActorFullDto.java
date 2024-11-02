package com.demo.actor.dto;

import com.demo.actor.models.Actor;
import com.demo.actor.models.MovieShort;
import java.util.List;

public record ActorFullDto(Actor actor, List<MovieShort> movies) {}
