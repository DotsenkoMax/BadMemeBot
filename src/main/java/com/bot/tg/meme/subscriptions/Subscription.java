package com.bot.tg.meme.subscriptions;

import org.springframework.util.Assert;

import java.util.Objects;

// should be entity
public class Subscription {

    public final Long chatId;

    public Subscription(Long chatId) {
        Assert.notNull(chatId, "chatId");
        this.chatId = chatId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(chatId, that.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
