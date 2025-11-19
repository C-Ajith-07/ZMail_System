package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connect {
		private Connect() {
			// TODO Auto-generated constructor stub
		}

		public static Connection getConnection() throws SQLException, ClassNotFoundException {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return  DriverManager.getConnection("jdbc:mysql://localhost:3306/ZMail_System","root","@jith07");
		}

}
