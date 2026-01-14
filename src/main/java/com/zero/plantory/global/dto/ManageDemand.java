package com.zero.plantory.global.dto;

public enum ManageDemand {
    STRONG("잘 견딤"),
    LITTLE_CARE("약간 돌봄"),
    NEED_CARE("필요함"),
    SPECIAL_CARE("특별 관리 필요"),
    ETC("기타");

    private final String label;
    ManageDemand(String label) { this.label = label; }
    public String getLabel() { return label; }
}
