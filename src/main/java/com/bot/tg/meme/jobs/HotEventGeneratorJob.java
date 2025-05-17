package com.bot.tg.meme.jobs;

import com.bot.tg.meme.config.ChatSessionConfig;
import com.bot.tg.meme.models.events.TgChatHotEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.repository.SubscriptionRepository;
import com.bot.tg.meme.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

// can be replaced with listener: advantage is in 1 job at a time
@Component
public class HotEventGeneratorJob {
    private static final Logger logger = LoggerFactory.getLogger(HotEventGeneratorJob.class);

    @Autowired
    SubscriptionRepository repository;

    @Autowired
    ActivityService activityService;

    @Autowired
    EventMessagesPublisher publisher;

    @Autowired
    ChatSessionConfig chatSessionConfig;

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    private void generateHotEvent() {
        final List<Long> allChats = repository.getAll().stream().map(it -> it.chatId).toList();

        for (Long chatId: allChats) {
            if (activityService.isHot(chatId)) {
                final var idKey = getIdempotancyKey(
                        chatId,
                        TgChatHotEvent.Type.GIF,
                        LocalDateTime.now(),
                        chatSessionConfig.getMinTimeBetweenGifsInOneChatInSeconds()
                );
                logger.info("Found hot chat: {}, publishing event: {}", chatId, idKey);

                publisher.publishHotEvent(
                        TgChatHotEvent.builder()
                                .eventType(TgChatHotEvent.Type.GIF)
                                .idempotencyKey(idKey)
                                .chatId(chatId)
                                .build()
                );
            }
        }

    }

    private static String getIdempotancyKey(Long chatId, TgChatHotEvent.Type type, LocalDateTime time, Integer timeModule) {
        final var epochSeconds = time.toEpochSecond(ZoneOffset.UTC);
        final var key = epochSeconds - (epochSeconds % (timeModule));

        return String.format("%s-%s-%s", chatId, type.toString(), key);
    }

}
