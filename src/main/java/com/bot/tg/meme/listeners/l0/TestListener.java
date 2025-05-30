package com.bot.tg.meme.listeners.l0;

import com.bot.tg.meme.integrations.giphy.model.response.Gif;
import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.bot.tg.meme.integrations.giphy.model.request.TranslateGifRequest.translateGifRequest;

@Component
@ConditionalOnProperty(name = "listeners.enable-echo-listener", havingValue = "true", matchIfMissing = false)
public class TestListener {

    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Autowired
    TelegramBot bot;
    @Autowired
    GiphyClient giphyClient;

    @Autowired
    TelegramClient telegramClient;


    @EventListener
    @Async
    public void onApplicationEvent(TgMessageEvent event) throws IOException {
        logger.info("Received TgMessage to process for test purposes, message: {}", event.tgUpdate.message());
        if (event.tgUpdate.message() != null && event.tgUpdate.message().text() == null) {
            logger.info("Received spring custom event that is not text {}", event.tgUpdate.message());
            return;
        }

        final Gif gif = giphyClient.getTranslateGif(translateGifRequest().s(event.tgUpdate.message().text()).build());
        final Optional<String> urlMp4 = gif.getMp4Url();

        logger.info("Received GIF with url {}", urlMp4);
        urlMp4.ifPresent(url -> telegramClient.sendMessage(
                TgSendEvent.TgRawSendEvent.builder()
                        .chatId(event.tgUpdate.message().chat().id())
                        .replyToMessageId(Optional.of(event.tgUpdate.message().messageId()))
                        .gifUrl(Optional.of(url))
                        .build()
        ));
    }
}
