package com.eclection.appid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Details {
	//static WebDriver driver;
	
	static HtmlUnitDriver driver;
	static int sethead;
	static PrintWriter writer;

	public Details() throws FileNotFoundException, UnsupportedEncodingException {
		driver = initialDriver();
		;
		sethead = 0;
		writer = new PrintWriter("appids.csv", "UTF-8");
	}

	public static void main(String[] args) {

		try {
			Details dd = new Details();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Started............................");
		ArrayList<String> appidlist = readAppId();

		for (String appid : appidlist) {
			// String appid = "EI16153113";
			navigate("http://104.211.229.179/AppTracking/TrackingStatus.aspx?Appid="
					+ appid);

			String xpath = ".//*[@id='GridView1']/tbody/tr[td[text()='%s']]/td[2]";

			String head = "";
			String data = "";

			ArrayList<String> headal = new ArrayList<String>();
			headal.add("FM_NAME_EN");
			headal.add("PART_NO");
			headal.add("SLNOINPART");
			headal.add("IDCARD_NO");
			headal.add("APPLICATIONID");
			headal.add("Progress");

			System.out.println("Start Reading........." + appid);
			for (String headdata : headal) {

				if (headdata.equals("Progress")) {

					WebElement we = driver.findElement(By
							.xpath(".//*[@class='progress-bar']"));
					String tmpdata = we.getAttribute("style");
				    System.out.println("Status:"+tmpdata);
					if (tmpdata.contains("width:16%"))
						tmpdata = "Data Entry Stage Over";
					else if (tmpdata.contains("width:66%"))
						tmpdata = "Booth Level Officer's verification status";
					else if (tmpdata.contains("width:83%"))
						tmpdata = "Electoral Registration officers's approval";
					else if (tmpdata.contains("width:100%"))
						tmpdata = "Card printing status";
					else
						tmpdata = "----Not Defined------";

					data = data + tmpdata + ",";
				} else {
					WebElement we = driver.findElement(By.xpath(String.format(
							xpath, headdata)));
					data = data + we.getText() + ",";
				}
				head = head + headdata + ",";

			}
			cfile(head, data, appid);
			System.out.println("         " + head);
			System.out.println("         " + data);
			System.out.println("Completed Reading........." + appid);

		}
		closeDriver();
		writer.close();
		System.out.println("Ended............................");

	}

	private static ArrayList<String> readAppId() {
		System.out
				.println("Started Reading ............................appids.txt");
		ArrayList<String> appids = new ArrayList<String>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream("./appids.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				appids.add(strLine.trim());
				System.out.println(strLine);
			}

			// Close the input stream
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("Completed Reading ............................appids.txt");
		return appids;
	}

	private static void closeDriver() {
		driver.quit();
		System.out.println("Ended............................");
	}

	@SuppressWarnings("null")
	public static void cfile(String vhead, String vdata, String vappid) {

		if (sethead == 0) {
			writer.println(vhead);
			sethead = 1;
		}
		writer.println(vdata);

	}

	private static void navigate(String url) {
		driver.get(url);

	}

	private static HtmlUnitDriver initialDriver() {

//		FirefoxBinary binary = new FirefoxBinary(new File(
//				"C:\\Program Files\\ff\\firefox.exe"));
//		FirefoxProfile profile = new FirefoxProfile();
//
//		WebDriver driver = new FirefoxDriver(binary, profile);

		// WebDriver driver = new FirefoxDriver();
		driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		return driver;
	}
}
