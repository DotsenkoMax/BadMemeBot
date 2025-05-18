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
public class TGSenderListener {
    private static final Logger logger = LoggerFactory.getLogger(TGSenderListener.class);

    @Autowired
    TelegramClient telegramClient;

    @Autowired
    BotMessagesRepository repository;

    @EventListener
    @Async("level100Executor")
    public void onApplicationEvent(TgSendEvent event) {
        var raw = event.tgRawSendEvent;
        boolean isGif = raw.message.isEmpty();

        logger.info("Received TgSendEvent to process, chatId: {}", raw.chatId);
        telegramClient.sendMessage(raw);

        BotMessageType type = isGif ? BotMessageType.GIF : BotMessageType.TEXT;
        logger.info("Adding Tg{}Event to the repository {}", type, raw.chatId);

        repository.addMessage(
                raw.chatId,
                BotMessage.builder()
                        .messageType(type)
                        .repliedMessageId(raw.replyToMessageId)
                        .chatId(raw.chatId)
                        .sentDateTime(LocalDateTime.now(Clock.systemUTC()))
                        .build()
        );
    }
}
