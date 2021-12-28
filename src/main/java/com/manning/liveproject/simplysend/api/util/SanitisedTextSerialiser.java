package com.manning.liveproject.simplysend.api.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.util.Map;

/**
 * Custom serializer to "clean" text-based outputs to prevent any XSS attacks.
 * Here is a very simple and naive implementation (just to show how to configure Jackson).
 */
public class SanitisedTextSerialiser extends StdSerializer<String> {

    private static final Map<String, String> PATTERNS_TO_ESCAPE = ImmutableMap.of(
            "<script", "&lt;script",
            "</script", "&lt;/script"
    );

    public SanitisedTextSerialiser() {
        super(String.class);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String sanitisedValue = PATTERNS_TO_ESCAPE.keySet().stream()
                .reduce(value, (v, k) -> v.replaceAll(k, PATTERNS_TO_ESCAPE.get(k)));
        gen.writeString(sanitisedValue);
    }
}
