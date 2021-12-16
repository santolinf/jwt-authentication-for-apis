package com.manning.liveproject.simplysend.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    MGR("MGR"),
    REPORTEE("REPORTEE"),
    ADMIN("ADMIN");

    private final String value;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Role fromValue(String text) {
        for (Role role : Role.values()) {
            if (String.valueOf(role.value).equals(text)) {
                return role;
            }
        }
        return null;
    }
}
