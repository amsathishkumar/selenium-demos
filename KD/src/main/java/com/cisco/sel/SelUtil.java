package com.cisco.sel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class SelUtil {
	private static SelUtil selutil;
	private WebDriver driver = null;
	private boolean appiumstatus;
	private SelUtil(BROWSERTYPE btype,boolean appium) {
		appiumstatus = appium;
		if (appium) {
			// http://www.guru99.com/introduction-to-appium.html
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("BROWSER_NAME", "Android");
			dc.setCapability("VERSION", "4.3.0");
			dc.setCapability("deviceName", "YT910RVLDJ");
			dc.setCapability("platformName", "Android");
			dc.setCapability("appPackage", "com.sat.info");
			dc.setCapability("appActivity", ".MainActivity");
			try {
				driver = new RemoteWebDriver(new URL(
						"http://127.0.0.1:4723/wd/hub"), dc);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (btype == BROWSERTYPE.FF ){
		    System.out.println("Browser : Fire Fox" + System.getProperty("user.dir"));
			FirefoxBinary binary = new FirefoxBinary(new File(
					"C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));
			FirefoxProfile profile = new FirefoxProfile();
			driver = new FirefoxDriver(binary, profile);
			//System.setProperty("webdriver.gecko.driver", "C:\\Users\\smuniapp\\Downloads\\geckodriver-v0.18.0-win64\\geckodriver.exe");
    		}
			else if(btype == BROWSERTYPE.CH ){
				System.out.println("Browser: Chrome");

			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+ "\\src\\resources\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.setBinary(System.getProperty("user.dir")+ "\\src\\resources\\chrome64_56.0.2924.87\\chrome.exe");
			driver = new ChromeDriver(options);
			}
			else
			{
			    System.setProperty("webdriver.ie.driver","C:\\Users\\smuniapp\\Downloads\\IEDriverServer.exe");
	            DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
	            capabilitiesIE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	           driver = new InternetExplorerDriver(capabilitiesIE);
			}

		}
	}

	public static SelUtil getSelUtil(BROWSERTYPE btype,boolean appium)
	{
		if (selutil == null)
			selutil = new SelUtil(btype,appium);
		return selutil;
	}

	public WebDriver getWebdriver(){
		return driver;
	}

	public boolean getAppiumStatus(){
		return appiumstatus;
	}

	public void navigateURL(String url) {

		driver.get(url);
		driver.manage().window().maximize();
		driver.manage()
		.timeouts()
		.implicitlyWait(2,TimeUnit.SECONDS);
	}
	public void closeDriver() {
		driver.close();
		driver.quit();
	}
}
