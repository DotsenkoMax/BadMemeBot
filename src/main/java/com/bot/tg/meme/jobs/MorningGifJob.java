package com.bot.tg.meme.jobs;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.giphy.model.request.TranslateGifRequest;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.repository.SubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MorningGifJob {
    private static final Logger logger = LoggerFactory.getLogger(MorningGifJob.class);

    @Autowired
    private GiphyClient gifClient;

    @Autowired
    private EventMessagesPublisher publisher;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 6 * * *") // Every morning at 6 am
    public void sendMorning6Gif() {
        sendRandomGifWithTag("good morning - sign #morning #sign", "SendMorning6Gif", List.of("g"));
    }
//
//    @Scheduled(cron = "0 30 6 * * *") // Every morning at 6 30 am
//    public void sendMorning630Gif() {
//        sendRandomGifWithTag("brush teeth #children", "SendMorning630Gif", List.of("g", "pg"));
//    }
//
//    @Scheduled(cron = "0 0 7 * * *") // Every morning at 6 30 am
//    public void sendMorning730Gif() {
//        sendRandomGifWithTag("push ups #morning #gym", "SendMorning700Gif", List.of("r", "pg13"));
//    }
//
//    @Scheduled(cron = "0 30 7 * * *") // Every morning at 6 30 am
//    public void sendMorning7Gif() {
//        sendRandomGifWithTag("breakfast #table #food", "SendMorning730Gif", List.of("g"));
//    }
//
//    @Scheduled(cron = "0 0 8 * * *") // Every morning at 6 30 am
//    public void sendMorning8Gif() {
//        sendRandomGifWithTag("bruh work again #work #rape", "SendMorning8Gif", List.of("r", "pg13"));
//    }

    public void runInBatch() {
        sendMorning6Gif();
//        sendMorning630Gif();
//        sendMorning7Gif();
//        sendMorning730Gif();
//        sendMorning8Gif();
    }

    private void sendRandomGifWithTag(String tag, String jobName, List<String> rating) {
        logger.info("{} started...", jobName);

        final var gifList = gifClient.getTranslateGif(
                TranslateGifRequest.translateGifRequest().s(tag).rating(rating).build()
        ).getMp4Url().stream().toList();

        logger.info("{} get list of {} elements", jobName, gifList.size());
        final var subscriptions = subscriptionRepository.getAll();
        logger.info("{} sends to all {} subscriptions", jobName, subscriptions.size());

        gifList.forEach(pic ->
                subscriptions.forEach(
                        it -> publisher.publishTgSendEvent(
                                new TgSendEvent(this,
                                        TgSendEvent.TgRawSendEvent.builder()
                                                .gifUrl(Optional.of(pic))
                                                .chatId(it.chatId)
                                                .build()
                                )
                        )
                )
        );
        logger.info("{} finished...", jobName);
    }


}
