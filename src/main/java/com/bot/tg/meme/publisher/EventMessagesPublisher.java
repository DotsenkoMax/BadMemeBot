package com.bot.tg.meme.publisher;

import com.bot.tg.meme.models.events.TgChatHotEvent;
import com.bot.tg.meme.models.events.TgGifEvent;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

public class EventMessagesPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventMessagesPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventMessagesPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    public void publishCustomEvent(final Update update) {
        logger.info("Publishing Update event from Telegram");
        TgMessageEvent customSpringEvent = new TgMessageEvent(this, update);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishGifSendingEvent(final TgGifEvent update) {
        logger.info("Publishing Update event from Telegram in chat with id {}", update.chatId);
        applicationEventPublisher.publishEvent(update);
    }

    public void publishHotEvent(final TgChatHotEvent hotEvent) {
        logger.info("Publishing hot eventin chat with id {}", hotEvent.getChatId());
        applicationEventPublisher.publishEvent(hotEvent);
    }
}
