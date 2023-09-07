package com.enzenith.engine;

public enum MatchResult {
    HIGH("精准匹配"),
//    MEDIUM,
    LOW("疑似匹配"),
    NONE("暂不匹配");

    private String info;
    MatchResult(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
