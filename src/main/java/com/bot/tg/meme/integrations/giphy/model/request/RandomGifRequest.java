package com.bot.tg.meme.integrations.giphy.model.request;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RandomGifRequest {

    public final List<String> rating;
    public final Optional<String> tag;
    public final Optional<String> randomId;
    public final Optional<String> countryCode;
    public final Optional<String> region;

    public static RandomGifRequest.Builder randomGifRequest() {
        return new RandomGifRequest.Builder();
    }


    private RandomGifRequest(Builder builder) {
        this.tag = Optional.ofNullable(builder.tag);
        this.rating = builder.rating;
        this.randomId = Optional.ofNullable(builder.randomId);
        this.countryCode = Optional.ofNullable(builder.countryCode);
        this.region = Optional.ofNullable(builder.region);
    }

    public static class Builder {
        private List<String> rating = Collections.emptyList();
        private String tag;
        private String randomId;
        private String countryCode;
        private String region;

        public Builder rating(List<String> rating) {
            this.rating = rating;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder randomId(String randomId) {
            this.randomId = randomId;
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

        public RandomGifRequest build() {
            return new RandomGifRequest(this);
        }
    }
}
