package TIMS.TIMS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

public class TIMS {	private static String url = "";
	private static String xml = "";
	private static String trID = "";
	private static LinkedHashMap credentials = new LinkedHashMap();
	private static String xmlFileName = "c:\\sattims.xml"; //Don't changes
	private static String fname="c:\\excetoTIMS.txt"; //log file
	private static PrintWriter outlog ; 
	
	private static String fFileName="C:\\Sathish.html";

	
	
	public static void main(String args[]) throws HttpException, IOException{
		updatexlstoTIMSresults();
	}
	public static boolean initializeTIMSParameters(String userID,			String projectID, String configID, String token,String sw,String platform) {		try {
			boolean status = true;
			credentials.put("userID", userID);
			credentials.put("token", token);
			credentials.put("projectID", projectID);
			credentials.put("configID", configID);
			credentials.put("sw", sw);
			credentials.put("platform", platform);

			if (userID == null || userID == "") {
				status = false;				System.out.println("UserID shouldn't be null or blank.");				return status;			}
			if (token == null || token == "") {				status = false;				System.out						.println("Automation Token shouldn't be null or blank.");				return status;			}
			if (projectID == null || projectID == "") {
				status = false;
				System.out.println("ProjectID shouldn't be null or blank.");
				return status;
			}
			if (configID == null || configID == "") {
				status = false;
				System.out.println("ConfigID shouldn't be null or blank.");
				return status;
			}
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	public static void generateTimsXMLFile() {
		try {
			String fileName = xmlFileName;
			File outFile = new File(fileName);
			FileWriter out = new FileWriter(outFile);
			out.write(xml);
			out.close();
			xml = "";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Properties loadProperties(String FileName) {
		try {
			Properties property = new Properties();
			FileInputStream in = new FileInputStream(FileName);
			property.load(in);
			in.close();
			return property;
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	public static boolean buildsearchxml(LinkedHashMap cred, String testCaseID) {
		boolean status = true;		        String configID=cred.get("configID").toString();
		try {
			if (configID == null || configID == "") {
				status = false;
				System.out.println("configID shouldn't be null or blank.");
			}		
			if (testCaseID == null || testCaseID == "") {
				status = false;
				System.out.println("Test Case ID shouldn't be null or blank");
			}
			xml="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
			xml=xml+"<Tims xsi:schemaLocation=\"http://tims.cisco.com/namespace http://tims.cisco.com/xsd/Tims.xsd\"\n"; 
			xml=xml+"xmlns=\"http://tims.cisco.com/namespace\"\n";
			xml=xml+"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";
			xml=xml+"xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n";
			xml=xml+"<ResultList>\n";
			xml=xml+"<ResultRange>last</ResultRange>\n";
			xml=xml+"<CaseID>"+testCaseID+"</CaseID>\n";
		    xml=xml+"<ConfigID>"+configID+"</ConfigID>\n";
		    xml=xml+"</ResultList>\n";	
		    xml=xml+"</Tims>";
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
			return status;
		}
	}
	public static boolean buildupdatexml(LinkedHashMap cred,String result) {

		boolean status = true;	
		

        String configID=cred.get("configID").toString();
        String usrID=cred.get("userID").toString();
        String tokenv=cred.get("token").toString();
        String sw=cred.get("sw").toString();
        String platform=cred.get("platform").toString();
        String brwtype=cred.get("brwtype").toString();
        String brwversion =cred.get("brwversion").toString();
        System.out.println("browser details:"+brwtype + brwversion);
		try {

			if (configID == null || configID == "") {
				status = false;
				System.out.println("configID shouldn't be null or blank.");
			}		
			if (trID == null || trID == "") {
				status = false;
				System.out.println("Test Result ID shouldn't be null or blank");
			}
			if (result == null || result == "") {
				status = false;
				System.out.println("Result shouldn't be null or blank");
			}
			if (!(result.equals("passed") | result.equals("failed")

					| result.equals("blocked") | result.equals("dropped")

					| result.equals("passx") | result.equals("pending"))) {

				System.out.println("Invalid Status value");
				return false;
			}

			xml="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
			xml=xml+"<Tims xsi:schemaLocation=\"http://tims.cisco.com/namespace http://tims.cisco.com/xsd/Tims.xsd\"\n";
			xml=xml+"xmlns=\"http://tims.cisco.com/namespace\"\n";
			xml=xml+"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";
			xml=xml+"xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n";
			xml=xml+"<Credential user=\"" + usrID +"\" token=\""+ tokenv+ "\"/>\n";
			xml=xml+"<ID>"+trID+"</ID>\n";
			
			xml=xml+"<ListFieldValue editoperator=\"set\">\n";
				xml=xml+"<FieldName>Status</FieldName>\n";
				xml=xml+"<Value>"+result+"</Value>\n";
			xml=xml+"</ListFieldValue>\n";			
			
			xml=xml+"<ListFieldValue editoperator=\"set\">\n";
				xml=xml+"<FieldName>Tested by</FieldName>\n";
				xml=xml+"<Value>"+usrID+"</Value>\n";		
			xml=xml+"</ListFieldValue>\n";
			
			/*xml=xml+"<ListFieldValue editoperator=\"set\">\n";
				xml=xml+"<FieldName>Software Version</FieldName>\n";
				xml=xml+"<Value>"+sw+"</Value>\n";	
			xml=xml+"</ListFieldValue>\n";*/
				
			xml=xml+"<ListFieldValue editoperator=\"set\">\n";
				xml=xml+"<FieldName>Platform</FieldName>\n";
				xml=xml+"<Value>"+platform+"</Value>\n";				
			xml=xml+"</ListFieldValue>\n";
		
			xml=xml+"<TextFieldValue editoperator=\"set\">\n";
			xml=xml+"<FieldName>Browser Type</FieldName>\n";
			xml=xml+"<Value>"+brwtype+"</Value>\n";				
		    xml=xml+"</TextFieldValue>\n";
		    
			xml=xml+"<TextFieldValue editoperator=\"set\">\n";
			xml=xml+"<FieldName>Browser Version</FieldName>\n";
			xml=xml+"<Value>"+brwversion+"</Value>\n";				
		    xml=xml+"</TextFieldValue>\n";
		    
     		xml=xml+"</Tims>\n";
			return status;

		} catch (Exception e) {

			e.printStackTrace();

			status = false;

			return status;

		}

	}	public static boolean buildcreatexml(LinkedHashMap cred,String testCaseID,String result,String title) {

		boolean status = true;
		String userID = cred.get("userID").toString();
		String automationToken = cred.get("token").toString();
		String projectID = cred.get("projectID").toString();
	     String configID=cred.get("configID").toString();
		
		try {
			if (userID == null || userID == "") {
				status = false;
				System.out.println("userID shouldn't be null or blank.");
			}
			if (automationToken == null || automationToken == "") {
				status = false;
				System.out.println("Automation Token shouldn't be null or blank.");
			}
			if (testCaseID == null || testCaseID == "") {
				status = false;
				System.out.println("Test Case ID shouldn't be null or blank");
			}
			if (configID == null || configID == "") {
				status = false;
				System.out.println("configID shouldn't be null or blank.");
			}

			xml = xml + "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
			xml = xml + "<Tims xsi:schemaLocation=\"http://tims.cisco.com/namespace http://tims.cisco.com/xsd/Tims.xsd\"\n";
			xml = xml + "xmlns=\"http://tims.cisco.com/namespace\"\n";
     		xml = xml + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";
			xml = xml + "xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n";
			xml = xml + "<Credential user=\"" + userID + "\" token=\"" + automationToken + "\"/>\n";
			xml = xml + "<Result xlink:href=\"http://tims.cisco.com/xml/"+ testCaseID + "/entity.svc\">\n";
		
			xml = xml + "<Title><![CDATA["+title+"]]></Title>\n";
			xml = xml + "<ListFieldValue multi-value=\"false\">\n";
			xml = xml + "<FieldName><![CDATA[Tested by]]>\n";
			xml = xml + "</FieldName>\n";
			xml = xml + "<Value><![CDATA[" + userID + "]]>\n";			
			xml = xml + "</Value>\n";
			xml = xml + "<Value><![CDATA[shisuren]]>\n";			
			xml = xml + "</Value>\n";			
			xml = xml + "</ListFieldValue>\n";
			
			
			xml = xml + "<Owner>\n";
				xml = xml + "<UserID>\n" + userID + "\n</UserID>\n";
				xml = xml + "<Email>\n" + userID + "@cisco.com" + "\n</Email>\n";
			xml	= xml + "</Owner>\n";
			xml = xml + "<Status>" + result + "</Status>\n";	
			xml = xml + "<ConfigID xlink:href=\"http://tims.cisco.com/xml/" + configID + "/entity.svc\">" + configID + "</ConfigID>\n";
			xml = xml + "<CaseID xlink:href=\"http://tims.cisco.com/xml/" + testCaseID + "/entity.svc\">" + testCaseID + "</CaseID>\n";
			xml = xml + "</Result>\n";
			xml = xml + "</Tims>";

			return status;

		} catch (Exception e) {

			e.printStackTrace();

			status = false;

			return status;

		}

	}
	public static void updatetimsresult(String tcid, String status){
		boolean result;
		//Properties property = loadProperties("C:\\Automation\\SR6.X_EC\\Resource\\TIMS\\Tims.properties");		
		Properties property = loadProperties("Tims.properties");		result =  initializeTIMSParameters(
				  property.getProperty("userID").trim(), 
				  property.getProperty("projectID").trim(), 
				  property.getProperty("configID").trim(),                  property.getProperty("token").trim(),
                  property.getProperty("sw").trim(),
                  property.getProperty("platform").trim()
                  );
		System.out.println(property.getProperty("browsertype").trim()+property.getProperty("browserversion").trim());
		try {			
			if(!postTIMSsearch(tcid)){
				System.out.println("Test ResultID:"+ trID+"\n Result:" +result+"\n");
			}	
			else{
				System.out.println("Test ResultID:"+ trID+"\n Result:" +result+"\n");
				System.out.println("update result"+postTIMSupdate(status));		
				//outlog.print(trID);
				
			}
		} 
		catch (Exception e) 
		{			e.printStackTrace();		}
			}
	public static void createtimsresult(String tcid, String status,String title){
		boolean result;
		//Properties property = loadProperties("C:\\Automation\\SR6.X_EC\\Resource\\TIMS\\Tims.properties");		
		Properties property = loadProperties("Tims.properties");
		result =  initializeTIMSParameters(
				  property.getProperty("userID").trim(), 
				  property.getProperty("projectID").trim(), 
				  property.getProperty("configID").trim(),
                  property.getProperty("token").trim(),
                  property.getProperty("sw").trim(),
		          property.getProperty("platform").trim()
		          );
		
		
		try {			
			if(!postTIMScreate(tcid,status,title)){
				System.out.println("TResult:" +result+"\n");
			}	
			else{
				System.out.println("Result:" +result+"\n");								
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	public static boolean postTIMSsearch(String testCaseID) throws Exception {
         try {
        	 	boolean status = true;
        	 	String tc=null;
        	    String sw=	credentials.get("sw").toString();
        	 	if (credentials.size() == 0) {
        	 		status = false;
        	 		System.out.println("Sorry!! , Please initialize TIMS parameters before posting the result.");
        	 		return status;
        	 	}
			String projectID = credentials.get("projectID").toString();
				if (projectID == null || projectID == "") {
					status = false;
					System.out.println("projectID shouldn't be null or blank");
					return status;
				}

				if (testCaseID == null || testCaseID == "") {
					status = false;
					System.out.println("Test Case ID shouldn't be null or blank");
					return status;
				}

			url ="http://tims.cisco.com/xml/"+projectID+"/result-list.svc";
			if (!buildsearchxml(credentials, testCaseID)) {
				return false;
			}
			generateTimsXMLFile();
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
			File f = new File(xmlFileName);
			InputStreamRequestEntity inputEntity = new InputStreamRequestEntity(new FileInputStream(f));
			postMethod.setRequestEntity(inputEntity);
			postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
			int statusCode1 = client.executeMethod(postMethod);

			if (statusCode1 == HttpStatus.SC_OK) {
				String a=postMethod.getResponseBodyAsString();
				System.out.println("Upload complete, response="+a);
				String b[]=a.split("\n");
				System.out.println("Upload complete, response="+a);
				tc=b[8].trim();
				
				tc=tc.substring("<LogicalID>".length(), tc.length()-"</LogicalID>".length() );
				System.out.println("test Result id" +tc);
				
				//System.out.println("software version" +b[93]);
				int found=0;
				for (int sat=0 ;sat<=b.length-1;sat++)
				//for (int sat=0 ;sat<=100;sat++)
				{
					String sw1="<Value><![CDATA["+sw+"]]></Value>";
					if ((b[sat].trim()).equalsIgnoreCase(sw1) )
					//if (b[sat].contains(sw))
					{
						found=1;
						System.out.println("software version" +b[sat]);
					}		
				}
				if (found==1)
				{	
				trID=tc;
				outlog.print(trID);
				}
				else
				{
				    outlog.println(sw + "are not mtached");
					return false;
					
				}

			} else {
				System.out.println("Upload failed, response="+ HttpStatus.getStatusText(statusCode1));
				return false;
			}

			System.out.println("statusLine>>>" + postMethod.getStatusLine());
			postMethod.releaseConnection();
			return true;

		} catch (Exception e) {

			e.printStackTrace();
           // outlog.println(e);
			return false;
		}

	}	public static boolean postTIMSupdate(String result) throws Exception {
        try {
       	 	boolean status = true;       	 	
       	 	if (credentials.size() == 0) {
       	 		status = false;
       	 		System.out.println("Sorry!! , Please initialize TIMS parameters before posting the result.");
       	 		return status;
       	 	}

       	 	if (result == null || result == "") {
       	 		status = false;
       	 		System.out.println("Result shouldn't be null or blank.");
       	 		return status;
       	 	}

       	 String projectID = credentials.get("projectID").toString();
				if (projectID == null || projectID == "") {
					status = false;
					System.out.println("projectID shouldn't be null or blank");
					return status;
				}
							
			url ="http://tims.cisco.com/xml/"+projectID+"/entity-list/edit.svc";
			if (!buildupdatexml(credentials, result)) {
				return false;
			}
			generateTimsXMLFile();
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
			File f = new File(xmlFileName);
			InputStreamRequestEntity inputEntity = new InputStreamRequestEntity(new FileInputStream(f));
			postMethod.setRequestEntity(inputEntity);
			postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
			int statusCode1 = client.executeMethod(postMethod);

			if (statusCode1 == HttpStatus.SC_OK) {
				String a=postMethod.getResponseBodyAsString();
				System.out.println("Upload complete, response="+a);
				if(a.contains("Error"))
				{
					System.out.println("Staus: Fail: "+ a);
					outlog.println(", Staus: Fail: "+ a);
				}
				else
				{
					System.out.println("Staus: Pass");
					outlog.println(", Status: Pass ");
				}
				} else {
				System.out.println("Upload failed, response="+ HttpStatus.getStatusText(statusCode1));
				return false;
			}

			System.out.println("statusLine>>>" + postMethod.getStatusLine());
			postMethod.releaseConnection();
			return true;

		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}

	}
	public static boolean postTIMScreate(String testCaseID,String result,String title) throws Exception {
        try {
       	 	boolean status = true;       	 	
       	 	if (credentials.size() == 0) {
       	 		status = false;
       	 		System.out.println("Sorry!! , Please initialize TIMS parameters before posting the result.");
       	 		return status;
       	 	}

       	 	if (result == null || result == "") {
       	 		status = false;
       	 		System.out.println("Result shouldn't be null or blank.");
       	 		return status;
       	 	}

       	 String projectID = credentials.get("projectID").toString();
				if (projectID == null || projectID == "") {
					status = false;
					System.out.println("projectID shouldn't be null or blank");
					return status;
				}
							
			
			url = "http://tims.cisco.com/xml/" + projectID + "/entity.svc";
			
			if (!buildcreatexml(credentials,testCaseID, result, title)) {
				return false;
			}
			generateTimsXMLFile();
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			client.getHttpConnectionManager().getParams().setConnectionTimeout(8000);
			File f = new File(xmlFileName);
			InputStreamRequestEntity inputEntity = new InputStreamRequestEntity(new FileInputStream(f));
			postMethod.setRequestEntity(inputEntity);
			postMethod.setRequestHeader("Content-type","text/xml; charset=ISO-8859-1");
			int statusCode1 = client.executeMethod(postMethod);

			if (statusCode1 == HttpStatus.SC_OK) {
				String a=postMethod.getResponseBodyAsString();
				System.out.println("Upload complete, response="+a);
				} else {
				System.out.println("Upload failed, response="+ HttpStatus.getStatusText(statusCode1));
				return false;
			}

			System.out.println("statusLine>>>" + postMethod.getStatusLine());
			postMethod.releaseConnection();
			return true;

		} catch (Exception e) {

			e.printStackTrace();

			return false;
		}

	}
	
	public static void updatexlstoTIMSresults() throws IOException{
	    ArrayList<String> re = new ArrayList<String>(); 
	    re = excel.readexcel();
		new FileOutputStream(fname).close();
	    outlog = new PrintWriter(new BufferedWriter(new FileWriter(fname, true))); 		
		System.out.println("Tims Update started: "+ (re.size()-1));	
		outlog.println("Tims Update started");
		outlog.println("Total TIMS test case for update"+(re.size()-1));
		outlog.println(" S.no, Desc, TCID, TIMS report id, Update status");
		
		
		//updatetimsresult("Ttv3742934c", "passed");	    
	  for (int i=1;i<re.size();i++)
	    {
	    	String res =re.get(i);
	    	String[] tc=res.split("~");
	    	if (tc[0].startsWith("Trd"))
	    	{
	    		
	    		outlog.println(i+", "+tc[1]+", " +tc[0]+", "+tc[2]+", ");
	    		updatetimsresult(tc[0], tc[2]);	         	
	    	}
	    }
	    System.out.println("Tims Update Ended");  
	    outlog.println("Tims Update Ended");
	    outlog.close();
	}
	public static void updateHTMLtoTIMSresults() throws IOException{
	    ArrayList<String> re = new ArrayList<String>(); 
	    re = readhtmlfilePass();
		new FileOutputStream(fname).close();
	    outlog = new PrintWriter(new BufferedWriter(new FileWriter(fname, true))); 		
		System.out.println("Tims Update started: "+re.size());	
		outlog.println("Tims Update started");
		outlog.println("Total TIMS test case for update"+re.size());
		outlog.println(" S.no, Desc, TCID, TIMS report id, Update status");
		
		
		//updatetimsresult("Ttv3742934c", "passed");	    
	  for (int i=0;i<re.size();i++)
	    {
	    	String res =re.get(i);
	    	String[] tc=res.split("_");
	    	if (tc[0].startsWith("Ttv"))
	    	{
	    		
	    		outlog.print(i+", "+tc[1]+", " +tc[0]+", ");
	    		updatetimsresult(tc[0], "passed");	         	
	    	}
	    }
	    System.out.println("Tims Update Ended");  
	    outlog.println("Tims Update Ended");
	    outlog.close();
	}
	public static ArrayList<String> readhtmlfilePass() throws FileNotFoundException {
		ArrayList<String> re = new ArrayList<String>(); 
	   
	    String fEncoding="UTF-8";
	    String tt = "";
	    int cc=0;
	  //  String NL = System.getProperty("line.separator");
	    Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
	    try {
	      while (scanner.hasNextLine()){
	    	  
	    	  tt=scanner.nextLine();
	    	  tt=tt.trim();
	    	
	    	  if (tt.contains("PASSED TEST CASES"))
	    	  {
	    		  cc=1;
	    	  }
	    	  if(cc==1 && tt.contains("Ttv")  && !(tt.contains("Precheck")) && !(tt.contains("Precondition")))
	    	  {
	    		  int charCount = 0;
	    		  int pos=0;
	    		  if (!tt.startsWith("Ttv"))
	    		  {
	    			//  System.out.println(tt);
	    	        for(int i =0 ; i<tt.length(); i++)
	    	        {
	    	            if(tt.charAt(i) == '_')
	    	            {
	    	                charCount++;
	    	                if (charCount==2)
	    	                {	   
	    	                  pos=i;
	    	                  break;
	    	                } 
	    	            }
	    	        }
	    		  }

	   
	    	    String tcid = null;
	    	    if (charCount==2 && (!tt.startsWith("Ttv"))){
	      	    	int k=nthOccurrence(tt,'_',0);
	      	    	System.out.println("position :"+k);
	    		    tcid=tt.substring(k+1, tt.length()-5);
	    		    if (!tcid.startsWith("Ttv")){
	    		    	tcid=tt.substring(16, tt.length()-5);
	    		    }
	    		}  	    
	    	    else if (charCount==2){
	    		tcid=tt.substring(pos-11, tt.length()-5);
	    		}
	       		else
	    		{
	       			
	    		  //tcid=tt.substring(pos+16, tt.length()-5);	
	       			tcid=tt.substring(0, tt.length()-5);
	    		}
	    		System.out.println("LINE read: "+tt);
	    		System.out.println("sathish id: "+tcid);
	    		re.add(tcid);
	     		  
	    	  }
	      	
	      } 	  
	 
	    }
	    finally{
	      scanner.close();
	    }
	    return re;
	  }
	
	public static ArrayList<String> readhtmlfileFail() throws FileNotFoundException {
		ArrayList<String> re = new ArrayList<String>(); 
	    String fEncoding="UTF-8";
	    String tt = "";
	    int cc=0;
	  //  String NL = System.getProperty("line.separator");
	    Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
	    try {
	      while (scanner.hasNextLine()){
	    	  
	    	  tt=scanner.nextLine();
	    	  tt=tt.trim();
	    	
	    	  if (tt.contains("FAILED TIMS TEST CASES"))
	    	  {
	    		  cc=1;
	    	  }
	    	  if (tt.contains("FAILED NON-TIMS TEST CASE ERROR LIST"))
	    	  {
	    		  return re;
	    	  }
	    	  if(cc==1 && tt.contains("Ttv")  && !(tt.contains("Precheck")) && !(tt.contains("Precondition")))
	    	  {
	    		  int charCount = 0;
	    		  int pos=0;
	    		  if (!tt.startsWith("Ttv"))
	    		  {
	    	        for(int i =0 ; i<tt.length(); i++)
	    	        {
	    	            if(tt.charAt(i) == '_')
	    	            {
	    	                charCount++;
	    	                if (charCount==2)
	    	                {	   
	    	                  pos=i;
	    	                  break;
	    	                } 
	    	            }
	    	        }
	    		  }

	   
	    	    String tcid = null;
	    	    if (charCount==2 && (!tt.startsWith("Ttv"))){
	      	    	int k=nthOccurrence(tt,'_',0);
	    		    tcid=tt.substring(k+1, tt.length()-5);
	    		    if (!tcid.startsWith("Ttv")){
	    		    	tcid=tt.substring(16, tt.length()-5);
	    		    }
	    		}  	    
	    	    else if (charCount==2){
	    		tcid=tt.substring(pos-11, tt.length()-5);
	    		}
	       		else
	    		{
	    		  tcid=tt.substring(pos+16, tt.length()-5);			
	    		}
	    		//System.out.println(tt);
	    		System.out.println(tcid);
	    		re.add(tcid);
	     		  
	    	  }
	      	
	      } 	  
	 
	    }
	    finally{
	      scanner.close();
	    }
	    return re;
	  }
	public static int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos+1);
	    return pos;
	}
}