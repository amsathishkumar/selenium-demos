package com.cashkaro.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import junit.framework.Assert;
import net.sourceforge.htmlunit.corejs.javascript.commonjs.module.provider.ParsedContentType;

import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.asprise.ocr.Ocr;
import com.cashkaro.join.JoinMail;
import com.cashkaro.osvers.Osversion;
import com.cashkaro.signin.SignIn;

import org.sikuli.basics.Settings;
import org.sikuli.script.Region;
import org.sikuli.script.TextRecognizer;
import org.apache.commons.codec.binary.Base64;
public class Login {
	static boolean appium = true;
	static WebDriver driver = initialDriver();
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Hello Welcome to Demo!");
        if (appium){
        System.out.println("appium");
        //	C:\\android-sdk\\tools\\uiautomatorviewer.bat
         driver.findElement(By.id("com.sat.info:id/ids")).sendKeys("123");
        }
        else{
		navigate("http://cashkaro.iamsavings.co.uk");
		getOSBrowserDetails(driver);
		
         loginmail("Auto5");		
         sign_inmail("Auto5");
		login_fb("amsathishkumar@gmail.com","XXXXXX","SathishkumarMuniappan");
		
        }
	
		closeDriver();

	}

	private static void login_fb(String usr, String pwd,String fullname) {
		driver.findElement(By.xpath(JoinMail.join_link)).click();
		driver.findElement(By.id("close_and_go_fb")).click();
		Set windowHandles = driver.getWindowHandles();
		Iterator it = windowHandles.iterator();


		String parentBrowser= (String) it.next();
		String childBrowser = (String) it.next();
		driver.switchTo().window(childBrowser); 
		
		
		driver.findElement(By.id("email")).sendKeys(usr);
		driver.findElement(By.id("pass")).sendKeys(pwd);		
		driver.findElement(By.xpath("//input[@value='Log In']")).click();
		
		driver.findElement(By.xpath("//*[contains(text(),'Okay')]")).click();
		
		driver.findElement(By.xpath("//*[contains(@class,'_fb')]")).click();
		
		
		System.out.println("Expected:Hello Auto," + "Actual:"+ driver.findElement(By.tagName("p")).getText());
	    Assert.assertEquals("Hello "+fullname+",", driver.findElement(By.tagName("p")).getText());
	    
	}

	private static void sign_inmail(String suser) {
		driver.findElement(By.xpath("//a[text()='SIGN IN']")).click();
		
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	 
		
		driver.switchTo().frame(driver.findElements(By.tagName("iframe")).get(1));
		
		
		driver.findElement(By.id("uname")).sendKeys(suser+"@cashkaro.com");
		driver.findElement(By.xpath("html/body/section/div[2]/form/input[2]")).sendKeys("Autommmmmm");
		driver.findElement(By.id("sign_in")).click();
		//driver.switchTo().defaultContent();
		System.out.println("Expected:Hello Auto," + "Actual:"+ driver.findElement(By.tagName("p")).getText());
	    Assert.assertEquals("Hello Auto,", driver.findElement(By.tagName("p")).getText());
	}

	private static void loginmail(String str) {
		
		driver.findElement(By.xpath(JoinMail.join_link)).click();
		driver.findElement(By.id(JoinMail.join_name_id)).sendKeys(str);
		driver.findElement(By.id(JoinMail.join_mail_id)).sendKeys(
				str+"@cashkaro.com");
		driver.findElement(By.id(JoinMail.join_conmail_id)).sendKeys(
				str+"@cashkaro.com");
		int c = 1;
		do {
			System.out.println("inside while");
			driver.findElement(By.id(JoinMail.join_pwdtxt_id)).sendKeys(
					"Autommmmmm");
			
			if (driver.findElements(By.xpath("//*[contains(@value,'Invalid sum of two numbers.')]")).size()>0)
				driver.findElement(By.xpath("//*[contains(@value,'Invalid sum of two numbers.')]")).click();
//			if (c>18)
//				c=1;
			 int upperBound = 10;
			 int lowerBound = 0;
			 Random random = new Random();
			 int randomNumber1 = random.nextInt(upperBound - lowerBound) + lowerBound;
			 int randomNumber2 = random.nextInt(upperBound - lowerBound) + lowerBound;
			 c=randomNumber1+randomNumber2;
			 System.out.println(c);
			 
					
			driver.findElement(By.id("to_be_check"))
					.sendKeys(String.valueOf(c));
			driver.findElement(By.id("join_free_submit")).click();
			for(int i=0;i<100000;i++){
				System.out.println("sss");
			}
			c++;

		} while (driver
				.findElements(
						By.xpath("//*[contains(@value,'Invalid sum of two numbers.')]"))
				.size() != 0);
		driver.findElement(By.xpath("//a[text()='LOGOUT']")).click();
		// WebElement secure=
		// driver.findElement(By.id(JoinMail.join_securityimage_id));

		// int width = secure.getSize().getWidth();
		// int height = secure.getSize().getHeight();
		//
		// Settings.OcrTextSearch=true;
		// Settings.OcrTextRead=true;
		//
		// Region rr = new Region(secure.getLocation().getX(),
		// secure.getLocation().getY(), width, height);
		//
		// System.out.println("SAT"+rr.text());

		//
		// File screen =
		// ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		// int width = secure.getSize().getWidth();
		// int height = secure.getSize().getHeight();
		// BufferedImage img;
		// try {
		// img = ImageIO.read(screen);
		// BufferedImage dest = img.getSubimage(secure.getLocation().getX(),
		// secure.getLocation().getY(), width, height);
		// ImageIO.write(dest, "png", screen);
		// File file = new File("./src/main/java/secure.png");
		// FileUtils.copyFile(screen, file);
		//
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		//
		//
		//
		// try {
		// Thread.sleep(200);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// imagepr();

		// Ocr.setUp(); // one time setup
		// Ocr ocr = new Ocr(); // create a new OCR engine
		// ocr.startEngine("eng", Ocr.SPEED_FAST); // English
		// String s = ocr.recognize(new File[] { new
		// File("./src/main/java/secure.png") },
		// Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
		// System.out.println("OCR Result: " + s);
		// // ocr more images here ...
		// ocr.stopEngine();
		//
	}

	public static void texttoimg() {
		String sampleText = "SAMPLE TEXT";

		// Image file name
		String fileName = "Image";

		// create a File Object
		File newFile = new File("./" + fileName + ".jpeg");

		// create the font you wish to use
		Font font = new Font("Tahoma", Font.PLAIN, 11);

		// create the FontRenderContext object which helps us to measure the
		// text
		FontRenderContext frc = new FontRenderContext(null, true, true);

		// get the height and width of the text
		Rectangle2D bounds = font.getStringBounds(sampleText, frc);
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		// create a BufferedImage object
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);

		// calling createGraphics() to get the Graphics2D
		Graphics2D g = image.createGraphics();

		// set color and other parameters
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.BLACK);
		g.setFont(font);

		g.drawString(sampleText, (float) bounds.getX(), (float) -bounds.getY());

		// releasing resources
		g.dispose();

		// creating the file

		// ImageIO.write(image, "jpeg", fileName);
	}

	public static void imagepr() {
		File file = new File("./src/main/java/secure1.jpeg");

		try {
			/*
			 * Reading a Image file from file system
			 */
			FileInputStream imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			imageInFile.read(imageData);

			/*
			 * Converting Image byte array into Base64 String
			 */
			String imageDataString = encodeImage(imageData);

			System.out.println(imageDataString);

			/*
			 * Converting a Base64 String into Image byte array
			 */
			byte[] imageByteArray = decodeImage(imageDataString);

			/*
			 * Write a image byte array into file system
			 */
			FileOutputStream imageOutFile = new FileOutputStream(
					"./src/main/java/secure1after.jpeg");
			imageOutFile.write(imageByteArray);

			imageInFile.close();
			imageOutFile.close();

			System.out.println("Image Successfully Manipulated!");
		} catch (FileNotFoundException e) {
			System.out.println("Image not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the Image " + ioe);
		}

	}

	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
	}

	public static String encodeImage(byte[] imageByteArray) {
		return Base64.encodeBase64URLSafeString(imageByteArray);
	}

	private static void closeDriver() {
		//driver.close();
		driver.quit();
	}

	private static void navigate(String url) {
		
		driver.get(url);
		driver.manage().window().maximize();
	}

	private static WebDriver initialDriver() {
		WebDriver driver = null;
	
		
		if (appium) {
			//http://www.guru99.com/introduction-to-appium.html
			DesiredCapabilities dc = new DesiredCapabilities();
			dc.setCapability("BROWSER_NAME", "Android");
			dc.setCapability("VERSION", "4.4.2");
			dc.setCapability("deviceName", "33003a90a6e5226d");
			dc.setCapability("platformName","Android");
			dc.setCapability("appPackage","com.sat.info");
			dc.setCapability("appActivity",".MainActivity");
			try {
				driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"),dc);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
		FirefoxBinary binary = new FirefoxBinary(new File(
				"C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe"));
		FirefoxProfile profile = new FirefoxProfile();

	   driver = new FirefoxDriver(binary, profile);
		
		}
		
		return driver;
	}

	private static void getOSBrowserDetails(WebDriver driver1) {
		String browserversion = (String) ((JavascriptExecutor) driver1)
				.executeScript("return navigator.userAgent;");
		System.out.println("Results:" + driver1.getTitle());
		Osversion os = new Osversion();
		System.out.println("OS:" + os.os_verion(browserversion));
		System.out.println("Browser : " + browserversion.split(" ")[0]);
	}

}
