package com.zero.plantory.global.dto;

public enum ManageDemand {
    STRONG("잘 견딤"),         // 낮음(잘 견딤), 잘 견딤(dry)
    LITTLE_CARE("약간 돌봄"),  // 보통(약간 잘견딤), 약간 돌봄(dry)
    NEED_CARE("필요함"),       // 필요함
    SPECIAL_CARE("특별 관리 필요"), // 특별관리요구(실내)
    ETC("기타");               // 기타(실내)

    private final String label;
    ManageDemand(String label) { this.label = label; }
    public String getLabel() { return label; }
}
