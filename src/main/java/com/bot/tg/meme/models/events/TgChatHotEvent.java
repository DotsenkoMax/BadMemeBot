package com.bot.tg.meme.models.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter @Getter @Builder
public class TgChatHotEvent {
    Long chatId;

    String idempotencyKey;

    @Builder.Default
    Type eventType = Type.GIF;

    public enum Type {
        GIF,
        TEXT
    }
}
