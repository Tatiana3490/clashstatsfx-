package com.svalero.clashstatsfx.model;

import com.google.gson.annotations.SerializedName;

public class Card {

    private String name;

    @SerializedName("maxLevel")
    private int maxLevel;

    @SerializedName("iconUrls")
    private IconUrls iconUrls;

    public String getName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public IconUrls getIconUrls() {
        return iconUrls;
    }

    public static class IconUrls {
        private String medium;

        public String getMedium() {
            return medium;
        }
    }
}