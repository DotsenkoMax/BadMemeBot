package com.bot.tg.meme.integrations.giphy.model.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Gif {
    public String type;
    public String id;
    public String slug;
    public String url;

    @JsonProperty("images")
    public Map<String, Image> imageMap;

    public List<Image> getImageList() {
        return new ArrayList<>(imageMap.values());
    }

    @JsonProperty("bitly_url")
    public String bitlyUrl;

    @JsonProperty("embed_url")
    public String embedUrl;

    public String username;
    public String source;
    public String rating;

    @JsonProperty("content_url")
    public String contentUrl;

    @JsonProperty("source_tld")
    public String sourceTld;

    @JsonProperty("source_post_url")
    public String sourcePostUrl;

    @JsonProperty("update_datetime")
    public String updateDatetime;

    @JsonProperty("create_datetime")
    public String createDatetime;

    @JsonProperty("import_datetime")
    public String importDatetime;

    @JsonProperty("trending_datetime")
    public String trendingDatetime;

    public String title;

    @JsonProperty("alt_text")
    public String altText;

    public Optional<String> getMp4Url() {
        return getImageList().stream()
                .filter(it -> it.mp4 != null)
                .map(it -> it.mp4).findFirst();
    }
}