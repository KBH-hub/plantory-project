package com.zero.plantory.global.vo;

public enum ManagementNeeds {
    LITTLE_CARE("409001", "약간 돌봄"),
    STRONG("409002", "잘 견딤"),
    NEED_CARE("409003", "필요함");

    private final String code;
    private final String label;

    ManagementNeeds(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /** 코드(예: "409001") → Enum */
    public static ManagementNeeds fromCode(String code) {
        for (ManagementNeeds d : values()) {
            if (d.code.equals(code)) return d;
        }
        throw new IllegalArgumentException("Unknown manage demand code: " + code);
    }

    /** 한글 라벨(예: "약간 돌봄") → Enum */
    public static ManagementNeeds fromLabel(String label) {
        for (ManagementNeeds d : values()) {
            if (d.label.equals(label)) return d;
        }
        throw new IllegalArgumentException("Unknown manage demand label: " + label);
    }

    /** 출력 시 한글 라벨이 바로 나오게 */
    @Override
    public String toString() {
        return label;
    }
}
