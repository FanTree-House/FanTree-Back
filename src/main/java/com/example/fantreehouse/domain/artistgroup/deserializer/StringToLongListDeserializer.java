package com.example.fantreehouse.domain.artistgroup.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringToLongListDeserializer extends JsonDeserializer<List<Long>> {

    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String text = p.getText();
        if (text == null || text.isEmpty()) {
            return null;
        }
        return Arrays.stream(text.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }
}