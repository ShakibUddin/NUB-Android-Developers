package com.nubandroiddev.nubandroiddevelopers;

public class Profile {
    private Integer imageSource;

    public Integer getImageSource() {
        return imageSource;
    }

    public String getName() {
        return name;
    }

    private String name;

    public Profile(String name,Integer imageSource) {
        this.imageSource = imageSource;
        this.name = name;
    }
}
