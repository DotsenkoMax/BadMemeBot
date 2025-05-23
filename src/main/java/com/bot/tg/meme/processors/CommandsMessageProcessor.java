package com.bot.tg.meme.processors;

import com.bot.tg.meme.integrations.tenor.TenorClient;
import com.bot.tg.meme.integrations.tenor.model.request.SearchGifRequest;
import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.processors.annotation.TgCommand;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.bot.tg.meme.repository.BotMessagesRepository;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class CommandsMessageProcessor implements MessageProcessor {
    private final String botName;
    private final EventMessagesPublisher publisher;
    private final BotMessagesRepository repository;
    private final TenorClient tenorClient;

    private HashMap<String, Consumer<Update>> commandTask = new HashMap<>();


    public CommandsMessageProcessor(String botName,
                                    EventMessagesPublisher publisher,
                                    BotMessagesRepository botMessagesRepository,
                                    TenorClient tenorClient
    ) {
        this.botName = botName;
        this.publisher = publisher;
        this.repository = botMessagesRepository;
        this.tenorClient = tenorClient;

        Class<CommandsMessageProcessor> obj = CommandsMessageProcessor.class;
        for (Method method : obj.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TgCommand.class)) {
                TgCommand annotation = method.getAnnotation(TgCommand.class);
                commandTask.put(
                        annotation.value(), (update) -> {
                            try {
                                method.invoke(this, update);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            }
        }
    }

    @Override
    public void process(Update tgEvent) {
        final var command = tgEvent.message().text().substring(1).split(" ")[0].split("@");
        if (commandTask.containsKey(command[0])) {
            commandTask.get(command[0]).accept(tgEvent);
        } else {
            fallback(command[0], tgEvent);
        }
    }

    @Override
    public boolean isApplicable(Update tgEvent) {
        if (tgEvent.message() == null || tgEvent.message().text() == null) {
            return false;
        }
        final var text = tgEvent.message().text();
        if (!text.startsWith("/")) {
            return false;
        }

        String command = text.split(" ")[0];

        if (command.contains("@")) {
            String[] parts = command.split("@");
            return parts[1].equalsIgnoreCase(botName);
        }

        return true;
    }

    @TgCommand("tellme")
    private void processDescriptionCommand(Update tgEvent) {
        publisher.publishTgSendEvent(
                new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                        .chatId(tgEvent.message().chat().id())
                        .replyToMessageId(Optional.of(tgEvent.message().messageId()))
                        .message(Optional.of("Sosal?"))
                        .build())
        );
    }

    @TgCommand("lastpromt")
    private void processLastPromtCommand(Update tgEvent) {
        final var maybeGif = repository.getLastSentMessageInChat(tgEvent.message().chat().id(), BotMessageType.GIF);
        final var resultOnThisCommand = maybeGif.map(BotMessage::getEmbeddings)
                .flatMap(it -> it)
                .orElse("No promt.");

        publisher.publishTgSendEvent(
                new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                    .chatId(tgEvent.message().chat().id())
                    .replyToMessageId(Optional.of(tgEvent.message().messageId()))
                    .message(Optional.of(resultOnThisCommand))
                    .build()
                )
        );
    }

    @TgCommand("sendgif")
    private void sendGifWithPromt(Update tgEvent) {
        final var text = Arrays.stream(tgEvent.message().text().strip().split(" "))
                .skip(1).reduce("", (a, b) -> a + " " + b).strip();
        final var response = tenorClient.getSearchGif(
                SearchGifRequest.builder()
                        .q(text)
                        .limit(1)
                        .locale(Optional.of("ru_RU"))
                        .mediaFilter(Optional.of("mp4"))
                        .clientKey(Optional.of("test_client"))
                        .country(Optional.of("ES"))
                        .random(Optional.of(false))
                        .build()
        );
        if (!response.isEmpty()) {
            final var gifUrl = response.getFirst().getMp4Url();
            if (gifUrl.isPresent()) {
                publisher.publishTgSendEvent(
                        new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                                .chatId(tgEvent.message().chat().id())
                                .replyToMessageId(Optional.of(tgEvent.message().messageId()))
                                .gifUrl(gifUrl)
                                .build()
                        )
                );
                return;
            }
        }

        publisher.publishTgSendEvent(
                new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                    .chatId(tgEvent.message().chat().id())
                    .replyToMessageId(Optional.of(tgEvent.message().messageId()))
                    .message(Optional.of("No gif was found"))
                    .build()
                )
        );
    }

    private void fallback(String command, Update tgEvent) {
        publisher.publishTgSendEvent(
                new TgSendEvent(this, TgSendEvent.TgRawSendEvent.builder()
                        .chatId(tgEvent.message().chat().id())
                        .replyToMessageId(Optional.of(tgEvent.message().messageId()))
                        .message(Optional.of("Что это? %s".formatted(command)))
                        .build())
        );
    }
}
