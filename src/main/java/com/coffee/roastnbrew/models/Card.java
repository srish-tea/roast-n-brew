package com.coffee.roastnbrew.models;

import lombok.Getter;

@Getter
public enum Card {
    HONESTY("Be honest"),
    BEST_AT_WHAT_YOU_DO("Be the best at what you do"),
    BUILD_PRODUCTS_USERS_LOVE("Build products that users love"),
    AUDACIOUS_GOALS("Have audacious goals"),
    EMPATHY("Have empathy");

    private String displayName;

    Card(String displayName) {
        this.displayName = displayName;
    }
}
