package com.bot.tg.meme.listeners;

import com.bot.tg.meme.integrations.giphy.model.response.Gif;
import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.telegram.TelegramClient;
import com.bot.tg.meme.models.events.TgMessageEvent;
import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.bot.tg.meme.integrations.giphy.model.request.RandomGifRequest.randomGifRequest;

@Component
public class GifRandomListener {

    private static final Logger logger = LoggerFactory.getLogger(EchoListener.class);

    @Autowired
    TelegramBot bot;
    @Autowired
    GiphyClient giphyClient;

    @Autowired
    TelegramClient telegramClient;


    @EventListener
    @Async
    public void onApplicationEvent(TgMessageEvent event) throws IOException {
        logger.info("Received GifRandomMessage to process {}", event.tgUpdate.message());

        if (event.tgUpdate.message().text() == null) {
            logger.info("Received spring custom event that is not text {}", event.tgUpdate.message());
            return;
        }

        final Gif gif = giphyClient.getRandomGif(randomGifRequest().build());
        final Optional<String> urlMp4 = gif.getMp4Url();

        logger.info("Received GIF with url {}", urlMp4);
        urlMp4.ifPresent(url -> telegramClient.sendGif(event.tgUpdate, url));
    }
}
