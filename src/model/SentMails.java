package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SentMails {
    private int userId;
    private List<ZMail> sharedMails;

    public SentMails(int userId) {
        this.userId = userId;
        this.sharedMails = new ArrayList<>();
    }
    
    
//    add mails

    public void addMail(ZMail mail) {
        sharedMails.add(mail);

        String sql = "INSERT INTO shared_mails (userId, mailId) VALUES (?, ?)";

        try (
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, mail.getId());
            stmt.executeUpdate();

            System.out.println("Mail ID " + mail.getId() + " shared with user " + userId);

        } catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        }
    }

    //  Load shared mails from DB
    public void loadSharedMailsFromDB() {
        String sql = """
            SELECT z.*
            FROM zmail z
            JOIN shared_mails s ON z.id = s.mailId
            WHERE s.userId = ?
        """;

        try ( 
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

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
                mail.setIsFavorite(rs.getBoolean("isFavorite"));
                if (rs.getBoolean("isRead")) mail.markAsRead();
                mail.setId(rs.getInt("id"));
                sharedMails.add(mail);
            }

        } catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        }
    }
    
    
    public void getMails(int id) {

        if (sharedMails.isEmpty()) loadSharedMailsFromDB();
        for (ZMail mail : sharedMails) {
        	if(mail.getId() == id)
            mail.showFullDetails();
        }
    }

    //  Show shared mails
    public int showMails() {
        if (sharedMails.isEmpty()) loadSharedMailsFromDB();
        for (ZMail mail : sharedMails) {
            System.out.println(mail.getMailDetails());
        }
        return sharedMails.size();
    }
    
	public void removeSavedMail(int userId, int mailId) {
	    String sql = "DELETE FROM shared_mails WHERE userId = ? AND mailId = ?";

	    try (Connection conn = Connect.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, userId);
	        
	        stmt.setInt(2, mailId);
	        int r = stmt.executeUpdate();
	        sharedMails.clear();
	        loadSharedMailsFromDB();
	        System.out.println("Mail removed from saved list for user " + userId);

	    } catch (Exception e) {
	    	System.out.println("Enter the valid mailid");
	    }
	}
}

