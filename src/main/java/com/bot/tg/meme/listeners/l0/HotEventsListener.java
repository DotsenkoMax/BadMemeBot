package com.bot.tg.meme.listeners.l0;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.models.TgMessage;
import com.bot.tg.meme.models.events.TgChatHotEvent;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.repository.SessionHistoryRepository;
import com.bot.tg.meme.service.EmbeddingService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bot.tg.meme.integrations.giphy.model.request.TranslateGifRequest.translateGifRequest;


@Component
public class HotEventsListener {
    final Logger logger = LoggerFactory.getLogger(HotEventsListener.class);

    @Autowired
    SessionHistoryRepository sessionHistoryRepository;

    @Autowired
    EmbeddingService embeddingService;

    @Autowired
    GiphyClient giphyClient;

    @Autowired
    private EventMessagesPublisher publisher;

    private final ConcurrentHashMap<String, EventState> locks = new ConcurrentHashMap<>();


    @Setter
    private static class EventState {
        boolean sent = false;

    }

    @EventListener
    @Async("level0Executor")
    public void onApplicationEvent(TgChatHotEvent event) {
        logger.info("Received HotEvent to process {} {}", event.getChatId(), event.getIdempotencyKey());

        EventState lock = locks.computeIfAbsent(event.getIdempotencyKey(), k -> new EventState());

        synchronized (lock) {
            if (lock.sent) {
                return;
            }

            final var maybeLastSession = sessionHistoryRepository.getLastSession(event.getChatId());

            if (maybeLastSession.isEmpty()) {
                return;
            }

            Optional<String> maybeEmbeddings = embeddingService.getEmbeddings(
                    maybeLastSession.get().getMessages().stream()
                            .map(TgMessage::getMessage)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList()
            );

            if (maybeEmbeddings.isEmpty()) {
                logger.warn("Found empty embeddings for event {} {}", event.getChatId(), event.getIdempotencyKey());
                return;
            }
            final var embeddings = maybeEmbeddings.get();

            logger.info("Got embeddings from the message: {} {}", embeddings, event.getIdempotencyKey());


            if (event.getEventType() == TgChatHotEvent.Type.GIF) {
                final var gif = giphyClient.getTranslateGif(translateGifRequest()
                        .s(embeddings)
                        .rating(List.of( "pg", "pg13", "r"))
                        .build());

                if (gif.getMp4Url().isEmpty()) {
                    logger.info("Mp4Url is empty {} {}", event.getChatId(), event.getIdempotencyKey());
                    return;
                }
                logger.info("Hot event was successfully processed, embeddings: {} ", embeddings);

                publisher.publishTgSendEvent(new TgSendEvent(this,
                    TgSendEvent.TgRawSendEvent.builder()
                            .chatId(event.getChatId())
                            .gifUrl(Optional.of(gif.getMp4Url().get()))
                            .embeddings(Optional.of(embeddings))
                            .build()
                    )
                );

                lock.setSent(true);
            }
        }
    }
}
