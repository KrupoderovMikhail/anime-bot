package com.krupoderov.animebot.enumeration;

public enum Command {

    MENU("menu");

    private final String label;

    Command(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
