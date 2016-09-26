package exp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class ExceptionSniper {
	public static Session essess = null;
	public static Connection esconn = null;
	public static InputStream esstdout = null;
	public static int SATinit = 0;
	public static String servicenodeip = null;
	public static String mailid = null;
	public static String status = null;
	public static void main(String[] arg) throws SocketException, IOException, InterruptedException, AddressException {
		
		openstack();
//
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		System.out.print("Enter the SD Service Node IP (eg:10.78.216.157): ");
//		servicenodeip = reader.readLine();
//		
//		BufferedReader reader1 = new BufferedReader(new InputStreamReader(System.in));
//		System.out.print("Enter you mail id (eg:smuniapp@cisco.com): ");
//		mailid=reader1.readLine();
//		
//		BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
//		System.out.print("Enter you Service statup (eg:UP/DOWN): ");
//		status=reader2.readLine();
//		
//		try {
//			
//			
//			
//			
//			InetAddress address = InetAddress.getByName(servicenodeip);
//			if (address.isReachable(3000)) {
//				sendmail("Service test against "+servicenodeip,mailid);
//				// System.out.println("Host with IP:"+address+ " is pingable");
//				// sendmail("Exception Sniper Started against "+servicenodeip);
//				// esStart();
//				// sendmail("Exception Sniper stopped against "+servicenodeip);
//				while (true) {
//					String ss = "<html><table>";
//					SDProcess.sdStatus(servicenodeip);
//					String pp[] = SDProcess.sathish.split("<tr>");
//					for (String oo : pp) {
//						if (oo.contains(status))
//							ss = ss + oo;
//					}
//					if (ss.contains("status : \""+status+"\""));
//						sendmail(ss + "</table></html>",mailid);
//					Thread.sleep(2000);
//					System.out.println("restarted");
//					SDProcess.sathish="<tr>";
//					
//				}
//			} else {
//				System.out.println("Host with IP:" + address + " is not pingable");
//				System.out.println("Exception Snipper is Stopped....");
//				sendmail("Host with IP:" + address + " is not pingable",mailid);
//				System.exit(2);
//			}
//
//		} catch (Exception e) {
//			sendmail("Exception Sniper stopped on" + servicenodeip,mailid);
//		}
	}

	
	public static void esStart() throws SocketException, IOException, InterruptedException, AddressException {

		System.out.println("Core sniper started" + servicenodeip);
		String cmd;
		String servip = servicenodeip;
		String servuser = "root";
		String servpwd = "2g3n3r!c";
		System.out.println(servip);
		esconn = SSH.createConnection(servip, servuser, servpwd);
		essess = SSH.createSession(esconn, "vt100");
		esstdout = essess.getStdout();
		essess.startShell();
		Thread.sleep(3000);
		// cmd = "cd /opt/jboss/jboss-as-7.1.0.Final/standalone/log \n";
		cmd = "cd /opt/vcs/logs/vcsconsole \n";

		essess.getStdin().write(cmd.getBytes());
		essess.getStdin().flush();
		Thread.sleep(2000);

		// cmd = "grep -i -A10 -B10 -n Error server.log* \n";
		// cmd = "tail -f server.log | grep -ie 'error\\|exception' \n";
		cmd = "tail -f * | grep -ie 'error\\|exception\\|WebApplicationExceptionMapper' \n";
		essess.getStdin().write(cmd.getBytes());
		essess.getStdin().flush();
		Thread.sleep(3000);
		while (true) {
			esread();
			Thread.sleep(10000);

			InetAddress address = InetAddress.getByName(servicenodeip);
			if (!address.isReachable(3000)) {
				System.out.println("Host with IP:" + address + " is not pingable");
				sendmail("Host with IP:" + address + " is not pingable","smuniapp@cisco.com");
				System.out.println("Exception Snipper is Stopped...." + servicenodeip);
				sendmail("Exception Snipper is Stopped....in " + servicenodeip,"smuniapp@cisco.com");
				System.exit(2);
			}

		}

	}
	
	public static void openstack() throws SocketException, IOException, InterruptedException, AddressException {

		System.out.println("openstack Started");
		String msg = "<html><head>";
		msg += "<style>";
		msg += "body{position:absolute;width:80%;height:100%;margin:0;padding:0}table,tbody{position:relative;width:100%;table-layout: auto}tr td,th{width:.5%;word-break:break-all;border:.5px solid black;} ";
		msg += "</style></head><body>";
		String cmd;
		String servip = "10.78.206.160";
		String servuser = "root";
		String servpwd = "generic";
		System.out.println(servip);
		esconn = SSH.createConnection(servip, servuser, servpwd);
		essess = SSH.createSession(esconn, "vt100");
		esstdout = essess.getStdout();
		essess.startShell();
		Thread.sleep(3000);
    	cmd = "./openstack_details.sh \n";
		
		essess.getStdin().write(cmd.getBytes());
		essess.getStdin().flush();
		Thread.sleep(20000);
		 String [] value = openstackread();
		 
		 msg=msg+extract(value,"Openstack Summary,Used,Max")+extract(value,"Instances List")+extract(value,"Image List:")+"</body><html>";
				 
System.out.println(msg);
		
sendmail(msg,"smuniapp@cisco.com");



	}


	private static String extract(String[] value, String search) {
		int str1=0;
		String table="<table border='200%'>";
		 for (int i=0;i<value.length;i++)
		 {
			if (value[i].trim().equals(search))
			{
				str1=1;
				table=table+"<tr><td>";
			}
			if (value[i].trim().equals(""))
			{
				str1=0;	
				table=table+"</td></tr>";
			}
				
			
		    if 	(str1==1 && !(value[i].trim().equals("")))
		    {
		    	table=table+value[i].replaceAll(",", "</td> <td>");
		    	//table=table.replaceAll("\"", "");
		    	table=table+"</tr><tr><td>";
		    	System.out.println(table);	
		    }
				
		 }
		 
		 return(table+"</table>");
	}

	public static void esread() {
		System.out.println("Core readed on" + servicenodeip);
		String[] corelines = null;
		String[] checkdump = null;
		try {
			byte[] buffer = new byte[5000000];

			while (true) {
				System.out.println("Core readed started");
				if (esstdout != null) {
					if (esstdout.available() == 0) {

						int conditions = essess.waitForCondition(ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA | ChannelCondition.EOF, 2000);
						if ((conditions & ChannelCondition.TIMEOUT) != 0) {
							break;
						}

						if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0)
							throw new IllegalStateException("Unexpected condition result (" + conditions + ")");
					}

					if (esstdout.available() > 0) {
						int len = esstdout.read(buffer);
						String output = "";
						for (int i = 0; i < len; i++) {
							output += ((char) buffer[i]);
						}
						System.out.println(output);
						corelines = output.split("\n");
						checkdump = new String[corelines.length];
						for (int i = 0; i < corelines.length; i++) {
							String ll = corelines[i];
							checkdump[i] = ll;
						//	System.out.println("DumpLine > " + ll);
						}

						// System.out.println("Core Total lines:"+
						// checkdump.length);
					}
				} else
					break;

			}
			String coredetail = "Details: ";
			if (checkdump != null) {
				System.out.println("Details Begin");
				for (int sat = 0; sat < checkdump.length; sat++) {
					coredetail = coredetail + checkdump[sat];
					System.out.println(checkdump[sat]);
				}
				if (SATinit != 0)
					sendmail(coredetail,mailid);
				else
					SATinit = 1;
				System.out.println("Details End");
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}
	
	
	
	public static String [] openstackread() {
		String[] checkdump = null;
		try {
			byte[] buffer = new byte[5000000];

			while (true) {
				if (esstdout != null) {
					if (esstdout.available() == 0) {

						int conditions = essess.waitForCondition(ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA | ChannelCondition.EOF, 2000);
						if ((conditions & ChannelCondition.TIMEOUT) != 0) {
							break;
						}

						if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0)
							throw new IllegalStateException("Unexpected condition result (" + conditions + ")");
					}

					if (esstdout.available() > 0) {
						int len = esstdout.read(buffer);
						String output = "";
						for (int i = 0; i < len; i++) {
							output += ((char) buffer[i]);
						}
						//System.out.println("sathish"+output);
						
						String[] lines = output.split("\n");
						checkdump = new String[lines.length];
						for (int i = 0; i < lines.length; i++) {
							String ll = lines[i];
							checkdump[i] = ll;
							System.out.println("DumpLine > " + ll);
						}
					}
				} else
					break;

			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return checkdump;

	}

	public static void sendmail(String str, String mailid) throws AddressException {

		// Get the session object
		String host = "outbound.cisco.com";// or IP address
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		String[] to = mailid.split(",");
		String from = "Kalam_Openstack@cisco.com";// change accordingly

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);
		InternetAddress[] toAddress = new InternetAddress[to.length];
		// To get the array of addresses
		for (int i = 0; i < to.length; i++) { // changed from a while loop
			toAddress[i] = new InternetAddress(to[i]);
		}

		// compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			// message.addRecipient(Message.RecipientType.TO,new
			// InternetAddress(to));
			message.setSubject("KALAM Openstack Status" );
			// message.setText(str);

			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(str, "text/html");
			mp.addBodyPart(htmlPart);
			message.setContent(mp);

			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....to" + servicenodeip);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

}
