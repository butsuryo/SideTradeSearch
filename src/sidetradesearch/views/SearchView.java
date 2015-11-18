package sidetradesearch.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import sidetradesearch.Activator;
import sidetradesearch.models.SearchCondition;
import sidetradesearch.models.SearchTargetEnum;
import sidetradesearch.models.TradeModel;
import sidetradesearch.utils.Util;

public class SearchView extends ViewPart {
    public static final String ID = "searchview";

    private Composite parent;
    private Composite composite;

    private Button presentRadio;
    private Button requestRadio;
    private Button oneRadio;

    private Button requestRadio2;

    private Button presentRadio2;

    private Button oneRadio2;

    private Text text;

    private Button conrainRadio;

    private Composite conditions2;
    private StackLayout stack;

    private Composite innerDefault;

    private Composite innerAdd;


    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {

        this.parent = parent;
        this.composite = Util.createGridComposite(parent, 0);

        // 基盤のformを作成
        FormToolkit toolkit = new FormToolkit(composite.getDisplay());
        Form form = toolkit.createForm(composite);
        form.setLayoutData(Util.createFillBothGridData());

        // 全体のcompositeを作成
        Composite body = form.getBody();
        body.setLayout(new FillLayout());

        Composite inner = Util.createGridComposite(body, 0);

        // 検索条件
        Composite conditions = Util.createGridComposite(inner, 3);

        // 文字列入力
        text = toolkit.createText(conditions, "");
        GridData data  = new GridData(200,20);
        data.verticalAlignment = SWT.BOTTOM;
        text.setLayoutData(data);

        // 検索条件ラジオ
        Group targetGroup = Util.createGroup(conditions, "対象", 3);
        presentRadio = toolkit.createButton(targetGroup, "「出」のみ", SWT.RADIO);
        requestRadio = toolkit.createButton(targetGroup, "「求」のみ", SWT.RADIO);
        oneRadio = toolkit.createButton(targetGroup, "どちらか片方でも", SWT.RADIO);
        oneRadio.setSelection(true);

        // 除外ラジオ
        Group containGroup = Util.createGroup(conditions, "含む/除外", 2);
        conrainRadio = toolkit.createButton(containGroup, "含む", SWT.RADIO);
        Button notContainRadio = toolkit.createButton(containGroup, "含まない", SWT.RADIO);
        conrainRadio.setSelection(true);


        // 検索条件2
        conditions2 = Util.createGridComposite(inner, 3);
        stack = new StackLayout();
        conditions2.setLayout(stack);

        // 初期
        innerDefault = Util.createGridComposite(conditions2, 0);
        stack.topControl = innerDefault;


        // 条件追加リンク
        Composite links =  Util.createGridComposite(innerDefault, 0);
        Link andLink = new Link(links, SWT.NONE);
        andLink.setText("<a>+AND条件追加</a>");
        andLink.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                changeStackTop();
            }
        });

        Link orLink = new Link(links, SWT.NONE);
        orLink.setText("<a>+OR条件追加</a>");
        orLink.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("link");
            }
        });


        Group checks =  new Group(innerDefault, SWT.NONE);
        checks.setLayout(new FillLayout());
        checks.setText("表示箇所");
        Button currentViewRadio = toolkit.createButton(checks, "現在のビューに表示", SWT.RADIO);
        Button newViewRadio = toolkit.createButton(checks, "新しいビューに表示", SWT.RADIO);
        currentViewRadio.setSelection(true);

        // 検索ボタン
        Composite buttons = Util.createGridComposite(innerDefault, 2);
        Button searchBtn = toolkit.createButton(buttons, "検索実行", SWT.PUSH);
        searchBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Activator.setDefaultList();
                ArrayList<SearchCondition> conditions = new ArrayList<>();

                conditions.add(new SearchCondition(text.getText(), getSelectedTarget(), conrainRadio.getSelection()));
                search(conditions);


                // Dataviewの再読み込み
                if (currentViewRadio.getSelection()) {
                    Util.reOpenDataView();
                } else {
                    // TODO 前のビューは前のモデルを参照する必要がある。アクティベーターでの持ち方に限界がある？
                    Util.openViewStack("dataview");
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });


        // 検索解除ボタン
        Button resetBtn = toolkit.createButton(buttons, "クリア", SWT.PUSH);
        resetBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Activator.setDefaultList();

                // Dataviewの再読み込み
                Util.reOpenDataView();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });

        Composite saveButton = Util.createGridComposite(innerDefault, 0);

        // 検索保存ボタン
        Button saveBtn = toolkit.createButton(saveButton, "検索条件を保存", SWT.PUSH);
        saveBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO 未実装
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });





        // 追加されたとき用
        innerAdd = Util.createGridComposite(conditions2, 0);
        Composite condition2 = Util.createGridComposite(innerAdd, 3);

        // 文字列入力
        Text text2 = toolkit.createText(condition2, "");
        GridData data2  = new GridData(200,20);
        data2.verticalAlignment = SWT.BOTTOM;
        text2.setLayoutData(data2);

        // 検索条件ラジオ
        Group targetGroup2 = Util.createGroup(condition2, "対象", 3);
        presentRadio2 = toolkit.createButton(targetGroup2, "「出」のみ", SWT.RADIO);
        requestRadio2 = toolkit.createButton(targetGroup2, "「求」のみ", SWT.RADIO);
        oneRadio2 = toolkit.createButton(targetGroup2, "どちらか片方でも", SWT.RADIO);
        oneRadio2.setSelection(true);

        // 除外ラジオ
        Group containGroup2 = Util.createGroup(condition2, "含む/除外", 2);
        Button conrainRadio2 = toolkit.createButton(containGroup2, "含む", SWT.RADIO);
        Button notContainRadio2 = toolkit.createButton(containGroup2, "含まない", SWT.RADIO);
        conrainRadio2.setSelection(true);

        // 条件追加リンク
        Composite links2 =  Util.createGridComposite(innerAdd, 0);
        Link andLink2 = new Link(links2, SWT.NONE);
        andLink2.setText("<a>+AND条件追加</a>");
        andLink2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                changeStackTop();
            }
        });

        Link orLink2 = new Link(links2, SWT.NONE);
        orLink2.setText("<a>+OR条件追加</a>");
        orLink2.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("link");
            }
        });


        Group checks2 =  new Group(innerAdd, SWT.NONE);
        checks2.setLayout(new FillLayout());
        checks2.setText("表示箇所");
        Button currentViewRadio2 = toolkit.createButton(checks2, "現在のビューに表示", SWT.RADIO);
        Button newViewRadio2 = toolkit.createButton(checks2, "新しいビューに表示", SWT.RADIO);
        currentViewRadio2.setSelection(true);

        // 検索ボタン
        Composite buttons2 = Util.createGridComposite(innerAdd, 2);
        Button searchBtn2 = toolkit.createButton(buttons2, "検索実行", SWT.PUSH);
        searchBtn2.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Activator.setDefaultList();
                ArrayList<SearchCondition> conditions = new ArrayList<>();

                conditions.add(new SearchCondition(text.getText(), getSelectedTarget(), conrainRadio.getSelection()));
                search(conditions);


                // Dataviewの再読み込み
                if (currentViewRadio.getSelection()) {
                    Util.reOpenDataView();
                } else {
                    // TODO 前のビューは前のモデルを参照する必要がある。アクティベーターでの持ち方に限界がある？
                    Util.openViewStack("dataview");
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });


        // 検索解除ボタン
        Button resetBtn2 = toolkit.createButton(buttons2, "クリア", SWT.PUSH);
        resetBtn2.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Activator.setDefaultList();

                // Dataviewの再読み込み
                Util.reOpenDataView();

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });

        Composite saveButton2 = Util.createGridComposite(innerAdd, 0);

        // 検索保存ボタン
        Button saveBtn2 = toolkit.createButton(saveButton2, "検索条件を保存", SWT.PUSH);
        saveBtn2.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO 未実装
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // do nothing.
            }
        });
    }


    // どの対象が選択されているか
    private SearchTargetEnum getSelectedTarget() {
        if (presentRadio.getSelection()) {
            return SearchTargetEnum.PRESENT;
        } else if (requestRadio.getSelection()) {
            return SearchTargetEnum.REQUEST;
        } else {
            return SearchTargetEnum.ONE;
        }
    }

    // 検索と言う名の絞込み
    private void search(ArrayList<SearchCondition> conditions) {

        // 絞込んだリスト
        ArrayList<TradeModel> searchList = new ArrayList<>();

        for (SearchCondition condition : conditions) {
            for (TradeModel model : Activator.getList()) {

                boolean isMatchPresent = model.getPresent().indexOf(condition.getSearch()) != -1;
                boolean isMatchRequest = model.getRequest().indexOf(condition.getSearch()) != -1;

                switch(condition.getTarget()) {
                    case PRESENT:
                        if (isMatchCondition(isMatchPresent, condition.isContain())) {
                            searchList.add(model);
                        }
                        break;
                    case REQUEST:
                        if (isMatchCondition(isMatchRequest, condition.isContain())) {
                            searchList.add(model);
                        }
                        break;
                    case ONE:
                        if (condition.isContain()) {
                            if (isMatchCondition(isMatchPresent, condition.isContain())
                                    || isMatchCondition(isMatchRequest, condition.isContain())) {
                                searchList.add(model);
                            }
                        } else {
                            if (isMatchCondition(isMatchPresent, condition.isContain())
                                    && isMatchCondition(isMatchRequest, condition.isContain())) {
                                searchList.add(model);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        // 新しく出来たリストをActivatorで保持
        Activator.setList(searchList);
    }

    private boolean isMatchCondition(boolean isMatchText, boolean isContain) {
        if (isMatchText && isContain) {
            return true;
        }
        if (!isMatchText && !isContain) {
            return true;
        }
        return false;
    }


    private void updateView() {

        // 画面の更新
        this.composite.dispose();
        this.createPartControl(this.parent);
        this.parent.layout();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        //this.changeStackTop();
    }

    private void changeStackTop() {

        // 一度現在の表示を破棄し、作り直す
        this.composite.dispose();
        this.createPartControl(this.parent);
        stack.topControl = innerAdd;

        this.parent.layout();
    }


}