package com.cisco.execute;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.cisco.login.Loginpage;
import com.cisco.logout.Logoutpage;
import com.cisco.osvers.Osversion;
import com.cisco.search.Searchpanel;
import com.cisco.sel.BROWSERTYPE;
import com.cisco.sel.SelUtil;
import com.cisco.sshtunnel.sshtunnel;
import com.jcraft.jsch.JSchException;

public class ExecuteTest {

	public static void main(String[] args) throws JSchException, IOException {
		ExecuteTest exectestcase = new ExecuteTest();
		exectestcase.othertenant();
	}

	private void KDtenant() throws JSchException, IOException{
		sshtunnel ssh = new sshtunnel();
		ssh.connect("smuniapp","smuniapp@123", "10.15.48.55");
		//sudo bash -c "source /tmp/jer_ironman.sh; nova list| grep -i ph |grep -o '[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}' | tail -1"
		String ip = ssh.exec("sudo bash -c \"source /tmp/jer_doritos.sh; nova list| grep -i ph | grep -o '[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' | tail -1\" ");
	    ssh.portforward(ip, 80);
		String dmpip = ssh.exec("sudo bash -c \"source /tmp/jer_doritos.sh; nova list| grep -i dmp | grep -o '[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' | tail -1\" ");
	    ssh.portforward(dmpip, 8097);

		ExecuteTest exectestcase = new ExecuteTest();
		exectestcase.testcase1();


		ssh.close();
	}

private void othertenant() throws JSchException, IOException{
	sshtunnel ssh = new sshtunnel();
	ssh.connect("vcidev","password4Lusers!", "10.57.26.195");
	//sudo bash -c "source /tmp/jer_ironman.sh; nova list| grep -i ph |grep -o '[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}\.[0-9]\{1,3\}' | tail -1"
	String ip = ssh.exec("sudo bash -c \"source /tmp/jer_doritos.sh; nova list| grep -i ph | grep -o '[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' | tail -1\" ");
    ssh.portforward(ip, 80);
	String dmpip = ssh.exec("sudo bash -c \"source /tmp/jer_doritos.sh; nova list| grep -i dmp | grep -o '[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}\\.[0-9]\\{1,3\\}' | tail -1\" ");
    ssh.portforward(dmpip, 8097);

	ExecuteTest exectestcase = new ExecuteTest();
	exectestcase.testcase1();


	ssh.close();
}
private void testcase1()  {
    Loginpage loginpage = new Loginpage();
    Logoutpage logoutpage = new Logoutpage();
    Searchpanel searchpanel = new Searchpanel();


    loginpage.Login("test@cisco.com", "test");
    searchpanel.search("200000000");
    logoutpage.logout();
    logoutpage.closeBrowser();


}






}
