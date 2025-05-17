package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.ChatSession;
import com.bot.tg.meme.models.TgMessage;

import java.util.Optional;

public interface SessionHistoryRepository {
    Optional<ChatSession> getLastSession(Long chatId);

    void addMessage(TgMessage message);
}
