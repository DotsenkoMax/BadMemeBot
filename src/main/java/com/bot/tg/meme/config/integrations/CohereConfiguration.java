package com.bot.tg.meme.config.integrations;

import com.bot.tg.meme.integrations.NlpClient;
import com.bot.tg.meme.integrations.cohere.CohereClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CohereConfiguration {

    @Bean
    public NlpClient nlpClient(@Value("${cohere.token}") String cohereToken) {
        return new CohereClient(cohereToken);
    }
}
