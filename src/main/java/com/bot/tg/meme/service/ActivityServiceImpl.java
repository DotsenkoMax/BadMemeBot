package com.bot.tg.meme.service;

import com.bot.tg.meme.config.ChatSessionConfig;
import com.bot.tg.meme.models.ChatSession;
import com.bot.tg.meme.repository.SessionHistoryRepository;
import com.bot.tg.meme.utils.Date;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    SessionHistoryRepository sessionHistoryRepository;

    @Autowired
    ChatSessionConfig chatSessionConfig;

    @Override
    public boolean isHot(Long chatId) {
        Optional<ChatSession> lastChatSession = sessionHistoryRepository.getLastSession(chatId);

        if (lastChatSession.isEmpty()) {
            return false;
        }

        return Date.isDistanceLessThen(lastChatSession.get().maxDateTimeInSession, LocalDateTime.now(),
                Duration.ofSeconds(chatSessionConfig.getMaxTimeWhenGifSessionGetsOutdatedInSeconds()))
                && lastChatSession.get().messages.size() >= chatSessionConfig.getMinNumberOfMessagesInOneGifSession();
    }
}
