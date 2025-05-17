package com.bot.tg.meme.repository;

import java.util.List;

public interface SubscriptionRepository {
    List<Subscription> getAll();

    boolean addNew(Subscription subscription);
}
