package com.bot.tg.meme.processors;

import com.pengrad.telegrambot.model.Update;

public interface MessageProcessor {
    void process(Update tgEvent);
    boolean isApplicable(Update tgEvent);
}