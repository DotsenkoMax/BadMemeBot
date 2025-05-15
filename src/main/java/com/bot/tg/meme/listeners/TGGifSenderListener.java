package com.bot.tg.meme.listeners;

import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.events.TgGifEvent;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.subscriptions.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TGGifSenderListener {
    private static final Logger logger = LoggerFactory.getLogger(TGGifSenderListener.class);

    @Autowired
    TelegramClient telegramClient;

    @EventListener
    @Async
    public void onApplicationEvent(TgGifEvent event) {
        logger.info("Received TGGifSenderListener to process {}", event.chatId);
        telegramClient.sendGif(event);
    }
}
