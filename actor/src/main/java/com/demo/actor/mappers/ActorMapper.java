package com.demo.actor.mappers;

import com.demo.actor.dto.ActorFullDto;
import com.demo.actor.dto.ActorShortDto;
import com.demo.actor.models.Actor;
import com.demo.actor.models.MovieShort;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ActorMapper {
  ActorShortDto toActorShortDto(Actor actor);

  @Mapping(source = "actor", target = "actor")
  @Mapping(source = "movieShort", target = "movies")
  ActorFullDto toActorFullDto(Actor actor, List<MovieShort> movieShort);
}
