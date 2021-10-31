package com.krupoderov.animebot.enumeration;

public enum TrapCategory {
    SEX("trap/sex"),
    BDSM("trap/bdsm"),
    BLOWJOB("trap/blowjob"),
    GANGBANG("trap/gangbang"),
    SCHOOL("trap/school");

    private String label;

    TrapCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
