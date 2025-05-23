package com.bot.tg.meme.integrations.tenor.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TenorMedia {
    private String preview; // URL to preview image
    private String url;     // URL to the media source
    private List<Integer> dims; // [width, height]
    private int size;       // Size in bytes
}
