package com.bot.tg.meme.models.events;

import com.pengrad.telegrambot.model.Update;
import org.springframework.context.ApplicationEvent;

import java.util.Optional;

public class TgGifEvent extends ApplicationEvent {
    public final Long chatId;
    public final Optional<Integer> replyToMessageId;
    public final String gifUrl;

    public TgGifEvent(Object source, Long chatId, String gifUrl) {
        this(source, chatId, gifUrl, Optional.empty());
    }
    public TgGifEvent(Object source, Long chatId, String gifUrl, Optional<Integer> replyToMessageId) {
        super(source);
        this.chatId = chatId;
        this.gifUrl = gifUrl;
        this.replyToMessageId = replyToMessageId;
    }
}
