package com.demo.movie.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
  ID("id"),
  TITLE("title"),
  YEAR("year");

  private final String databaseFieldName;
}
