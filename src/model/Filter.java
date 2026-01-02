package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Filter {
	private int filterId;
	private String name;
	private List<User> users;
	private List<ZMail> mails;
	private static final Logger logger = LogManager.getLogger(Filter.class);

	public Filter(int filterId, String name) {
		this.filterId = filterId;
		this.name = name;
		this.users = new ArrayList<>();
		this.mails = new ArrayList<>();
	}

	public void addUser(int filterId, User user) {
		if (isUserAlreadyAssigned(user.getUserId())) {
			System.out.println("User already assigned to this filter.");
			return;
		}

		users.add(user);
		String sql = "INSERT INTO filter_users (filterId, userId) VALUES (?, ?)";

		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

			stmt.setInt(1, filterId);
			stmt.setInt(2, user.getUserId());
			stmt.executeUpdate();

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
		}
	}

	private boolean isUserAlreadyAssigned(int userId) {
		String sql = "SELECT COUNT(*) FROM filter_users WHERE filterId = ? AND userId = ?";
		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

			stmt.setInt(1, filterId);
			stmt.setInt(2, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
		}
		return false;
	}

	// Load users from DB
	public void loadUsersFromDB() {
		users.clear();
		String sql = """
				    SELECT u.userId, u.name, u.dateOfBirth, u.mailId, u.type
				    FROM users u
				    JOIN filter_users fu ON u.userId = fu.userId
				    WHERE fu.filterId = ?
				""";

		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

			stmt.setInt(1, filterId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int userId = rs.getInt("userId");
				String name = rs.getString("name");
				java.sql.Date dob = rs.getDate("dateOfBirth");
				String mailId = rs.getString("mailId");
				String type = rs.getString("type");

				User user = new User(userId, name, dob, mailId, type);
				users.add(user);
			}

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
		}
	}

	// Show users assigned to this filter
	public void showUsers() {
		if (users.isEmpty())
			loadUsersFromDB();
		for (User user : users) {
			System.out.println("User: " + user.getMailId());
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	public int getFilterId() {
		return filterId;
	}
	
	public User getUser(String mail) {
		if(users.isEmpty())loadUsersFromDB();
		for(User u:users) {
			if(u.getMailId().equals(mail)) {
				return u;
			}
		}
			return null;
	}

//    ===================== load mails ===============================

	public void addMail(ZMail mail) {
		mails.add(mail);
		String sql = "INSERT INTO filter_mails (filterId, mailId) VALUES (?, ?)";
		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {
			stmt.setInt(1, filterId);
			stmt.setInt(2, mail.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
		}
	}

	public void loadMailsFromDB() {
		mails.clear();
		String sql = """
				    SELECT z.*
				    FROM zmail z
				    JOIN filter_mails fm ON z.id = fm.mailId
				    WHERE fm.filterId = ?
				""";
		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {
			stmt.setInt(1, filterId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ZMail mail = new ZMail(rs.getString("sender_mail"), rs.getString("receiver_mail"), rs.getString("cc"),
						rs.getTimestamp("mail_date"), rs.getString("subject"), rs.getString("content"));
				mail.setId(rs.getInt("id"));
				mails.add(mail);
			}
		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
		}
	}

	void showMails() {
		if (mails.isEmpty())
			loadMailsFromDB();
		for (ZMail mail : mails) {
			System.out.println(mail.getMailDetails());
		}
	}
}