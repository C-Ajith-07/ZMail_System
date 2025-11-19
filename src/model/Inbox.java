package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Inbox {
	private int userId;
	private List<ZMail> mails;

	public Inbox(int userId) {
		this.userId = userId;
		this.mails = new ArrayList<>();
	}
	
//	void loadZMails(String mail) {
//		try {
//			PreparedStatement stm = Connect.getConnection().prepareStatement("SELECT z.*\n"
//					+ "FROM zmail z"
//					+ "JOIN inbox i ON z.id = i.mailId\n"
//					+ "WHERE i.userId = ?;");
//			stm.setInt(1, userId);
//			ResultSet result = stm.executeQuery();
//			while (result.next()) {
//				ZMail zmail = new ZMail(result.getInt(1),result.getString(3), result.getString(4), result.getString(6), result.getDate(7), result.getString(6), result.getString(2));
//				mails.add(zmail);
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//	}
	
	public void loadZMails(String usermail) {
	    String sql = """
	        SELECT z.*
	        FROM zmail z
	        JOIN inbox i ON z.id = i.mailId
	        WHERE i.userId = ?
	    """;

	    try (Connection conn = Connect.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            ZMail mail = new ZMail(
	                rs.getString("sender_mail"),
	                rs.getString("receiver_mail"),
	                rs.getString("cc"),
	                rs.getTimestamp("mail_date"),
	                rs.getString("subject"),
	                rs.getString("content")
	            );
	            mail.setId(rs.getInt("id"));
	            mails.add(mail);
	        }

	    } catch (Exception e) {
        	System.out.println(Color.RED+"We can't get Mail in DB : "+Color.RESET);
	    }

	}


	public int alreadyRead(String mail) {
		if(mails.isEmpty())loadZMails(mail);
		for (ZMail m : mails) {
			if(m.isIsRead()) {

				System.out.println(m.getMailDetails());	
			}
		}
		return mails.size();

	}

	public int yetToRead(String mail) {
		if(mails.isEmpty())loadZMails(mail);
		for (ZMail m : mails) {
			if(!m.isIsRead()) {

				System.out.println(m.getMailDetails());	
			}
		}
		return mails.size();
	}

	public void searchEmail(String mail,String keyword) {
		if(mails.isEmpty())loadZMails(mail);
		for (ZMail m : mails) {
			if(m.getCc().contains(keyword) || m.getContent().contains(keyword)|| m.getFrom().contains(keyword)) {

				System.out.println(m.getMailDetails());	
			}
		}
	}

	public void addMail(ZMail mail) {
		mails.add(mail);
	    String sql = "INSERT INTO inbox (userId, mailId) VALUES (?, ?)";

	    try ( 
	         PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

	        stmt.setInt(1, userId);
	        stmt.setInt(2, mail.getId()); //  Use mail ID from ZMail object
	        stmt.executeUpdate();

	        System.out.println("Mail ID " + mail.getId() + " added to inbox for user " + userId);

	    } catch (Exception e) {
        	System.out.println(Color.RED+"you can't Add Mail in DB : "+Color.RESET);
	    }
	}

	int showMails(String mail) {
		if(mails.isEmpty())loadZMails(mail);
		for (int i = 0; i < mails.size(); i++) {
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(mails.get(i).getMailDetails());
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
		return mails.size();
	}
	
//	return mail with id
	ZMail getMail(int id,String mail) {
		if(mails.isEmpty())loadZMails(mail);
		for (ZMail m : mails) {
			if(m.getId() == id) {

				return m;
			}
		}
		return null;
	}
	
	
	public void removeMail(int userId, int mailId,String zmail) {
	    String sql = "DELETE FROM inbox WHERE userId = ? AND mailId = ?";

	    try (
	         PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

	        stmt.setInt(1, userId);
	        
	        stmt.setInt(2, mailId);
	        int r = stmt.executeUpdate();
	        mails.clear();
	        loadZMails(zmail);
	        System.out.println("Mail removed from saved list for user " + userId);

	    } catch (Exception e) {
	    	System.out.println("Enter the valid mailid");
	    }
	}
	

}
