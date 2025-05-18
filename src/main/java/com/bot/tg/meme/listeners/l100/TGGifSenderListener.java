package com.bot.tg.meme.listeners.l100;

import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.repository.BotMessagesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Clock;
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
    public void onApplicationEvent(TgSendEvent event) {
        logger.info("Received TGGifSenderListener to process {}", event.tgRawSendEvent.chatId);
        telegramClient.sendGif(event.tgRawSendEvent);

        logger.info("Adding TgGifEvent to the repository {}", event.tgRawSendEvent.chatId);
        repository.addMessage(
                event.tgRawSendEvent.chatId, BotMessage.builder()
                        .messageType(BotMessageType.GIF)
                        .repliedMessageId(event.tgRawSendEvent.replyToMessageId)
                        .chatId(event.tgRawSendEvent.chatId)
                        .sentDateTime(LocalDateTime.now(Clock.systemUTC()))
                        .build());
    }
}
