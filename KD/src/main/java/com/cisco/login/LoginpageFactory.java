package com.cisco.login;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginpageFactory implements LoginpageConstants{
    @FindBy(xpath = username_x)
    public WebElement WE_username_x;

    @FindBy(xpath = password_x)
    public WebElement WE_password_x;

    @FindBy(xpath = signin_x)
    public WebElement WE_signin_x;
}
