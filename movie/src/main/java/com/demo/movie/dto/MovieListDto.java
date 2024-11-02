package com.demo.movie.dto;

import com.demo.movie.models.Movie;
import java.util.List;

public record MovieListDto(List<Movie> movieList, long total) {}
