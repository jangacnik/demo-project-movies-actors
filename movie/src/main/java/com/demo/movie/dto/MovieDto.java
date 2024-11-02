package com.demo.movie.dto;

import java.util.List;
import lombok.NonNull;

public record MovieDto(
    @NonNull String title,
    @NonNull String year,
    @NonNull String description,
    @NonNull List<String> actorIds,
    String coverImage,
    List<String> images) {}
