package com.bot.tg.meme.models;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeSet;

@Getter
public class ChatSession {

    public Long chatId;

    public LinkedList<TgMessage> messages = new LinkedList<>();

    public LocalDateTime minDateTimeInSession;
    public LocalDateTime maxDateTimeInSession;
    private final Duration sessionMaxDelay;


    public ChatSession(Long chatId, TgMessage initialMessage, Integer maxDistanceInSeconds) {
        this.sessionMaxDelay = Duration.ofSeconds(maxDistanceInSeconds);
        this.chatId = chatId;
        this.messages.add(initialMessage);
        this.minDateTimeInSession = initialMessage.sentDateTime;
        this.maxDateTimeInSession = initialMessage.sentDateTime;
    }

    public synchronized boolean addToSession(TgMessage newMessage) {
        if (!Objects.equals(newMessage.chatId, chatId)) {
            throw new IllegalStateException("Adding chat message in wrong chat session %s: %s".formatted(newMessage.chatId, chatId));
        }
        if (!newMessage.getSentDateTime().isBefore(this.maxDateTimeInSession) &&
                !newMessage.getSentDateTime().isAfter(this.maxDateTimeInSession.plus(sessionMaxDelay))
        ) {
            messages.add(newMessage);
            this.maxDateTimeInSession = newMessage.getSentDateTime();
            return true;
        }
        return false;
    }
    }
