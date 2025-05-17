package com.bot.tg.meme.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@NoArgsConstructor
@Validated
public class ChatSessionConfig {
    @NotNull
    private Integer maxDistanceBetweenMessagesInOneGifSessionInSeconds;
    @NotNull
    private Integer minNumberOfMessagesInOneGifSession;
    @NotNull
    private Integer maxTimeWhenGifSessionGetsOutdatedInSeconds;
    @NotNull
    private Integer minTimeBetweenGifsInOneChatInSeconds;
}
