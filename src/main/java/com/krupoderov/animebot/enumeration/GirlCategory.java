package com.krupoderov.animebot.enumeration;

public enum GirlCategory {
    SEX("girl/sex"),
    YURI("girl/yuri"),
    BLOWJOB("girl/blowjob"),
    BDSM("girl/bdsm"),
    SCHOOL("girl/school");

    private String label;

    GirlCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
