package sidetradesearch.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import sidetradesearch.Activator;
import sidetradesearch.models.TradeModel;
import sidetradesearch.utils.Util;

public class DataView extends ViewPart {
    public static final String ID = "dataview";

    private TableViewer viewer;
    private ScrolledComposite sc;

    private ArrayList<TradeModel> list;

    public DataView() {

        this.list = Activator.getList();
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    public void createPartControl(Composite parent) {

        Composite composite = Util.createGridComposite(parent, 0);

        sc =  new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
        sc.setLayout(new GridLayout());
        sc.setLayoutData(Util.createFillBothGridData());

        viewer = new TableViewer(sc, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

        Table table = viewer.getTable();

        // 現在のウィンドウの横幅を取得
        int tableFullWidth = (Util.getCurrentWindowSize().x  - table.getBorderWidth() * 2) - 10;

        // カラム作成
        createColumn(table, 0, "出", (int)(tableFullWidth * 0.3));
        createColumn(table, 1, "求", (int)(tableFullWidth * 0.3));
        createColumn(table, 2, "備考", (int)(tableFullWidth * 0.3));

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new DataViewLabelProvider());

        table.setSize(tableFullWidth, 280);
        sc.setContent(table);

        viewer.setInput(this.list);
    }

    /**
     * テーブルカラムを作成する
     * ヘッダカラム押下には対応しない
     *
     * @param table テーブル
     * @param columnIndex カラム番号
     * @param text 表示文言
     * @param width 横幅
     */
    private void createColumn(Table table, int columnIndex, String text, int width){
        TableColumn column = new TableColumn(table, 0, columnIndex);
        column.setText(text);
        column.setData("index", columnIndex);
        column.setWidth(width);
    }


    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}