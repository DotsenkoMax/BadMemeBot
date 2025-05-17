package com.bot.tg.meme.config.integrations;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GiphyConfiguration {

    @Bean
    public RestTemplate initRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public GiphyClient giphyClient(@Value("${giphy.token}") String giphyToken, RestTemplate restTemplate) {
        return new GiphyClient(restTemplate, giphyToken);
    }
}
