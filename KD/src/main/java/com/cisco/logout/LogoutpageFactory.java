package com.cisco.logout;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LogoutpageFactory implements LogoutpageConstants {
	@FindBy(xpath = loginuser_x)
    public WebElement WE_loginuser_x;

	@FindBy(xpath = logout_x)
    public WebElement WE_logout_x;

}
