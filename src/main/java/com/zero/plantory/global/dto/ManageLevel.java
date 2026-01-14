package com.zero.plantory.global.dto;

public enum ManageLevel {
    VERY_EASY("매우 쉬움"),
    EASY("쉬움"),
    NORMAL("보통"),
    HARD("어려움"),
    VERY_HARD("매우 어려움"),
    ETC("기타");

    private final String label;
    ManageLevel(String label) { this.label = label; }
    public String getLabel() { return label; }
}
