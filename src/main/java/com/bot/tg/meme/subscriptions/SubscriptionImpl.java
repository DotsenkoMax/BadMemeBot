package com.bot.tg.meme.subscriptions;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;



@Component
public class SubscriptionImpl implements SubscriptionRepository {
    private final HashSet<Subscription> allSubscriptions = new HashSet<>();

    @Override
    public List<Subscription> getAll() {
        return allSubscriptions.stream().toList();
    }

    @Override
    public synchronized boolean addNew(Subscription subscription) {
        return allSubscriptions.add(subscription);
    }
}
