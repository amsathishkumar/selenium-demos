package exp.server;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SSH {
	public static boolean utilDebug = true;
	public static Connection createConnection(String hostname, String username,
			String password) throws IOException {
		Connection conn = new Connection(hostname);
		conn.connect();

		// Authentication with user name and password
		boolean isAuthenticated = conn.authenticateWithPassword(username,
				password);

		if (utilDebug)
			System.out.print("Authenticating...");

		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");

		if (utilDebug)
			System.out.println("Done");

		return conn;
	}
	
	public static void closeSession(Session sess) {
		// Close the session
		if (utilDebug)
			System.out.print("Closing the session...");
		sess.close();
		if (utilDebug)
			System.out.println("Done");

		return;
	}
	public static void closeConnection(Connection conn) {
		// Close the connection.
		if (utilDebug)
			System.out.print("Closing the connection...");

		conn.close();

		if (utilDebug)
			System.out.println("Done");

		return;
	}
	
	public static Session createSession(Connection conn, String PTY)
			throws IOException {
		if (utilDebug)
			System.out.print("Creating  a session...");

		// Create a session
		Session sess = conn.openSession();
		if (utilDebug)
			System.out.println("Done");

		// PTY type
		sess.requestPTY(PTY);

		return sess;

	}

	
}
