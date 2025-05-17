package com.bot.tg.meme.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Getter @Setter @Builder
public class TgMessage {
    Long chatId;

    LocalDateTime sentDateTime;

    @Builder.Default
    Optional<String> message = Optional.empty();

    @Builder.Default
    Optional<Integer> messageId = Optional.empty();

    @Builder.Default
    Optional<String> senderName = Optional.empty();


    @Builder.Default
    Language countryCode = Language.RU;
}
