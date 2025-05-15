package com.bot.tg.meme.models.events;

import com.pengrad.telegrambot.model.Update;
import org.springframework.context.ApplicationEvent;

public class TgMessageEvent extends ApplicationEvent {

    public final Update tgUpdate;
    public TgMessageEvent(Object source, Update update) {
        super(source);
        tgUpdate = update;
    }
}
