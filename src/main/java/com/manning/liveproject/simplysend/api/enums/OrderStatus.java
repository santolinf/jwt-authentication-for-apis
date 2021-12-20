package com.manning.liveproject.simplysend.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    REQUESTED("REQUESTED"),
    APPROVED("APPROVED"),
    DENIED("DENIED");

    @Getter
    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromValue(String text) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.value.equals(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException(text);
    }
}
