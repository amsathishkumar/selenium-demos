package com.cisco.logout;

import org.openqa.selenium.support.PageFactory;

import com.cisco.sel.BROWSERTYPE;
import com.cisco.sel.SelUtil;

public class Logoutpage {
    private LogoutpageFactory logutpf;
    SelUtil  selutil = SelUtil.getSelUtil(BROWSERTYPE.CH, false);
    public Logoutpage()
    {
    	logutpf = PageFactory.initElements(selutil.getWebdriver(), LogoutpageFactory.class);
    }
    public void logout()
    {
    	logutpf.WE_loginuser_x.click();
    	logutpf.WE_logout_x.click();
    }
    public void closeBrowser(){
    	selutil.closeDriver();
    }



}
