package com.bot.tg.meme.listeners;

import com.bot.tg.meme.models.events.TgMessageEvent;
import com.bot.tg.meme.subscriptions.Subscription;
import com.bot.tg.meme.subscriptions.SubscriptionRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionListener {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionListener.class);

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @EventListener
    @Async
    public void onApplicationEvent(TgMessageEvent event) {
        logger.info("Received NewSubscription to process {}", event.tgUpdate.message().chat().id());

        subscriptionRepository.addNew(new Subscription(event.tgUpdate.message().chat().id()));
    }
}
