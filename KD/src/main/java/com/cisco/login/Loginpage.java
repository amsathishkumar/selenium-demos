package com.cisco.login;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.cisco.osvers.Osversion;
import com.cisco.sel.BROWSERTYPE;
import com.cisco.sel.SelUtil;

public class Loginpage {
    private LoginpageFactory loginpf;
    SelUtil  selutil = SelUtil.getSelUtil(BROWSERTYPE.CH, false);
    public Loginpage(){
    		loginpf = PageFactory.initElements(selutil.getWebdriver(), LoginpageFactory.class);

    }
    public void Login(String userID, String pwd){
		openBrowser();
		getOSBrowserDetails();
		enterLogin(userID,pwd);

    }

    private void openBrowser()
    {
    	if (selutil.getAppiumStatus()) {
			System.out.println("appium");
			// C:\\android-sdk\\tools\\uiautomatorviewer.bat'
			selutil.getWebdriver().findElement(By.xpath("//android.widget.EditText")).sendKeys(
					"123");
			selutil.getWebdriver().findElement(By.id("com.sat.info:id/name")).sendKeys(
					"sathish");
		} else {
			selutil.navigateURL("http://localhost/");
		}
    }
    	private  void enterLogin(String usr, String pwd)  {

    		try{
    		System.out.println(selutil.getWebdriver().getTitle());
    		loginpf.WE_username_x.clear();
    		loginpf.WE_username_x.sendKeys(usr);
    		loginpf.WE_password_x.sendKeys(pwd);
    		Thread.sleep(1000);
    		loginpf.WE_signin_x.click();
    		}catch (Exception e){
    	      selutil.closeDriver();
    		}

    	}
        private void getOSBrowserDetails() {
    		String browserversion = (String) ((JavascriptExecutor) selutil.getWebdriver())
    				.executeScript("return navigator.userAgent;");
    		System.out.println("Results:" + selutil.getWebdriver().getTitle());
    		Osversion os = new Osversion();
    		System.out.println("OS:" + os.os_verion(browserversion));
    		System.out.println("Browser : " + browserversion.split(" ")[0]);
    	}
}
