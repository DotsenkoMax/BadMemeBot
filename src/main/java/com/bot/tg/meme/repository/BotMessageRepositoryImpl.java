package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.BotMessage;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;

@Component
public class BotMessageRepositoryImpl implements BotMessagesRepository {

    private final HashMap<Long, TreeSet<BotMessage>> messages = new HashMap<>();

    @Override
    public synchronized Optional<BotMessage> getLastSentMessageInChat(Long chatId) {
        if (messages.containsKey(chatId)) {
            return Optional.of(messages.get(chatId).getLast());
        }
        return Optional.empty();
    }

    @Override
    public synchronized void addMessage(Long chatId, BotMessage botMessage) {
        messages
            .computeIfAbsent(chatId, k -> new TreeSet<>(Comparator.comparing(BotMessage::getSentDateTime)))
            .add(botMessage);
    }
}
