package com.krupoderov.animebot.enumeration;

public enum Category {
    ANAL("anal"),
    DP("dp"),
    SOLO_FEMALE("sf"),
    VAGINAL("vaginal"),
    TRAP("trap");

    private String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
