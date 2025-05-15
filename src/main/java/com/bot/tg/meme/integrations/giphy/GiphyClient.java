package com.bot.tg.meme.integrations.giphy;

import com.bot.tg.meme.integrations.giphy.model.request.RandomGifRequest;
import com.bot.tg.meme.integrations.giphy.model.request.SearchGifRequest;
import com.bot.tg.meme.integrations.giphy.model.response.Gif;
import com.bot.tg.meme.integrations.giphy.model.response.RandomResponse;
import com.bot.tg.meme.integrations.giphy.model.response.SearchResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class GiphyClient {

    private static final String GIPHY_URL = "https://api.giphy.com";
    private static final String GIPHY_RANDOM_SUFFIX = "/v1/gifs/random";
    private static final String GIPHY_SEARCH_SUFFIX = "/v1/gifs/search";
    final RestTemplate restTemplate;
    final String apiToken;

    public GiphyClient(RestTemplate restTemplate, String apiToken) {
        this.restTemplate = restTemplate;
        this.apiToken = apiToken;
    }

    public Gif getRandomGif(RandomGifRequest randomGifRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GIPHY_URL + GIPHY_RANDOM_SUFFIX);
        randomGifRequest.tag.map(it -> builder.queryParam("tag", it));
        randomGifRequest.countryCode.map(it -> builder.queryParam("country_code", it));
        randomGifRequest.region.map(it -> builder.queryParam("region", it));
        builder.queryParam("api_key", apiToken);

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

    public List<Gif> getGifByType(SearchGifRequest searchGifRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GIPHY_URL + GIPHY_SEARCH_SUFFIX);

        searchGifRequest.limit.map(it -> builder.queryParam("limit", it));
        searchGifRequest.countryCode.map(it -> builder.queryParam("country_code", it));
        searchGifRequest.region.map(it -> builder.queryParam("region", it));
        searchGifRequest.offset .map(it -> builder.queryParam("offset", it));
        searchGifRequest.limit.map(it -> builder.queryParam("limit", it));
        builder.queryParam("rating", searchGifRequest.rating.toArray());
        builder.queryParam("q", searchGifRequest.q);
        builder.queryParam("api_key", apiToken);

        ResponseEntity<SearchResponse> entity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                SearchResponse.class
        );

        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Giphy returned with error code %s".formatted(entity.toString()));
        }

        if (!"200".equals(entity.getBody().meta.status)) {
            throw new RuntimeException("Giphy returned Meta with error code %s".formatted(entity.getBody().meta.toString()));
        }

        return entity.getBody().gifs;
    }


}
