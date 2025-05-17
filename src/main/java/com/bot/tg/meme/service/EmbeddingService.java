package com.bot.tg.meme.service;

import java.util.List;
import java.util.Optional;

public interface EmbeddingService {
    Optional<String> getEmbeddings(List<String> text);
}
