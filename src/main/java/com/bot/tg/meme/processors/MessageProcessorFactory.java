package com.bot.tg.meme.processors;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageProcessorFactory {
    private final List<MessageProcessor> messageProcessors;

    public MessageProcessorFactory(List<MessageProcessor> messageProcessors) {
        this.messageProcessors = messageProcessors;
    }

    public List<MessageProcessor> getProcessor(Update tgEvent) {
        return messageProcessors
            .stream()
            .filter(it -> it.isApplicable(tgEvent))
            .toList();
    }
}
