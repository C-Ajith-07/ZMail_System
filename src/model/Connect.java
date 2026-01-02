package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	private static Connection con = null;

	private Connect() {
		// TODO Auto-generated constructor stub
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if (con == null || con.isClosed()) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/ZMail_System", "root", "@jith07");
		}
		return con;
	}

}
