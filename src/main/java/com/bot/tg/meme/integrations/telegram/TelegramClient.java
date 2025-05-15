package com.bot.tg.meme.integrations.telegram;

import com.bot.tg.meme.models.events.TgGifEvent;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.pengrad.telegrambot.request.SendAnimation;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TelegramClient {
    private static final Logger logger = LoggerFactory.getLogger(TelegramClient.class);

    @Autowired
    private TelegramBot bot;

    public void sendGif(Update update, String gifUrl) {
        final var response = bot.execute(
                new SendAnimation(update.message().chat().id(), gifUrl)
                        .replyParameters(new ReplyParameters(update.message().messageId()))
        );
        logger.info("Sent gif on Update {}", response);
    }

    public void sendGif(TgGifEvent tgGifEvent) {
        final com.pengrad.telegrambot.response.SendResponse response;
        response = tgGifEvent.replyToMessageId.map(integer -> bot.execute(
                new SendAnimation(tgGifEvent.chatId, tgGifEvent.gifUrl)
                        .replyParameters(new ReplyParameters(integer))
        )).orElseGet(() -> bot.execute(
                new SendAnimation(tgGifEvent.chatId, tgGifEvent.gifUrl)
        ));
        logger.info("Sent gif on Update message id {}", response.message().messageId());
    }

//    public void sendGif(String gifUrl) {
//        final var response = bot.execute(
//                new SendAnimation(update.message().chat().id(), gifUrl)
//                        .replyParameters(new ReplyParameters(update.message().messageId()))
//        );
//        logger.info("Sent gif on Update {}", response);
//    }
    public void sendMessage(Update update, String message) {
        final var response = bot.execute(
                new SendMessage(update.message().chat().id(), message)
                        .replyParameters(new ReplyParameters(update.message().messageId()))
        );
        logger.info("Sent message on Update {}", response);
    }

}
