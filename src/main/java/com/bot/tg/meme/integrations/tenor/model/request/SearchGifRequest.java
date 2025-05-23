package com.bot.tg.meme.integrations.tenor.model.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Setter @Getter
@Builder
public class SearchGifRequest {
    private String q;

    private Integer limit;

    @Builder.Default
    private Optional<String> locale = Optional.empty();

    @Builder.Default
    private Optional<Boolean> random = Optional.empty();

    @Builder.Default
    private Optional<String> country = Optional.empty();

    @Builder.Default
    private Optional<String> clientKey = Optional.empty();

    @Builder.Default
    private Optional<String> mediaFilter = Optional.empty();

    @Builder.Default
    private Optional<String> contentfilter = Optional.empty();
}
