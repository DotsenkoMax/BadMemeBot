package com.bot.tg.meme.integrations.tenor.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TenorGifReponse {
    private String next;
    private List<TenorGif> results;
}
