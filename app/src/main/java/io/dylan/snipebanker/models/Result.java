package io.dylan.snipebanker.models;

public enum Result {

    UNKNOWN("-99", "unknown"),

    WIN("3", "win"),
    DRAW("1", "draw"),
    LOSE("0", "lose"),

    HANDICAP_WIN("h3", "handicap_win"),
    HANDICAP_DRAW("h1", "handicap_draw"),
    HANDICAP_LOSE("h0", "handicap_lose")

    ;

    String code;
    String description;

    Result(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Result fromCode(String code) {
        switch (code) {
            case "3":
                return WIN;
            case "1":
                return DRAW;
            case "0":
                return LOSE;
            case "h3":
                return HANDICAP_WIN;
            case "h1":
                return HANDICAP_DRAW;
            case "h0":
                return HANDICAP_LOSE;
            default:
                return UNKNOWN;
        }
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

}