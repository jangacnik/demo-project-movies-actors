package com.demo.actor.dto;

import com.demo.actor.models.Actor;
import java.util.List;

public record ActorListDto(List<Actor> actorList, long total) {}
