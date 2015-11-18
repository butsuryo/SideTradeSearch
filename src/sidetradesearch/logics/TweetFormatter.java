package sidetradesearch.logics;

import java.util.ArrayList;
import java.util.regex.Pattern;

import sidetradesearch.models.TradeModel;


public class TweetFormatter {

    // ツイートを整形して分析し、モデルのリストで返す
    public static ArrayList<TradeModel> format(ArrayList<String> tweetList) {

        ArrayList<TradeModel> retList = new ArrayList<>();

        for (String row : tweetList) {

            // 「求」「出」が両方揃ってないツイートはスルー
            if (row.indexOf("求") == -1 || row.indexOf("出") == -1) {
                continue;
            }

            // カード名を【】区切りにしてないやつもスルー
            if (row.indexOf("【") == -1 || row.indexOf("】") == -1) {
                continue;
            }

            // 「求」「出」それぞれの出現位置
            int requestStartIndex = row.indexOf("求");
            int presentStartIndex = row.indexOf("出");

            // 「求」「出」のうち「求」が先に書かれているかどうか
            boolean isRequestFirst = requestStartIndex < presentStartIndex;
            int firstStartIndex = (isRequestFirst) ? requestStartIndex : presentStartIndex;
            int secondStartIndex = (!isRequestFirst) ? requestStartIndex : presentStartIndex;


            // 先に書かれている方
            // 「求」から「出」の前まで、もしくは「出」から「求」の前までを切り出し
            String first = row.substring(firstStartIndex, secondStartIndex);

            // カード名の始まりを表す「【」より前を除去
            first = removeHead(first);

            // 先頭・末尾の改行を削除し、行間の2行以上の改行は1行に
            first = formatLineSeparator(first);


            // 後に書かれている方
            // ツイートの中で最後に「】」が出る位置
            int lastSeparatorIndex = row.lastIndexOf("】");

            // ↑の次に改行が出る位置
            int secondEndIndex = row.indexOf("\r\n", lastSeparatorIndex);

            // ↑↑から↑までの間を切り出し
            String second = row.substring(secondStartIndex, secondEndIndex);

            // カード名の始まりを表す「【」より前を除去
            second = removeHead(second);

            // 先頭・末尾の改行を削除し、行間の2行以上の改行は1行に
            second = formatLineSeparator(second);


            // 備考
            String optional = row.substring(secondEndIndex);
            optional = formatLineSeparator(optional);


            // モデルを作成してリストに追加
            if (isRequestFirst) {
                retList.add(new TradeModel(first, second, optional));
            } else {
                retList.add(new TradeModel(second, first, optional));
            }
        }
        return retList;
    }

    // 【 より前の文字列を除去
    private static String removeHead(String str) {

        int separatorIndex = str.indexOf("【");

        // もし文字列がやたら多いようならそこを残す努力をする(変な文字列になる可能性はある)
        if (separatorIndex > 5) {
            return str.substring(2);
        }

        return str.substring(separatorIndex);

    }

    // 改行関連のフォーマット
    private static String formatLineSeparator(String str) {

        // 二つ以上の改行を一つに
        str = str.replaceAll("\r\n\r\n", "");

        // 先頭の改行を削除
        str = removeFirstLineSeparator(str);

        // お尻の改行を削除
        str = removeLastLineSeparator(str);

        return str;
    }

    // 先頭の改行を削除
    private static String removeFirstLineSeparator(String str) {

        if (Pattern.compile("^\r\n").matcher(str).find()) {
            str = str.replaceAll("^\r\n", "");
            str = removeFirstLineSeparator(str);
        }
        return str;
    }

    // お尻の改行を削除
    private static String removeLastLineSeparator(String str) {

        if (Pattern.compile("\r\n$").matcher(str).find()) {
            str = str.replaceAll("\r\n$", "");
            str = removeLastLineSeparator(str);
        }
        return str;
    }

    // テストデータ
    public static ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("@side_trade \r\n求)【パピっとマーチ】水嶋咲\r\n出)下記いずれか\r\n【鼓舞の櫓太鼓】鷹城恭二\r\n【ドラゴンバンチ】木村龍\r\n【闇魔皇子】神楽麗+(MM)\r\n【音楽教員】都築圭+(MM)\r\n1:1の募集です。\r\n条件合う方はリプライくださいませ\r\n\r\n");
        list.add("@side_trade\r\n出)\r\n【共に進む道】東雲 荘一郎\r\n\r\n求)\r\n【ｱｲﾄﾞﾙ名探偵】姫野 かのん+とゼリー50\r\n(チェンジ前2枚でも可能です)\r\n\r\nピンポイントで難しいと思いますが条件の合う方がいらっしゃいましたらご連絡下さい。\r\nよろしくお願い致します。\r\n\r\n");
        list.add("@side_trade 求）【爆炎の益荒男】紅井 朱雀+もしくはチェンジ前2枚\r\n出）【ﾗｰﾒﾝ陶芸家】円城寺 道流 &【ﾃﾝｼｮﾝMAX!】橘 志狼 &ゼリー50\r\n1:2（&ゼリー50）もしくは2:2（&ゼリー50）取得予定でも構いません。よろしくおねがいします\r\n");
        list.add("@side_trade \r\n出)【マーチングバンド】神谷幸広\r\n求)【ﾄﾘｯｸｵｱﾃｨｰﾁｬｰ】舞田類\r\n1:1で難しいと思いますが、条件の合う方リプライorあいさつ(143630)までよろしくお願いします\r\n\r\n");
        list.add("@side_trade \r\n\r\n出）下記カード1枚＋ゼリー100\r\n【ｱｲﾄﾞﾙ名探偵】姫野かのん\r\n【ﾗｰﾒﾝ陶芸家】円城寺道流\r\n【ﾊﾟﾋﾟｯとﾏｰﾁ】水嶋咲（取得予定）\r\n\r\n求）【Full Throttle】若里春名\r\n\r\nお気軽にお声掛けください。\r\nゼリー追加個数は御相談に応じます。\r\n\r\n");
        list.add("@side_trade　求）カッコ内数アイテム\r\n【壮大華麗】天ヶ瀬 冬馬（120）\r\n【音楽教員】都築 圭+【陽射しの下】神楽 麗（300）\r\n【闇魔皇子】神楽 麗（320）\r\n【英姿颯爽】大河 ﾀｹﾙ+（300）MM済\r\n\r\n複数の場合ゼリー数調整など可能です、よろしくおねがいします。\r\n\r\n");
        list.add("@side_trade\r\n\r\n求) \r\n【パピッとマーチ 】水嶋咲\r\n\r\n出) \r\n【伝説の船長 】天ヶ瀬冬馬\r\n【ドラゴンバンチ 】木村龍\r\n\r\n1:1でのトレード希望です。条件の合う方がいらっしゃいましたらよろしくお願いします。\r\n\r\n");
        list.add("@side_trade\r\nトレード募集失礼します\r\n求)\r\n【The Sunshine!】天ヶ瀬 冬馬\r\n出)\r\n【腹ﾍﾟｺｳｻｷﾞ】蒼井 悠介\r\n【ﾄﾞﾗｺﾞﾝﾊﾞﾝﾁ】木村 龍\r\n【ﾗｰﾒﾝ陶芸家】円成寺 道流\r\n\r\n求:出=1:1+ｱｲﾃﾑ60\r\n\r\nよろしくお願いします！\r\n\r\n");
        list.add("@side_trade\r\n求）【正義が恋人】握野英雄+（MM）\r\n出）【ｽﾊﾟｰｸﾘﾝｸﾞｻﾝﾀ】天ヶ瀬冬馬+（MM済）＆ｾﾞﾘｰ150\r\nいらっしゃいましたらお気軽にご連絡お願い致します。\r\n\r\n");
        list.add("@side_trade\r\n出）【共に進む道】東雲荘一郎\r\n求）\r\n必須：【ﾏﾙﾁｷｰﾎﾞｰﾃﾞｨｽﾄ】桜庭 薫\r\n＋下記よりいずれか1～2枚\r\n【ﾊﾟﾋﾟｯとﾏｰﾁ】水嶋咲\r\n【ﾄﾞﾗｺﾞﾝﾊﾞﾝﾁ】木村 龍\r\n\r\n");
        list.add("@side_trade \r\n出)【ﾗｰﾒﾝ陶芸家】円城寺 道流+ゼリー50\r\n求)【The Sunshine!】天ヶ瀬 冬馬\r\n\r\nピンポイントで難しいと思いますが、条件の合う方がいらっしゃいましたらリプライ頂けると幸いです。\r\n宜しくお願い致します。\r\n\r\n");
        list.add("@side_trade\r\n出）【夏のまなざし】伊集院北斗\r\n　　【三蔵法師】清澄九郎\r\n\r\n求）【英姿颯爽】大河タケルMM+\r\n\r\n2:1でトレードしてくださる方を探しています。ゼリー100個までならお付けできます。条件の合う方お願いします。\r\n\r\n");
        list.add("@side_trade\r\n求）\r\n[迷子の花婿]神谷幸広\r\n\r\n出）\r\n[純真ｼｪﾛｶﾙﾃ]ﾋﾟｴｰﾙ\r\n[ｵﾌｼｮｯﾄ]神谷幸広\r\n[The Sunshine!]天ヶ瀬冬馬\r\n\r\n基本的にピントレを希望しておりますが、～150程度ゼリー調整が可能です。\r\n");

        return list;
    }
}
