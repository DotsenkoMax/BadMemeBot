package com.bot.tg.meme.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class Date {
    public static boolean isDistanceLessThen(LocalDateTime before, LocalDateTime after, Duration between) {
        return between.minus(Duration.between(before, after)).isPositive();
    }
}
