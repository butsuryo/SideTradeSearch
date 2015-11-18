package sidetradesearch.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文字列ユーティリティクラス
 * @author butsuryo
 *
 */
public class StringUtil {

    public static final String SLASH = "/";
    public static final String EMPTY = "";
    public static final String LINE_CODE = System.getProperty("line.separator");
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String FULL_WIDTH_SPACE = "　";
    public static final String COMMA = ",";
    public static final String MIDDLE_DOT = "・";
    public static final String TAB = "\t";
    public static final String START_PARENTHESES = "「";
    public static final String END_PARENTHESES  = "」";
    public static final String START_BIG_PARENTHESES = "【";
    public static final String END_BIG_PARENTHESES  = "】";
    public static final String START_SMALL_PARENTHESES = "(";
    public static final String END_SMALL_PARENTHESES  = ")";
    public static final String COLON = "：";
    public static final String And = "&";
    public static final String EQUAL = "=";

    public static boolean isEmpty(String str){

        if (str == null || str.equals("")) {
            return true;
        }

        return false;
    }

    /**
     * 改行で区切ってリストを返す
     * 空白行はスキップする
     *
     * @param text 文字列
     * @return 改行区切りのリスト
     */
    public static ArrayList<String> getRowList(String text){
        String[] str = text.split(LINE_CODE);
        ArrayList<String> splitList = new ArrayList<String>();

        for (int i=0; i<str.length; i++) {
            if (!StringUtil.isEmpty(str[i])) {
                splitList.add(str[i]);
            }
        }
        return splitList;
    }


    /**
     * タブで区切ってリストを返す
     * @param text 文字列
     * @return タブ区切りのリスト
     */
    public static List<String> getTubList(String text) {
        String[] str = text.split(TAB);
        return Arrays.asList(str);
    }


    /**
     * リストを区切り文字で結合する
     * @return
     */
    public static String implode(ArrayList<String> array, String glue) {
        String str = "";
        for (String piece : array) {
            str += piece + glue;
        }

        // 最後の結合要素を削除
        return str.substring(0, str.lastIndexOf(glue));
    }


    /**
     * 文字列を「」で囲って返す
     * @param str 文字列
     * @return 「」で囲った文字列
     */
    public static String createParenthesesStr(String str) {
        return START_PARENTHESES + str + END_PARENTHESES;
    }


    /**
     * 文字列を【】で囲って返す
     * @param str 文字列
     * @return 【】で囲った文字列
     */
    public static String createBigParenthesesStr(String str) {
        return START_BIG_PARENTHESES + str + END_BIG_PARENTHESES;
    }

    /**
     * 文字列を()で囲って返す
     * @param str 文字列
     * @return ()で囲った文字列
     */
    public static String createSmallParenthesesStr(String str) {
        return START_SMALL_PARENTHESES + str + END_SMALL_PARENTHESES;
    }

    /**
     * 引数の数字を○付き数字にして返す
     * 20より上には対応してないので、その場合はString化した素の数値を返す
     *
     * @param num ○付きにしたい数値
     * @return ○付き数字
     */
    public static String getNumberWithCircle(int num) {

        switch (num) {
            case 1 : return "①";
            case 2 : return "②";
            case 3 : return "③";
            case 4 : return "④";
            case 5 : return "⑤";
            case 6 : return "⑥";
            case 7 : return "⑦";
            case 8 : return "⑧";
            case 9 : return "⑨";
            case 10 : return "⑩";
            case 11 : return "⑪";
            case 12 : return "⑫";
            case 13 : return "⑬";
            case 14 : return "⑭";
            case 15 : return "⑮";
            case 16 : return "⑯";
            case 17 : return "⑰";
            case 18 : return "⑱";
            case 19 : return "⑲";
            case 20 : return "⑳";
            default : return String.valueOf(num);
        }
    }

}
