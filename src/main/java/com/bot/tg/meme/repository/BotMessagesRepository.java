package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.BotMessage;

import java.util.Optional;

public interface BotMessagesRepository {
    Optional<BotMessage> getLastSentMessageInChat(Long chatId);

    void addMessage(Long chatId, BotMessage botMessage);
}
