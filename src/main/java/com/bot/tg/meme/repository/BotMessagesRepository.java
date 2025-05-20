package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;

import java.util.Optional;

public interface BotMessagesRepository {
    Optional<BotMessage> getLastSentMessageInChat(Long chatId);

    Optional<BotMessage> getLastSentMessageInChat(Long chatId, BotMessageType messageType);


    void addMessage(Long chatId, BotMessage botMessage);
}
