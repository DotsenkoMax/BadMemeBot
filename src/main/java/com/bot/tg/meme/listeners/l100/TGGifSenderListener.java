package com.bot.tg.meme.listeners.l100;

import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;
import com.bot.tg.meme.models.events.TgGifEvent;
import com.bot.tg.meme.repository.BotMessagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TGGifSenderListener {
    private static final Logger logger = LoggerFactory.getLogger(TGGifSenderListener.class);

    @Autowired
    TelegramClient telegramClient;

    @Autowired
    BotMessagesRepository repository;

    @EventListener
    @Async("level100Executor")
    public void onApplicationEvent(TgGifEvent event) {
        logger.info("Received TGGifSenderListener to process {}", event.chatId);
        telegramClient.sendGif(event);

        logger.info("Adding TgGifEvent to the repository {}", event.chatId);
        repository.addMessage(
                event.chatId, BotMessage.builder()
                        .messageType(BotMessageType.GIF)
                        .repliedMessageId(event.replyToMessageId)
                        .chatId(event.chatId)
                        .sentDateTime(LocalDateTime.now())
                        .build());
    }
}
