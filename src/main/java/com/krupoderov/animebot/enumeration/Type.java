package com.krupoderov.animebot.enumeration;

public enum Type {
    NSFW("nsfw_"),
    SFW("sfw_");

    private String label;

    Type(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
