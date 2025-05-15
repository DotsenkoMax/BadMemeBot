package com.bot.tg.meme.integrations.giphy.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    public String url;
    public String width;
    public String height;
    public String size;
    public String mp4;
    public String webp;

    // Getters and setters
}