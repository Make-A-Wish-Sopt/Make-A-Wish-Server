package com.sopterm.makeawish.config.cache;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {

    PRESENT_COUNT("allPresentsList", 3000, 30);

    private final String cacheName;
    private final int maxSize;
    private final int timeToLive;
}
