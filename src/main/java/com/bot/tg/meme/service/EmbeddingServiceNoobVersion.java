package com.bot.tg.meme.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmbeddingServiceNoobVersion implements EmbeddingService {

    @Override
    public Optional<String> getEmbeddings(List<String> text) {
        return Optional.of(text.stream()
                .flatMap(it -> Arrays.stream(it.split("\\s+")))
                .map(String::strip)
                .filter(it -> it.length() >= 3)
                .collect(Collectors.toMap(Function.identity(), it -> 1, Integer::sum))
                .entrySet()
                .stream()
                .sorted((l, r) -> r.getValue() - l.getValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .reduce("", (a, b) -> a + " " + b))
        ;
    }
}
