package com.zero.plantory.global.dto;

public enum ManageLevel {
    VERY_EASY("매우 쉬움"),   // 매우쉬움
    EASY("쉬움"),             // 쉬움, 초보자(실내)
    NORMAL("보통"),           // 보통
    HARD("어려움"),           // 어려움, 경험자(실내)
    VERY_HARD("매우 어려움"), // 매우어려움, 전문가(실내)
    ETC("기타");

    private final String label;
    ManageLevel(String label) { this.label = label; }
    public String getLabel() { return label; }
}
