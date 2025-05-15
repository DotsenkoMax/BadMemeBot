package com.bot.tg.meme.subscriptions;

import java.util.List;

public interface SubscriptionRepository {
    List<Subscription> getAll();

    boolean addNew(Subscription subscription);
}
