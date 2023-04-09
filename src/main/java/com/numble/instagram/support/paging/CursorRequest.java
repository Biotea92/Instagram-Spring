package com.numble.instagram.support.paging;

public record CursorRequest(Long key, Integer size) {

    public static final Long NONE_KEY = -1L;
    private static final Integer DEFAULT_SIZE = 10;
    private static final Integer MAX_SIZE = 100;


    public CursorRequest(Long key, Integer size) {
        this.key = key;
        this.size = size == null ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
    }

    public Boolean hasKey() {
        return key != null && !key.equals(NONE_KEY);
    }

    public CursorRequest next(Long key) {
        return new CursorRequest(key, size);
    }
}
