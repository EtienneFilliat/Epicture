package com.epitech.mael.epicture.Imgur;

import java.util.List;
import com.epitech.mael.epicture.Imgur.ImageList.Image;

public class AlbumList {

    public List<Album> data;

    public class Album {

        public String id;
        public String title;
        public String description;
        public String link;
        public String vote;
        public Integer views;
        public boolean favorite;
        public boolean is_album;
        public List<Image> images;
    }
}
