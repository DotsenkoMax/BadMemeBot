package com.bot.tg.meme.processors;
import com.bot.tg.meme.models.events.TgSendEvent;
import com.bot.tg.meme.publisher.EventMessagesPublisher;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CommandsMessageProcessorTest {

    private EventMessagesPublisher publisher;

    @Spy
    private CommandsMessageProcessor processor;

    @BeforeEach
    public void setUp() {
        publisher = mock(EventMessagesPublisher.class);
        processor = new CommandsMessageProcessor("TestBot", publisher);
    }

    @Test
    public void testDescriptionCommand() {
        // given
        Update update = mockUpdate("/tellme", 123L, 321);

        // when
        processor.process(update);

        // then
        ArgumentCaptor<TgSendEvent> captor = ArgumentCaptor.forClass(TgSendEvent.class);
        verify(publisher, times(1)).publishTgSendEvent(captor.capture());

        TgSendEvent.TgRawSendEvent raw = captor.getValue().tgRawSendEvent;
        assertEquals(123L, raw.getChatId());
        assertEquals(Optional.of(321), raw.getReplyToMessageId());
        assertEquals(Optional.of("Sosal?"), raw.getMessage());
    }

    @Test
    public void testDescriptionWithTagCommand() {
        // given
        Update update = mockUpdate("/tellme@TestBot", 123L, 321);

        // when
        processor.process(update);

        // then
        ArgumentCaptor<TgSendEvent> captor = ArgumentCaptor.forClass(TgSendEvent.class);
        verify(publisher, times(1)).publishTgSendEvent(captor.capture());

        TgSendEvent.TgRawSendEvent raw = captor.getValue().tgRawSendEvent;
        assertEquals(123L, raw.getChatId());
        assertEquals(Optional.of(321), raw.getReplyToMessageId());
        assertEquals(Optional.of("Sosal?"), raw.getMessage());
    }

    @Test
    public void testFallbackCommand() {
        // given
        Update update = mockUpdate("/unknown", 123L, 321);

        // when
        processor.process(update);

        // then
        ArgumentCaptor<TgSendEvent> captor = ArgumentCaptor.forClass(TgSendEvent.class);
        verify(publisher, times(1)).publishTgSendEvent(captor.capture());

        TgSendEvent.TgRawSendEvent raw = captor.getValue().tgRawSendEvent;
        assertEquals(123L, raw.getChatId());
        assertEquals(Optional.of(321), raw.getReplyToMessageId());
        assertEquals(Optional.of("Что это? unknown"), raw.getMessage());
    }

    private Update mockUpdate(String text, Long chatId, Integer messageId) {
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(chatId);

        Message message = mock(Message.class);
        when(message.text()).thenReturn(text);
        when(message.chat()).thenReturn(chat);
        when(message.messageId()).thenReturn(messageId);

        Update update = mock(Update.class);
        when(update.message()).thenReturn(message);

        return update;
    }
}
