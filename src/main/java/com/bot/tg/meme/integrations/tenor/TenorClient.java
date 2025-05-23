package com.bot.tg.meme.integrations.tenor;

import com.bot.tg.meme.integrations.tenor.model.request.SearchGifRequest;
import com.bot.tg.meme.integrations.tenor.model.response.TenorGif;
import com.bot.tg.meme.integrations.tenor.model.response.TenorGifReponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class TenorClient {

    private static final String TENOR_URL = "https://tenor.googleapis.com";
    private static final String TENOR_TRANSLATE_SUFFIX = "/v2/search";
    private final RestTemplate restTemplate;
    private final String apiToken;


    public TenorClient(RestTemplate restTemplate, String apiToken) {
        this.restTemplate = restTemplate;
        this.apiToken = apiToken;
    }

    public List<TenorGif> getSearchGif(SearchGifRequest searchGifRequest) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TENOR_URL + TENOR_TRANSLATE_SUFFIX);
        searchGifRequest.getClientKey().map(it -> builder.queryParam("client_key", it));
        searchGifRequest.getMediaFilter().map(it -> builder.queryParam("media_filter", it));
        searchGifRequest.getCountry().map(it -> builder.queryParam("country", it));
        searchGifRequest.getLocale().map(it -> builder.queryParam("locale", it));
        searchGifRequest.getRandom().map(it -> builder.queryParam("random", it));
        searchGifRequest.getContentfilter().map(it -> builder.queryParam("contentfilter", it));
        builder.queryParam("key", apiToken);
        builder.queryParam("q", searchGifRequest.getQ());
        builder.queryParam("limit", searchGifRequest.getLimit());

        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", List.of("application/json"));
        headers.put("Accept", List.of("*/*"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TenorGifReponse> entity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                TenorGifReponse.class
        );

        if (!entity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Tenor returned with error code %s".formatted(entity.toString()));
        }

        if (entity.getBody() == null || entity.getBody().getResults().isEmpty()) {
            throw new RuntimeException("Tenor returned Meta with error code %s".formatted(searchGifRequest.getQ()));
        }

        return entity.getBody().getResults();
    }
}
