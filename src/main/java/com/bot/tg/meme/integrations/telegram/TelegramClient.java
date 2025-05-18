package com.bot.tg.meme.integrations.telegram;

import com.bot.tg.meme.models.events.TgSendEvent;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotName;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component
public class TelegramClient {
    private static final Logger logger = LoggerFactory.getLogger(TelegramClient.class);

    @Autowired
    private TelegramBot bot;

    // @RateLimiter
    public void sendMessage(TgSendEvent.TgRawSendEvent tgSendEvent) {
        AbstractSendRequest<? extends AbstractSendRequest<?>> request;

        if (tgSendEvent.message.isPresent()) {
            request = new SendMessage(tgSendEvent.chatId, tgSendEvent.message.get());
        } else if (tgSendEvent.gifUrl.isPresent()) {
            request = new SendAnimation(tgSendEvent.chatId, tgSendEvent.gifUrl.get());
        } else if (tgSendEvent.fileAnimation.isPresent()) {
            request = new SendAnimation(tgSendEvent.chatId, tgSendEvent.fileAnimation.get());
        } else {
            logger.warn("TgSendEvent has unsupported type");
            return;
        }

        tgSendEvent.replyToMessageId.ifPresent(id -> {
                request.replyParameters(new ReplyParameters(id));
        });

        SendResponse response = bot.execute(request);
        logger.info("Sent on Update message id {}", response.message().messageId());

    }

    @Cacheable("telegramBotName")
    public BotName getMe() {
        final var response = bot.execute(new GetMyName());
        logger.info("Received bot name {}", response.botName().name());
        return response.botName();
    }
}
