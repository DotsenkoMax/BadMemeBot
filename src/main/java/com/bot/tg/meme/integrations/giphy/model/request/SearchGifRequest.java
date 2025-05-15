package com.bot.tg.meme.integrations.giphy.model.request;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SearchGifRequest {

    public final String q;
    public final Optional<Integer> limit;
    public final Optional<Integer> offset;
    public final List<String> rating;
    public final Optional<String> lang;
    public final Optional<String> randomId;
    public final Optional<String> bundle;
    public final Optional<String> countryCode;
    public final Optional<String> region;

    public static SearchGifRequest.Builder searchGifRequest() {
        return new SearchGifRequest.Builder();
    }

    private SearchGifRequest(Builder builder) {
        this.q = builder.q;
        this.limit = Optional.ofNullable(builder.limit);
        this.offset = Optional.ofNullable(builder.offset);
        this.rating = builder.rating;
        this.lang = Optional.ofNullable(builder.lang);
        this.randomId = Optional.ofNullable(builder.randomId);
        this.bundle = Optional.ofNullable(builder.bundle);
        this.countryCode = Optional.ofNullable(builder.countryCode);
        this.region = Optional.ofNullable(builder.region);
    }

    public static class Builder {
        private String q;
        private Integer limit;
        private Integer offset;
        private List<String> rating = Collections.emptyList();
        private String lang;
        private String randomId;
        private String bundle;
        private String countryCode;
        private String region;

        public Builder q(String q) {
            this.q = q;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder rating(List<String> rating) {
            this.rating = rating;
            return this;
        }

        public Builder lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder randomId(String randomId) {
            this.randomId = randomId;
            return this;
        }

        public Builder bundle(String bundle) {
            this.bundle = bundle;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public SearchGifRequest build() {
            if (q == null || q.isBlank()) {
                throw new IllegalArgumentException("q (query) is required");
            }
            return new SearchGifRequest(this);
        }
    }
}
