package com.cisco.osvers;

public class Osversion {

		public String os_verion(String versionStr){
			
		String OSName = "Unknown";
		if (versionStr.contains("Windows NT 6.2")) OSName="Windows 8";
		if (versionStr.contains("Windows NT 6.1")) OSName="Windows 7";
		if (versionStr.contains("Windows NT 6.0") ) OSName="Windows Vista";
		if (versionStr.contains("Windows NT 5.1")) OSName="Windows XP";
		if (versionStr.contains("Windows NT 5.0") ) OSName="Windows 2000";
		if (versionStr.contains("Mac")) OSName="Mac/iOS";
		if (versionStr.contains("X11")) OSName="UNIX";
		if (versionStr.contains("Linux")) OSName="Linux";
		return OSName;
		}

}
