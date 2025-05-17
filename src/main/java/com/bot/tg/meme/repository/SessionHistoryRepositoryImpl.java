package com.bot.tg.meme.repository;

import com.bot.tg.meme.config.ChatSessionConfig;
import com.bot.tg.meme.models.ChatSession;
import com.bot.tg.meme.models.TgMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SessionHistoryRepositoryImpl implements SessionHistoryRepository {

    private final HashMap<Long, LinkedList<ChatSession>> chatSessionsByChatId = new HashMap<>();

    @Autowired
    ChatSessionConfig chatSessionConfig;

    @Override
    public synchronized Optional<ChatSession> getLastSession(Long chatId) {
        return Optional.ofNullable(chatSessionsByChatId.get(chatId)).map(LinkedList::getLast);
    }

    @Override
    public synchronized void addMessage(TgMessage message) {
        final var allSessions = chatSessionsByChatId
                .computeIfAbsent(message.getChatId(), k -> new LinkedList<>());

        if (allSessions.isEmpty() || !allSessions.getLast().addToSession(message)) {
            allSessions.add(new ChatSession(message.getChatId(), message, chatSessionConfig.getMaxDistanceBetweenMessagesInOneGifSessionInSeconds()));

            if (allSessions.size() > 10) {
                while (allSessions.size() != 10) {
                    allSessions.removeFirst();
                }
            }
        }
    }
}
