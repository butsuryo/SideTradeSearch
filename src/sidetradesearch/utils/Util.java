package sidetradesearch.utils;


import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import sidetradesearch.Activator;


/**
 * 全般ユーティリティクラス
 * @author butsuryo
 *
 */
public class Util {

    public static final Color pink = new Color(getDisplay(), new RGB(240,204,204));
    public static final Color gray  = new Color(getDisplay(), new RGB(240,240,240));
    public static final Color red = new Color(getDisplay(), new RGB(255,0,0));
    public static final Color blue = new Color(getDisplay(), new RGB(0,0,255));
    /**
     * SWTラベルを作成する
     * @param composite ラベルを配置する親Compositeオブジェクト
     * @param text ラベルに表示する文字数
     * @return 作成したラベル
     */
    public static Label createLabel(Composite composite, String text){
        Label label = new Label(composite, SWT.NONE);
        label.setText(text);
        return label;
    }

    /**
     * 改行用の文字なしSWTラベルを作成する
     * @param composite ラベルを配置する親Compositeオブジェクト
     * @return 作成したラベル
     */
    public static Label createEmptyLineLabel(Composite composite){
        Label label = new Label(composite, SWT.NONE);
        return label;
    }

    /**
     * 横幅いっぱいの境界線を作成して返却する
     * @param composite
     * @return ラベルオブジェクト
     */
    public static Label createHorizotalLine(Composite composite) {
        Label line = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        line.setLayoutData(data);
        return line;
    }

    /**
     * 縦横いっぱいに幅を取ったGridDataを作成して返却する
     * @return GridData
     */
    public static GridData createFillBothGridData() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        return gridData;
    }

    /**
     * 横いっぱいに幅を取ったGridDataを作成して返却する
     * @return GridData
     */
    public static GridData createHorizontalGridData() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        return gridData;
    }

    /**
     * 「コピー」リンクを作成する。
     * リンク押下時、引数で指定したText要素に入力されている文字列をクリップボードにコピーする。
     *
     * @param composite 親Composite
     * @param textObj Text
     * @return 作成したLink
     */
    public static Link createCopyLink(Composite composite, final Text textObj) {
        Link link = new Link(composite, SWT.NONE);
        link.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
        link.setText("<a>コピー</a>");
        link.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Util.copyToClipboad(textObj.getText());
            }
        });
        return link;
    }

    /**
     * 「コピー」リンクを作成する。
     * リンク押下時、引数で指定したLabel要素に入力されている文字列をクリップボードにコピーする。
     *
     * @param composite 親Composite
     * @param labelObj Label
     * @return 作成したLink
     */
    public static Link createCopyLink(Composite composite, final Label labelObj) {
        Link link = new Link(composite, SWT.NONE);
        link.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false));
        link.setText("<a>コピー</a>");
        link.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Util.copyToClipboad(labelObj.getText());
            }
        });
        return link;
    }

    /**
     * GridLayoutをセットしたCompositeを作成して返す
     * @param parent 親Composite
     * @param columnNum カラム数(指定しない場合は0)
     * @return GridLayoutをセットしたComposite
     */
    public static Composite createGridComposite(Composite parent, int columnNum) {
        Composite composite = new Composite(parent, SWT.NONE);
        if (columnNum == 0 ){
            composite.setLayout(new GridLayout());
        } else {
            composite.setLayout(new GridLayout(columnNum, false));
        }
        return composite;
    }

    /**
     * GridLayoutをセットしたCompositeを作成して返す(等幅)
     * @param parent 親Composite
     * @param columnNum カラム数(指定しない場合は0)
     * @return GridLayoutをセットしたComposite
     */
    public static Composite createEqualWidthGridComposite(Composite parent, int columnNum) {
        Composite composite = new Composite(parent, SWT.NONE);
        if (columnNum == 0 ){
            composite.setLayout(new GridLayout());
        } else {
            composite.setLayout(new GridLayout(columnNum, true));
        }
        return composite;
    }

    /**
     * FillLayoutをセットしたGroupを作成して返す
     * @param parent 親Composite
     * @param label  グループ上部に表示する文字列
     * @return FillLayoutをセットしたGroup
     */
    public static Group createGroup(Composite parent, String label) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new FillLayout());
        group.setText(label);

        return group;
    }

    /**
     * infoダイアログ表示
     * @param title
     * @param message
     */
    public static void openInfoDialog(String title, String message){
        MessageDialog.openInformation(Util.getShell(), title, message);
    }

    /**
     * confirmダイアログ表示（OK/キャンセル）
     * @param title
     * @param message
     * @return ダイアログで選択されたボタンのbool（OK:true　キャンセル:false）
     */
    public static boolean openConfirmDialog(String title, String message){
        return MessageDialog.openConfirm(Util.getShell(), title, message);
    }

    /**
     * questionダイアログ表示（はい/いいえ）
     * @param title
     * @param message
     * @return ダイアログで選択されたボタンのbool（はい:true　いいえ:false）
     */
    public static boolean openQuestionDialog(String title, String message){
        return MessageDialog.openQuestion(Util.getShell(), title, message);
    }

    /**
     * IWorkbench取得
     * @return
     */
    public static IWorkbench getWorkbench(){
        return PlatformUI.getWorkbench();
    }

    public static IWorkbenchPage getActivePage() {
        return getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * Shell取得
     * @return
     */
    public static Shell getShell(){
        IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
        return window.getShell();
    }

    /**
     * Display取得
     * @return
     */
    public static Display getDisplay(){
        return getShell().getDisplay();
    }

    /**
     * 引数の文字列リストを改行区切りでファイル出力する
     * @param strList 出力したい文字列リスト
     * @param path 出力したいファイルパス
     */
    public static void outputFileStrList(ArrayList<String> strList, String path) {
        try{

            File file = new File(path);
            file.createNewFile();

            if (file.exists() && file.isFile() && file.canWrite()){
              PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

              for (String str : strList) {
                  pw.println(str);
                  pw.println("");
              }

              pw.close();
            }else{
              System.err.println("ファイルに書き込めません");
            }
          }catch(IOException e){
            System.out.println(e);
          }
    }

    /**
     * 指定したSJISファイルの中身を読みこむ
     * @param filePath ファイルパス
     * @return ファイルの中身
     */
    public static String inputSJisFile(String filePath) {
        String fileStr = StringUtil.EMPTY;

        try{
            File file = new File(filePath);

            if (file.exists() && file.isFile() && file.canWrite()){
                FileInputStream in = new FileInputStream(filePath);
                InputStreamReader sr = new InputStreamReader(in, "SJIS");
                BufferedReader br = new BufferedReader(sr);

                String line;
                while((line = br.readLine()) != null){
                    fileStr += line + StringUtil.LINE_CODE;
                }

                br.close();
            }else{
                System.out.println("ファイルが見つからないか開けません");
            }
        }catch(FileNotFoundException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }

        return fileStr;
    }

    /**
     * 指定されたパスのファイルを外部エディタで開く
     * @param path エディタで開くファイルのパス
     */
    public static void fileOpen(String path) {
        try {
            Desktop.getDesktop().edit(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定された文字列を、クリップボードへコピーします。
     * @param select 文字列。
     */
    public static void copyToClipboad(String select) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(select);
        clipboard.setContents(selection, selection);

//        // 「これ以降この確認メッセージを表示しない」が選択済みの場合、ダイアログを表示しない
//        if (Util.getPreference("isOpenCopyDialog").equals("true")) {
//            ClassTrialDialog dialog = new ClassTrialDialog(Util.getShell());
//            dialog.open("コピー完了", "コピーしました。", "これ以降この確認メッセージを表示しない");
//        }
    }

    /**
     * プラグインのプリファレンスストア(設定)の値を更新する
     * @param key 設定キー
     * @param value キーに設定する値
     */
    public static void setPreference(String key, String value) {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(key, value);
    }

    /**
     * プラグインのプリファレンスストア(設定)の値を取得する
     * @param key 設定キー
     * @return キーに設定されている値
     */
    public static String getPreference(String key) {
        return Activator.getDefault().getPreferenceStore().getString(key);
    }

    /**
     * 単一ファイル選択ダイアログを開き、選択されたファイルのパスを返す
     * @return ダイアログで選択されたファイルのパス
     */
    public static String openFileSelectDialog() {
        FileDialog dialog = new FileDialog(Util.getShell(), SWT.OPEN);
        dialog.setText("title");

        return dialog.open();
    }

    /**
     * 単一ファイル選択ダイアログを開き、選択されたファイルのパスを返す（選択対象の拡張子を限定する）
     * @param String[] extentions 限定する拡張子の配列
     * @return ダイアログで選択されたファイルのパス
     */
    public static String openFileSelectDialogWithExtension(String[] extensions) {
        FileDialog dialog = new FileDialog(Util.getShell(), SWT.OPEN);
        dialog.setText("title");

        dialog.setFilterExtensions(extensions);

        return dialog.open();
    }

    public static void openView(String viewId) {
        // ビューを開く
        try {
            getActivePage().showView(viewId);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public static void openViewStack(String viewId) {
        // ビューを既に開いている同名ビューと同箇所にタブで開き、今開いた方をアクティブにする
        try {
            getActivePage().showView(viewId, viewId, IWorkbenchPage.VIEW_ACTIVATE);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public static void closeView(String viewId) {
        // ビューを閉じる
        IViewPart viewPart = getActivePage().findView(viewId);
        getActivePage().hideView(viewPart);

    }

    public static void reOpenDataView() {
        Util.closeView("dataview");
        Util.openView("dataview");
    }

    /**
     * パースペクティブを開く
     * @param persepectiveId 開くパースペクティブのID
     */
    public static void openPerspective(String persepectiveId) {

        try {
            // 一度現在開いているパースペクティブを閉じてから
            closePerspective();

            // 開く
            getWorkbench().showPerspective(persepectiveId, getWorkbench().getActiveWorkbenchWindow());

        } catch (WorkbenchException e) {
            e.printStackTrace();
        }
    }

    public static void closePerspective() {
        // 現在のパースペクティブを閉じる
        if (getActivePage().getPerspective() != null) {
            getActivePage().closePerspective(getActivePage().getPerspective(), false, false);
        }
    }

    public static Point getDefaultWindowSize() {
        return new Point(400, 300);
    }

    /**
     * 現在のウィンドウのサイズを取得する
     * @return 現在のウィンドウのサイズ
     */
    public static Point getCurrentWindowSize() {
        return getWorkbench().getActiveWorkbenchWindow().getShell().getSize();
    }
}
