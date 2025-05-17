package com.bot.tg.meme.listeners.l0;

import com.bot.tg.meme.models.Language;
import com.bot.tg.meme.models.TgMessage;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.repository.SessionHistoryRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class BaseMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(BaseMessageListener.class);

    @Autowired
    SessionHistoryRepositoryImpl repository;

    @EventListener
    @Async("level0Executor")
    public void onApplicationEvent(TgMessageEvent event) {
        if (event.tgUpdate.message() != null
                && event.tgUpdate.message().text() != null
        ) {
            logger.info("Received spring custom event {}", event.tgUpdate.message());
            repository.addMessage(TgMessage.builder()
                    .countryCode(Language.RU)
                    .chatId(event.tgUpdate.message().chat().id())
                    .message(Optional.of(event.tgUpdate.message().text()))
                    .sentDateTime(LocalDateTime.now())
                    .messageId(Optional.of(event.tgUpdate.message().messageId()))
                    .build());
        }
    }
}
