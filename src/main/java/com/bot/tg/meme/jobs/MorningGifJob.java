package com.bot.tg.meme.jobs;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.giphy.model.request.RandomGifRequest;
import com.bot.tg.meme.integrations.giphy.model.response.Gif;
import com.bot.tg.meme.models.events.TgGifEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.subscriptions.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

import static com.bot.tg.meme.integrations.giphy.model.request.SearchGifRequest.searchGifRequest;

@Component
public class MorningGifJob {
    private static final Logger logger = LoggerFactory.getLogger(MorningGifJob.class);

    @Autowired
    private GiphyClient gifClient;

    @Autowired
    private EventMessagesPublisher publisher;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private static final Random rng = new Random(System.currentTimeMillis());

    @Scheduled(cron = "0 */1 * * * *") // Every 2 minutes, at zero seconds
    public void sendMorningGif() {
        logger.info("MorningGifJob started...");

        final var gifList = gifClient.getRandomGif(
                        RandomGifRequest.randomGifRequest()
                                .tag("morning, cringe, memes")
                                .countryCode("ES")
                                .rating(List.of("r", "pg"))
                                .build()
                ).getMp4Url().stream().toList();

        logger.info("MorningGifJob get list of {} elements", gifList.size());
        final var subscriptions = subscriptionRepository.getAll();
        logger.info("MorningGifJob sends to all {} subscriptions", subscriptions.size());

        gifList.forEach(pic ->
            subscriptions.forEach(
                it -> publisher.publishGifSendingEvent(
                    new TgGifEvent(this, it.chatId, pic)
                )
            )
        );
        logger.info("MorningGifJob finished...");
    }
}
