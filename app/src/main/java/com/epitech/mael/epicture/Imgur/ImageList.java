package com.epitech.mael.epicture.Imgur;

import java.util.List;

public class ImageList {

    public List<Image> data;

    public class Image {

        public String id;
        public String deletehash;
        public String title;
        public String description;
        public String views;
        public String link;
        public boolean is_album;
    }
}