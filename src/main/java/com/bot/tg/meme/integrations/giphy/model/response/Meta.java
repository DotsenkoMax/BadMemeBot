package com.bot.tg.meme.integrations.giphy.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {

    public String msg;

    @JsonProperty("status")
    public String status;
    @JsonProperty("response_id")
    public String responseId;
}
