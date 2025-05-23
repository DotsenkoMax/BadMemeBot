package com.bot.tg.meme.config.integrations;

import com.bot.tg.meme.integrations.giphy.GiphyClient;
import com.bot.tg.meme.integrations.tenor.TenorClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class GifConfiguration {

    @Bean
    public RestTemplate initRestTemplate() {
        final var rest = new RestTemplate();

        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        rest.setUriTemplateHandler(defaultUriBuilderFactory);
//        rest.setInterceptors(List.of(new LoggingRequestInterceptor()));
        return rest;
    }

    @Bean
    public GiphyClient giphyClient(@Value("${giphy.token}") String giphyToken, RestTemplate restTemplate) {
        return new GiphyClient(restTemplate, giphyToken);
    }

    @Bean
    public TenorClient tenorClient(@Value("${tenor.token}") String tenorToken, RestTemplate restTemplate) {
        return new TenorClient(restTemplate, tenorToken);
    }
}
