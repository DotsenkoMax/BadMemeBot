package com.bot.tg.meme.integrations;

import java.util.Optional;

public interface NlpClient {
    Optional<String> getResponse(String text);
}
