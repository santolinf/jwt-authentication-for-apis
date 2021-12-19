package com.manning.liveproject.simplysend.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemType {
    HARDWARE("HARDWARE"),
    SOFTWARE("SOFTWARE"),
    STATIONARY("STATIONARY"),
    TRAINING("TRAINING"),
    MISC("MISC");

    @Getter
    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return value;
    }

    @JsonCreator
    public static ItemType fromValue(String text) {
        for (ItemType type : ItemType.values()) {
            if (type.value.equals(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException(text);
    }
}
