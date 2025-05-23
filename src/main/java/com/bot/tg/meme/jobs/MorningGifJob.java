package com.bot.tg.meme.jobs;

import com.bot.tg.meme.integrations.tenor.TenorClient;
import com.bot.tg.meme.integrations.tenor.model.request.SearchGifRequest;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MorningGifJob {
    private static final Logger logger = LoggerFactory.getLogger(MorningGifJob.class);

    @Autowired
    private TenorClient tenorClient;

    @Autowired
    private EventMessagesPublisher publisher;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 6 * * *") // Every morning at 6 am
    public void sendMorning6Gif() {
        sendRandomGifWithTag("доброе утро", "SendMorning6Gif");
    }

    @Scheduled(cron = "0 0 18 ? * FRI")
    public void sendFridayEvening6Gif() {
        sendRandomGifWithTag("гачимучи", " SendFridayEvening6Gif");
    }

    public void runInBatch() {
        sendMorning6Gif();
    }

    private void sendRandomGifWithTag(String tag, String jobName) {
        logger.info("{} started...", jobName);

        final var gifList = tenorClient.getSearchGif(
                SearchGifRequest.builder()
                        .q(tag)
                        .limit(1)
                        .random(Optional.of(true))
                    .build()
        ).stream().toList();

        logger.info("{} get list of {} elements", jobName, gifList.size());
        final var subscriptions = subscriptionRepository.getAll();
        logger.info("{} sends to all {} subscriptions", jobName, subscriptions.size());

        gifList.forEach(pic ->
                subscriptions.forEach(
                        it -> publisher.publishTgSendEvent(
                                new TgSendEvent(this,
                                        TgSendEvent.TgRawSendEvent.builder()
                                                .gifUrl(pic.getMp4Url())
                                                .chatId(it.chatId)
                                                .build()
                                )
                        )
                )
        );
        logger.info("{} finished...", jobName);
    }


}
