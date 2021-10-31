package com.krupoderov.animebot.enumeration;

public enum LoliCategory {
    BLOWJOB("loli/blowjob"),
    SEX("loli/sex"),
    SHOLICON("loli/sholicon"),
    SOLO("loli/solo"),
    YURI("loli/yuri");

    private String label;

    LoliCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
