package com.bot.tg.meme.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@Getter
@Builder
public class BotMessage {
    private Long chatId;

    private LocalDateTime sentDateTime;

    @Builder.Default
    private Optional<Integer> repliedMessageId = Optional.empty();

    private BotMessageType messageType;
}
