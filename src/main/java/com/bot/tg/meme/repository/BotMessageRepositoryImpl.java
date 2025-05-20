package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public synchronized Optional<BotMessage> getLastSentMessageInChat(Long chatId, BotMessageType type) {
        if (messages.containsKey(chatId)) {
            for (Iterator<BotMessage> it = messages.get(chatId).descendingIterator(); it.hasNext(); ) {
                BotMessage each = it.next();
                if (each.getMessageType().equals(type)) {
                    return Optional.of(each);
                }
            }
            return Optional.empty();
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
