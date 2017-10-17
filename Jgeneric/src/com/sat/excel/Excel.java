package com.sat.excel;

import java.sql.*;

public class Excel {

	public static void main(String args[]) throws ClassNotFoundException,
			SQLException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		String myDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=c:/data.xls;"
				+ "DriverID=22;READONLY=false";
		Connection con = DriverManager.getConnection(myDB, "", "");

		Statement st = con.createStatement();
		System.out.println("Connection Created");
		String query = "select * from [Sheet1$] ";
		ResultSet rs = st.executeQuery(query);
		System.out.println("Rs Created");
		while (rs.next()) {
			System.out.println(rs.getString("sa"));
			System.out.println(rs.getString(1));
		}

	}
}