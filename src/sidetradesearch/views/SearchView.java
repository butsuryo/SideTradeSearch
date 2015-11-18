package sidetradesearch.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
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
        Text text = toolkit.createText(conditions, "");
        GridData data  = new GridData(200,20);
        data.verticalAlignment = SWT.BOTTOM;
        text.setLayoutData(data);

        // 検索条件ラジオ
        Group targetGroup = Util.createGroup(conditions, "対象");
        Button presentRadio = toolkit.createButton(targetGroup, "「出」のみ", SWT.RADIO);
        Button requestRadio = toolkit.createButton(targetGroup, "「求」のみ", SWT.RADIO);
        Button noneRadio = toolkit.createButton(targetGroup, "どちらも", SWT.RADIO);
        noneRadio.setSelection(true);

        // 除外ラジオ
        Group containGroup = Util.createGroup(conditions, "含む/除外");
        Button conrainRadio = toolkit.createButton(containGroup, "を含む", SWT.RADIO);
        Button notContainRadio = toolkit.createButton(containGroup, "を含まない", SWT.RADIO);
        conrainRadio.setSelection(true);

        // 条件追加リンク
        Composite links =  Util.createGridComposite(inner, 0);
        Link andLink = new Link(links, SWT.NONE);
        andLink.setText("<a>+AND条件追加</a>");
        andLink.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("link");
            }
        });

        Link orLink = new Link(links, SWT.NONE);
        orLink.setText("<a>+OR条件追加</a>");
        orLink.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                System.out.println("link");
            }
        });


        Group checks =  new Group(inner, SWT.NONE);
        checks.setLayout(new FillLayout());
        checks.setText("表示箇所");
        Button currentViewRadio = toolkit.createButton(checks, "現在のビューに表示", SWT.RADIO);
        Button newViewRadio = toolkit.createButton(checks, "新しいビューに表示", SWT.RADIO);
        currentViewRadio.setSelection(true);

        // 検索ボタン
        Composite buttons = Util.createGridComposite(inner, 2);
        Button searchBtn = toolkit.createButton(buttons, "検索実行", SWT.PUSH);
        searchBtn.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                Activator.setDefaultList();
                ArrayList<SearchCondition> conditions = new ArrayList<>();

                SearchTargetEnum target;
                if (presentRadio.getSelection()) {
                    target = SearchTargetEnum.PRESENT;
                } else if (requestRadio.getSelection()) {
                    target = SearchTargetEnum.REQUEST;
                } else {
                    target = SearchTargetEnum.NONE;
                }

                boolean isOpenCurrentView = currentViewRadio.getSelection();
                conditions.add(new SearchCondition(text.getText(), target, conrainRadio.getSelection()));
                search(conditions);


                // Dataviewの再読み込み
                if (isOpenCurrentView) {
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

        Composite saveButton = Util.createGridComposite(inner, 0);

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
                    case NONE:
                        if (isMatchCondition(isMatchPresent, condition.isContain())
                                && isMatchCondition(isMatchRequest, condition.isContain())) {
                            searchList.add(model);
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


}