package com.bot.tg.meme.listeners;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.search.engine.StaticGifProvider;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendAnimation;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EchoListener {
    private static final Logger logger = LoggerFactory.getLogger(EchoListener.class);

    @Autowired
    TelegramClient client;

    @EventListener
    @Async
    public void onApplicationEvent(TgMessageEvent event) {
        if (event.tgUpdate.message().text() == null) {
            logger.info("Received spring custom event that is not text {}", event.tgUpdate.message());
            return;
        }
        logger.info("Received spring custom event {}", event.tgUpdate.message());
        client.sendMessage(event.tgUpdate, event.tgUpdate.message().text());
    }
}
