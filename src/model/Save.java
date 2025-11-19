package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Save {
	private int userId;
	private List<ZMail> savedMails;

	public Save(int userId) {
		this.userId = userId;
		this.savedMails = new ArrayList<>();
	}

	public void getSavedMails() {
//        List<ZMail> saved = new ArrayList<>();
		String sql = """
				    SELECT z.*
				    FROM zmail z
				    JOIN saved_mails s ON z.id = s.mailId
				    WHERE s.userId = ?;
				""";

		try (Connection conn = Connect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ZMail mail = new ZMail(rs.getInt("id"), rs.getString("sender_mail"), rs.getString("receiver_mail"),
						rs.getString("cc"), rs.getTimestamp("mail_date"), rs.getString("subject"),
						rs.getString("content"));
				mail.setIsFavorite(rs.getBoolean("isFavorite"));
				if (rs.getBoolean("isRead"))
					mail.markAsRead();
				savedMails.add(mail);
			}

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
		}

//        return saved;
	}

	public void addMail(ZMail mail) {
		savedMails.add(mail);

		String sql = "INSERT INTO saved_mails (userId, mailId) VALUES (?, ?)";

		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

			stmt.setInt(1, userId);
			stmt.setInt(2, mail.getId()); // Use mail ID from ZMail object
			stmt.executeUpdate();

			System.out.println("Mail ID " + mail.getId() + " added to inbox for user " + userId);

		} catch (Exception e) {
			System.out.println("This mail already saved");
		}
//    	String sql = "INSERT INTO zmail (content, sender_mail, receiver_mail, subject, cc, mail_date, isRead, isFavorite) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//        try  {
//        	Connection conn = Connect.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, mail.getContent());
//            stmt.setString(2, mail.getFrom());
//            stmt.setString(3, mail.getTo());
//            stmt.setString(4, mail.getSubject());
//            stmt.setString(5, mail.getCc());
//            stmt.setTimestamp(6, new java.sql.Timestamp(mail.getDate().getTime()));
//            stmt.setBoolean(7, mail.getIsRead());
//            stmt.setBoolean(8, mail.getIsFavorite());
//
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void getMails(int id) {

		if (savedMails.isEmpty())
			getSavedMails();
		for (ZMail mail : savedMails) {
			if (mail.getId() == id)
				mail.showFullDetails();
		}
	}

	int showMails() {
		if (savedMails.isEmpty())
			getSavedMails();
		for (ZMail mail : savedMails) {
			System.out.println(mail.getMailDetails());
		}
		return savedMails.size();
	}
	
	
	public void removeSavedMail(int userId, int mailId) {
	    String sql = "DELETE FROM saved_mails WHERE userId = ? AND mailId = ?";

	    try (Connection conn = Connect.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, userId);
	        
	        stmt.setInt(2, mailId);
	        int r = stmt.executeUpdate();
	        savedMails.clear();
	        getSavedMails();
	        System.out.println("Mail removed from saved list for user " + userId);

	    } catch (Exception e) {
	    	System.out.println("Enter the valid mailid");
	    }
	}
}

