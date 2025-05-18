package com.bot.tg.meme.listeners.l0;

import com.bot.tg.meme.models.Language;
import com.bot.tg.meme.models.TgMessage;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.processors.MessageProcessorFactory;
import com.bot.tg.meme.repository.SessionHistoryRepository;
import com.bot.tg.meme.repository.Subscription;
import com.bot.tg.meme.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class BaseMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(BaseMessageListener.class);

    @Autowired
    SessionHistoryRepository repository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    MessageProcessorFactory messageProcessorFactory;

    @EventListener
    @Async("level0Executor")
    public void onApplicationEventForSessionsHistory(TgMessageEvent event) {
        if (event.tgUpdate.message() != null
                && event.tgUpdate.message().text() != null
        ) {
            logger.info("Received spring custom event {}", event.tgUpdate.message());

            repository.addMessage(TgMessage.builder()
                    .countryCode(Language.RU)
                    .chatId(event.tgUpdate.message().chat().id())
                    .message(Optional.of(event.tgUpdate.message().text()))
                    .sentDateTime(LocalDateTime.ofEpochSecond(event.tgUpdate.message().date(), 0, ZoneOffset.UTC))
                    .messageId(Optional.of(event.tgUpdate.message().messageId()))
                    .build());
        }
    }

    @EventListener
    @Async("level0Executor")
    public void onApplicationEventForSubscriptions(TgMessageEvent event) {
        if (event.tgUpdate.message() != null) {
            logger.info("Received subscription to process, chatId: {}", event.tgUpdate.message().chat().id());

            subscriptionRepository.addNew(new Subscription(event.tgUpdate.message().chat().id()));
        }
    }

    @EventListener
    @Async("level0Executor")
    public void onApplicationEventForCustomEventsProcessing(TgMessageEvent event) {
        logger.info("Received custom event processing to process, chatId: {}", event.tgUpdate.message().chat().id());
        messageProcessorFactory.getProcessor(event.tgUpdate).forEach(it -> it.process(event.tgUpdate));
    }
}
