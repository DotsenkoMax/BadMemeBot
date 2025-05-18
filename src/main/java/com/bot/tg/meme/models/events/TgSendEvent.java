package com.bot.tg.meme.models.events;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class TgSendEvent extends ApplicationEvent {
    public final TgRawSendEvent tgRawSendEvent;

    public TgSendEvent(Object source, TgRawSendEvent rawSendEvent) {
        super(source);
        this.tgRawSendEvent = Objects.requireNonNull(rawSendEvent);
    }


    @Builder
    @Getter
    public static class TgRawSendEvent {
        public final Long chatId;

        @Builder.Default
        public final Optional<Integer> replyToMessageId = Optional.empty();

        @Builder.Default
        public final Optional<String> gifUrl = Optional.empty();

        @Builder.Default
        public final Optional<String> message = Optional.empty();

        @Builder.Default
        public final Optional<File> fileAnimation = Optional.empty();
    }
}
