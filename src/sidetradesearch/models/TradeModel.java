package sidetradesearch.models;

import java.text.Normalizer;

public class TradeModel {

    // 求
    private String request;

    // 出
    private String present;

    // 備考
    private String optional;

    public TradeModel(String request, String present, String optional) {
        this.request = request;
        this.present = present;
        this.optional = optional;
    }

    public String getRequest() {
        // 半角⇒全角
        return Normalizer.normalize(request, Normalizer.Form.NFKC);
    }
    public void setRequest(String request) {
        this.request = request;
    }
    public String getPresent() {
         // 半角⇒全角
        return Normalizer.normalize(present, Normalizer.Form.NFKC);
    }
    public void setPresent(String present) {
        this.present = present;
    }
    public String getOptional() {
        // 半角⇒全角
        return Normalizer.normalize(optional, Normalizer.Form.NFKC);
    }
    public void setOptional(String optional) {
        this.optional = optional;
    }


}
