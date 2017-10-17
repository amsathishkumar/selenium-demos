package com.cisco.search;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchpanelFactory implements SearchpanelConstants {

	@FindBy(id = searchtext_id)
    public WebElement WE_searchtext_id;

	@FindBy(xpath = searchbutton_x)
    public WebElement WE_searchbutton_x;
}
