package com.bot.tg.meme.config;

import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Configuration
@EnableAsync
public class BotConfiguration {
    @Bean
    TelegramBot initBot(@Value("${bot.token}") String botToken) {
        return new TelegramBot(botToken);
    }

    @Bean
    EventMessagesPublisher initPublisher(TelegramBot bot, ApplicationEventPublisher applicationEventPublisher) {
        EventMessagesPublisher publisher = new EventMessagesPublisher(applicationEventPublisher);
        bot.setUpdatesListener(updates -> {
            updates.stream().filter(it -> it.message().text() != null).forEach(publisher::publishCustomEvent);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                // got bad response from telegram
                e.response().errorCode();
                e.response().description();
            } else {
                // probably network error
                e.printStackTrace();
            }
        });
        return publisher;
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster =
                new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return eventMulticaster;
    }

    @Bean
    @ConfigurationProperties(prefix = "chat.session")
    public ChatSessionConfig getDefaultConfigs() {
        return new ChatSessionConfig();
    }

}
