package com.aat.application.core.form;


public enum EnumDateFilter {
    TD("Today"), TW ("This week"), NW ("Next Week"), TM("This month"), NM("Next Month");

    private final String name;

    EnumDateFilter(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
