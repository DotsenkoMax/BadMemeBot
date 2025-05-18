package com.bot.tg.meme.publisher;

import com.bot.tg.meme.models.events.TgChatHotEvent;
import com.bot.tg.meme.models.events.TgSendEvent;
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
    public void publishRawTgEvent(final Update update) {
        logger.info("Publishing raw update event from Telegram");
        TgMessageEvent customSpringEvent = new TgMessageEvent(this, update);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }

    public void publishTgSendEvent(final TgSendEvent update) {
        logger.info("Publishing Update event from Telegram in chat with id {}", update.tgRawSendEvent.chatId);
        applicationEventPublisher.publishEvent(update);
    }

    public void publishHotEvent(final TgChatHotEvent hotEvent) {
        logger.info("Publishing hot event in chat with id {} {}", hotEvent.getChatId(), hotEvent.getIdempotencyKey());
        applicationEventPublisher.publishEvent(hotEvent);
    }
}
