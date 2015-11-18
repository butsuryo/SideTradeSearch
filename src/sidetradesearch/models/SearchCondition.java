package sidetradesearch.models;

public class SearchCondition {

	// 検索対象の文字列
    private String search;
    // 検索対象の区分
    private SearchTargetEnum target;
    // 含む検索かどうか
    private boolean isContain;

    public SearchCondition(String search, SearchTargetEnum target, boolean isContain) {
        this.search = search;
        this.target = target;
        this.isContain = isContain;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public SearchTargetEnum getTarget() {
        return target;
    }
    public void setTarget(SearchTargetEnum target) {
        this.target = target;
    }
    public boolean isContain() {
		return isContain;
	}
    public void setContain(boolean isContain) {
		this.isContain = isContain;
	}


}
