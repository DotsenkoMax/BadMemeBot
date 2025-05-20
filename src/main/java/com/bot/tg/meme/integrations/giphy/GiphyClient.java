package com.bot.tg.meme.integrations.giphy;

import com.bot.tg.meme.integrations.giphy.model.request.RandomGifRequest;
import com.bot.tg.meme.integrations.giphy.model.request.SearchGifRequest;
import com.bot.tg.meme.integrations.giphy.model.request.TranslateGifRequest;
import com.bot.tg.meme.integrations.giphy.model.response.Gif;
import com.bot.tg.meme.integrations.giphy.model.response.RandomResponse;
import com.bot.tg.meme.integrations.giphy.model.response.SearchResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class GiphyClient {

    private static final String GIPHY_URL = "https://api.giphy.com";
    private static final String GIPHY_TRANSLATE_SUFFIX = "/v1/gifs/translate";
    final RestTemplate restTemplate;
    final String apiToken;

    public GiphyClient(RestTemplate restTemplate, String apiToken) {
        this.restTemplate = restTemplate;
        this.apiToken = apiToken;
    }

//    @RateLimiter(name = "gifApi", fallbackMethod = "rateLimitFallback")
    public Gif getTranslateGif(TranslateGifRequest translateGifRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GIPHY_URL + GIPHY_TRANSLATE_SUFFIX);
        translateGifRequest.countryCode.map(it -> builder.queryParam("country_code", it));
        translateGifRequest.region.map(it -> builder.queryParam("region", it));
        builder.queryParam("api_key", apiToken);
        builder.queryParam("s", translateGifRequest.s);
        builder.queryParam("rating", translateGifRequest.rating.toArray());

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<RandomResponse> entity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                RandomResponse.class
        );

        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Giphy returned with error code %s".formatted(entity.toString()));
        }

        if (!"200".equals(entity.getBody().meta.status)) {
            throw new RuntimeException("Giphy returned Meta with error code %s".formatted(entity.getBody().meta.toString()));
        }

        return entity.getBody().data;
    }

    public String rateLimitFallback(Throwable t) {
        return "Rate limit exceeded. Please try again later.";
    }

}
