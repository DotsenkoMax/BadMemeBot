package com.bot.tg.meme.processors;

import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@Component
public class HardcodedPhrasesMessageProcessor implements MessageProcessor {

    private final EventMessagesPublisher publisher;

    private final Map<String, String> hardcodedPhrasesToHardCodedGifs = Map.of(
            "da", "static/da_pizda.mp4",
            "да", "static/da_pizda.mp4",
            "пизда", "static/da_pizda.mp4",
            "pizda", "static/da_pizda.mp4"
    );

    public HardcodedPhrasesMessageProcessor(EventMessagesPublisher publisher) {
        this.publisher = publisher;
    }


    @Override
    public void process(Update tgEvent) {
        final var phrase = tgEvent.message().text().toLowerCase().strip();

        if (hardcodedPhrasesToHardCodedGifs.containsKey(phrase)) {
            sendGif(
                    hardcodedPhrasesToHardCodedGifs.get(phrase),
                    tgEvent.message().chat().id(),
                    tgEvent.message().messageId()
            );
        }
    }

    @Override
    public boolean isApplicable(Update tgEvent) {
        return tgEvent.message() != null && tgEvent.message().text() != null
                && !tgEvent.message().text().isBlank()
                ;
    }

    private void sendGif(String path, Long chatId, Integer messageReplyTo) {
        URL resourceUrl = this.getClass().getClassLoader().getResource(path);
        if (resourceUrl == null) {
            return;
        }
        try {
            File file = new File(resourceUrl.toURI());
            publisher.publishTgSendEvent(
                    new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                            .chatId(chatId)
                            .replyToMessageId(Optional.of(messageReplyTo))
                            .fileAnimation(Optional.of(file))
                            .build())
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
