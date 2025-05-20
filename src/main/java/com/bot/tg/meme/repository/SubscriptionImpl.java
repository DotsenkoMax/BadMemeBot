package com.bot.tg.meme.repository;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;



@Component
public class SubscriptionImpl implements SubscriptionRepository {
    private final HashSet<Subscription> allSubscriptions = new HashSet<>();

    public SubscriptionImpl() {
        allSubscriptions.add(new Subscription(405217254L)); // me
        allSubscriptions.add(new Subscription(-1001559610005L)); // no levch
    }

    @Override
    public List<Subscription> getAll() {
        return allSubscriptions.stream().toList();
    }

    @Override
    public synchronized boolean addNew(Subscription subscription) {
        return allSubscriptions.add(subscription);
    }
}
