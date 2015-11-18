package sidetradesearch.models;


public enum SearchTargetEnum {

    PRESENT("出"),
    REQUEST("求"),
    ONE("どちらか");

    public final String searchTarget;

    private SearchTargetEnum(String difficulty) {
        this.searchTarget = difficulty;
    }

    public static SearchTargetEnum getDifficultyEnumByName(String searchTarget) {

        for (SearchTargetEnum targetEnum : SearchTargetEnum.values()) {
            if (targetEnum.searchTarget.equals(searchTarget)) {
                return targetEnum;
            }
        }
        return null;
    }

}
