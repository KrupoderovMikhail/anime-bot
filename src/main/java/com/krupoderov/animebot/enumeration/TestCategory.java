package com.krupoderov.animebot.enumeration;

public enum TestCategory {
    GIRL("girl/test"),
    TRAP("trap/test"),
    MONSTER("monster/test"),
    MILF("milf/test"),
    LOLI("loli/test");

    private String label;

    TestCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
