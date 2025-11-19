package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Authentication {

	List<User> user;

	Authentication() {
		user = new ArrayList<>();
	}

	public User login(String mailId, String password) {
		String sql = "SELECT * FROM users WHERE mailId = ? AND password = ?";
		try (PreparedStatement stm = Connect.getConnection().prepareStatement(sql)) {

			stm.setString(1, mailId);
			stm.setString(2, encryptPassword(password));

			try (ResultSet result = stm.executeQuery()) {
				if (result.next()) {
					return new User(result.getInt(1), result.getString(2), result.getDate(3), result.getString(4),
							result.getString(5));
				}
			}

		} catch (Exception e) {
			System.out.println(Color.RED + "Enter the valid data : " + Color.RESET);
			e.printStackTrace();
		}
		return null;
	}

	public User signUp(String userName, Date date, String type, String password) {
		String mail = genarateMail(userName);
		String sql = "INSERT INTO users (name, dateOfBirth, mailId, type, password) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stm = Connect.getConnection().prepareStatement(sql,
				PreparedStatement.RETURN_GENERATED_KEYS)) {

			stm.setString(1, userName);
			stm.setDate(2, new java.sql.Date(date.getTime()));
			stm.setString(3, mail);
			stm.setString(4, type);
			stm.setString(5, encryptPassword(password));

			int rs = stm.executeUpdate();

			try (ResultSet result = stm.getGeneratedKeys()) {
				if (result.next()) {
					User userObj = new User(result.getInt(1), userName, date, mail, type);
					System.out.println("Sign up successfully");
					System.out.println("Your Zmail is : " + mail);
					return userObj;
				}
			}

		} catch (Exception e) {
			System.out.println(Color.RED + "Enter the valid data : " + Color.RESET);
			e.printStackTrace();
		}
		return null;
	}

	String encryptPassword(String password) {
		String encrypt = "";
		for (int i = 0; i < password.length(); i++) {
			encrypt += (password.charAt(i) + 10);
		}
		return encrypt;
	}

	String decryptPassword(String password) {
		String encrypt = "";
		for (int i = 0; i < password.length(); i++) {
			encrypt += (password.charAt(i) - 10);
		}
		return encrypt;
	}

	String genarateMail(String userName) {
		return userName + (int) (Math.random() * (10000 - 1 + 1)) + 1 + "@zmail.com";

	}
}