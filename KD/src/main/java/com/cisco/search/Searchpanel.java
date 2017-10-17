package com.cisco.search;

import org.openqa.selenium.support.PageFactory;

import com.cisco.sel.BROWSERTYPE;
import com.cisco.sel.SelUtil;

public class Searchpanel {
    private SearchpanelFactory searchf;
    SelUtil  selutil = SelUtil.getSelUtil(BROWSERTYPE.CH, false);
    public Searchpanel()
    {
    	searchf = PageFactory.initElements(selutil.getWebdriver(), SearchpanelFactory.class);
    }
    public void search(String searchStr){
    	searchf.WE_searchtext_id.clear();
    	searchf.WE_searchtext_id.sendKeys(searchStr);
    	searchf.WE_searchbutton_x.click();
    }

}
