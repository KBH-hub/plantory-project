package com.zero.plantory.global.vo;

public enum ManagementLevel {
    VERY_EASY("408001", "매우 쉬움"),
    VERY_HARD("408002", "매우 어려움"),
    NORMAL("408003", "보통"),
    EASY("408004", "쉬움"),
    HARD("408005", "어려움");

    private final String code;
    private final String label;

    ManagementLevel(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }

    public String getLabel() { return label; }

    public static ManagementLevel fromCode(String code) {
        for (ManagementLevel lv : values()) {
            if (lv.code.equals(code)) return lv;
        }
        throw new IllegalArgumentException("Unknown manage level code: " + code);
    }

    public static ManagementLevel fromLabel(String label) {
        for (ManagementLevel lv : values()) {
            if (lv.label.equals(label)) {
                return lv;
            }
        }
        throw new IllegalArgumentException("Unknown manage level label: " + label);
    }

    /** 출력 시 한글 라벨이 바로 나오게 */
    @Override
    public String toString() {
        return label;
    }

}
