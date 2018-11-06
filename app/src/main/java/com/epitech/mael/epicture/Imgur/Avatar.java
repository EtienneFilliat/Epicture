package com.epitech.mael.epicture.Imgur;

public class Avatar {
    private Data data;

    private class Data {
        String avatar;
    }

    public String avatarUrl() {
        return data.avatar;
    }
}