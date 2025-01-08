package com.project.logistic_management_2.enums;

import lombok.Getter;

@Getter
public enum Pagination {
    TEN(10, "Limit: 10 records");

    private final Integer size;
    private final String note;

    Pagination(int size, String note) {
        this.size = size;
        this.note = note;
    }
}
