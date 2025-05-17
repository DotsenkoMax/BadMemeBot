package com.bot.tg.meme.integrations.cohere;

import com.bot.tg.meme.integrations.NlpClient;
import com.bot.tg.meme.jobs.HotEventGeneratorJob;
import com.cohere.api.Cohere;
import com.cohere.api.resources.v2.requests.V2ChatRequest;
import com.cohere.api.types.ChatMessageV2;
import com.cohere.api.types.ChatResponse;
import com.cohere.api.types.UserMessage;
import com.cohere.api.types.UserMessageContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CohereClient implements NlpClient {
    private static final Logger logger = LoggerFactory.getLogger(CohereClient.class);

    private final Cohere cohere;

    public CohereClient(String apiToken) {
        this.cohere = Cohere.builder().clientName("snippet").token(apiToken).build();
    }

    @Override
    public Optional<String> getResponse(String text) {

        ChatResponse response = null;
        try {
            response = cohere.v2().chat(
                V2ChatRequest.builder()
                    .model("command-a-03-2025")
                    .messages(
                            List.of(
                                    ChatMessageV2.user(
                                            UserMessage.builder()
                                                    .content(UserMessageContent.of(text))
                                                    .build()
                                    )
                            )
                    ).build()
            );
        } catch (Exception exception) {
            logger.error("Cohere exception during the call with text: {}, {}",
                    text.substring(0, Math.min(10, text.length())), exception.getMessage()
            );
            return Optional.empty();
        }

        final var maybeListOfContents = response.getMessage().getContent();
        if (maybeListOfContents.isEmpty()
                || maybeListOfContents.get().size() != 1
                || maybeListOfContents.get().get(0).getText().isEmpty()) {
            return Optional.empty();
        }
        final var content = maybeListOfContents.get().get(0).getText().get().getText();

        return Optional.of(content);
    }
}
