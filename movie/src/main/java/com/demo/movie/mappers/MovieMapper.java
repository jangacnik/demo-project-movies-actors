package com.demo.movie.mappers;

import com.demo.movie.dto.MovieFullDto;
import com.demo.movie.dto.MovieShortDto;
import com.demo.movie.models.ActorShort;
import com.demo.movie.models.Movie;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieMapper {

  @Mapping(source = "title", target = "title")
  @Mapping(source = "year", target = "year")
  @Mapping(source = "description", target = "description")
  MovieShortDto toMovieShortDto(Movie movie);

  @Mapping(source = "actorShortList", target = "actorShortList")
  @Mapping(source = "movie", target = "movie")
  MovieFullDto toMovieFullDto(Movie movie, List<ActorShort> actorShortList);
}
