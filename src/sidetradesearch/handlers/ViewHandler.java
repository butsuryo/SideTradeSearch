package sidetradesearch.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sidetradesearch.utils.Util;



public class ViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {


        Util.openView("dataview");
        Util.openPerspective("SideTradeSearch.perspective");
        return null;
    }

}
