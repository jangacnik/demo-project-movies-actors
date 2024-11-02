package com.demo.actor.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
  ID("id"),
  FIRST_NAME("firstName"),
  LAST_NAME("lastName");

  private final String databaseFieldName;
}
