package com.bot.tg.meme.integrations.giphy.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResponse {

    @JsonProperty("data")
    public List<Gif> gifs;
    public Meta meta;
}
