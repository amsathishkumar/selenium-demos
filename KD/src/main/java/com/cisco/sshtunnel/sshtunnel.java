package com.cisco.sshtunnel;


import com.jcraft.jsch.*;

import java.io.*;


public class sshtunnel {

	private Session session;

	public void connect(String user, String password, String host)
			throws JSchException {
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, 22);
		session.setConfig("PreferredAuthentications",
				"publickey,keyboard-interactive,password");
		session.setPassword(password);
		session.setConfig(config);
		session.connect();
		System.out.println("Connected to " + host);
	}
    public void portforward(String ip,int port) throws JSchException{
    	session.setPortForwardingL(port, ip, port);
        System.out.println("Port Forward to" + ip.trim() + " :"+port);
    	wait(5);

    }
	public String exec(String command) throws JSchException, IOException {
		String result = null;
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec) channel).setErrStream(System.err);
		InputStream in = channel.getInputStream();
		channel.connect();
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				result = new String(tmp, 0, i);
				System.out.println(result);
			}
			if (channel.isClosed()) {
				break;
			}
			wait(10);
		}
		channel.disconnect();
		System.out.println("Disconnected chanel " + channel.getExitStatus());
		return result;
	}

	public void close() {
		session.disconnect();
	}

	public void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			System.out.println("Thread exception " + e.getMessage());
		}
	}

}