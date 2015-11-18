package sidetradesearch.views;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import sidetradesearch.models.TradeModel;
import sidetradesearch.utils.StringUtil;


public class DataViewLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {


    public String getColumnText(Object obj, int columnIndex) {

        if(!(obj instanceof TradeModel)){
            return StringUtil.EMPTY;
        }

        TradeModel data = (TradeModel)obj;

        switch(columnIndex){

            // 出
            case 0:
                return data.getPresent();

            // 求
            case 1:
                return data.getRequest();

            // 備考
            case 2:
                return data.getOptional();
        }

        return StringUtil.EMPTY;
    }

    public Image getColumnImage(Object obj, int index) {
        return null;
    }

    @Override
    public Color getForeground(Object obj, int columnIndex) {
        return null;
    }

    @Override
    public Color getBackground(Object obj, int columnIndex) {

//        if(!(obj instanceof Player)){
//            return null;
//        }
//
//        Player data = (Player)obj;
//
//        if (data.isItemRedraw()) {
//            return Util.pink;
//        }

        return null;
    }


}
