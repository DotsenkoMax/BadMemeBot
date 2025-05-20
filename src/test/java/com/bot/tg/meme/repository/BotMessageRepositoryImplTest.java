package com.bot.tg.meme.repository;

import com.bot.tg.meme.models.BotMessage;
import com.bot.tg.meme.models.BotMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BotMessageRepositoryImplTest {

    private BotMessageRepositoryImpl repository;
    private final Long chatId = 42L;

    @BeforeEach
    void setUp() {
        repository = new BotMessageRepositoryImpl();
    }

    @Test
    void testGetLastSentMessageInChat() {
        BotMessage msg1 = BotMessage.builder()
                .chatId(chatId)
                .sentDateTime(LocalDateTime.now().minusMinutes(10))
                .messageType(BotMessageType.TEXT)
                .build();

        BotMessage msg2 = BotMessage.builder()
                .chatId(chatId)
                .sentDateTime(LocalDateTime.now())
                .messageType(BotMessageType.TEXT)
                .build();

        repository.addMessage(chatId, msg1);
        repository.addMessage(chatId, msg2);

        Optional<BotMessage> last = repository.getLastSentMessageInChat(chatId);

        assertTrue(last.isPresent());
        assertEquals(msg2.getSentDateTime(), last.get().getSentDateTime());
    }

    @Test
    void testGetLastSentMessageInChatByType() {
        BotMessage msg1 = BotMessage.builder()
                .chatId(chatId)
                .sentDateTime(LocalDateTime.now().minusMinutes(5))
                .messageType(BotMessageType.GIF)
                .build();

        BotMessage msg2 = BotMessage.builder()
                .chatId(chatId)
                .sentDateTime(LocalDateTime.now().minusMinutes(2))
                .messageType(BotMessageType.TEXT)
                .build();

        BotMessage msg3 = BotMessage.builder()
                .chatId(chatId)
                .sentDateTime(LocalDateTime.now())
                .messageType(BotMessageType.GIF)
                .build();

        repository.addMessage(chatId, msg1);
        repository.addMessage(chatId, msg2);
        repository.addMessage(chatId, msg3);

        Optional<BotMessage> lastImage = repository.getLastSentMessageInChat(chatId, BotMessageType.GIF);

        assertTrue(lastImage.isPresent());
        assertEquals(msg3.getSentDateTime(), lastImage.get().getSentDateTime());

        Optional<BotMessage> lastText = repository.getLastSentMessageInChat(chatId, BotMessageType.TEXT);
        assertTrue(lastText.isPresent());
        assertEquals(msg2.getSentDateTime(), lastText.get().getSentDateTime());
    }

    @Test
    void testEmptyChatReturnsEmpty() {
        Optional<BotMessage> result = repository.getLastSentMessageInChat(chatId);
        assertFalse(result.isPresent());

        result = repository.getLastSentMessageInChat(chatId, BotMessageType.TEXT);
        assertFalse(result.isPresent());
    }
}
