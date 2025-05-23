package com.bot.tg.meme.integrations.tenor.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Builder
public class TenorGif {
    private float created;                   // Unix timestamp
    private boolean hasaudio;               // true if audio is present
    private String id;                      // Tenor result identifier

    @JsonProperty("media_formats")
    private Map<String, TenorMedia> media; // Map of GIF_FORMAT -> MEDIA_OBJECT
    private List<String> tags;              // Tags
    private String title;                   // Post title
    private String itemurl;                 // Full URL on tenor.com
    private boolean hascaption;            // true if post contains captions
    private String url;                     // Short URL on tenor.com

    public Optional<String> getMp4Url() {
        for (var each: media.entrySet()) {
            if (each.getValue() != null && each.getValue().getUrl() != null) {
                return Optional.of(each.getValue().getUrl());
            }
        }
        return Optional.empty();
    }
}
