package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ZMail {

	// Data members ---------------
	private int id;
	private String from;
	private String to;
	private String cc;
	private String subject;
	private String content;
	private Date date;
	private boolean isRead;
	private boolean isFavorite;

	// Constructor -------------------

	public ZMail(String from, String to, String cc, Date date, String subject, String content) {
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.content = content;
		this.date = date;
		isRead = false;
		isFavorite = false;
	}
	
	public ZMail(int id,String from, String to, String cc, Date date, String subject, String content) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.content = content;
		this.date = date;
		isRead = false;
		isFavorite = false;
	}

	// ================================================================ getter
	// setter ===============================================================

	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id= id ;
	}
	
	// getFrom --
	public String getFrom() {
		return this.from;
	}

	// setFrom ----
	public void setFrom(String from) {
		if (from != null) {
			this.from = from;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	// getTo ----
	public String getTo() {
		return this.to;
	}

	// setTo ----
	public void setTo(String to) {
		if (to != null) {
			this.to = to;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	// getCc ---
	public String getCc() {
		return this.cc;
	}

	// setCc ---
	public void setCc(String cc) {
		if (cc != null) {
			this.cc = cc;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	// getSubject ---
	public String getSubject() {
		return this.subject;
	}

	// setSubject ---
	public void setSubject(String subject) {
		if (subject != null) {
			this.subject = subject;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	// getContent ---
	public String getContent() {
		return this.content;
	}

	// setContent ---
	public void setContent(String content) {
		if (content != null) {
			this.content = content;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	// getDate----
	public Date getDate() {
		return this.date;
	}

	// setDate ---
	public void setDate(Date date) {

		if (date != null) {
			this.date = date;
		} else {
			System.out.println("Enter the valid input");
		}
	}

	public boolean isIsRead() {
		return this.isRead;
	}

	// getIsRead ---
	public boolean getIsRead() {
		return this.isRead;
	}

	// setIsRead ---
	public void markAsRead() {

		String sql = "UPDATE zmail SET isRead = TRUE WHERE id = ?;";
		try {
			Connection conn = Connect.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			this.isRead = true;
			stmt.executeUpdate();
		} catch (SQLException e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
		} catch (ClassNotFoundException e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
		}
	}

//    markAsFavorite
	public void markAsFavorite() {
		String sql = "UPDATE zmail SET isFavorite = TRUE WHERE id = ?";

		try (Connection conn = Connect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, id);
			stmt.executeUpdate();
			this.isFavorite = true;

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
		}
	}

	// getIsRead ---
	public boolean getIsFavorite() {
		return this.isFavorite;
	}

	// setIsRead ---
	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	String getMailDetails() {
		
		return "|Mail id : "+id+"\t|From : " + from + "\t|To : " + to + "\t|cc : " + cc + "\t|subject : " + subject + "\t|date : " + date;
	}
	
	void showFullDetails() {
		System.out.println("");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("|Mail id : "+id+"\n|From    : "+from+"\n|To      : "+to+"\n|cc      : "+cc+"\n|subject : "+subject+"\n|Date    : "+date+"\n|content : "+content );
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}

	
//	create mail
	public int send() {
		String sql = """
				    INSERT INTO zmail (sender_mail, receiver_mail, cc, subject, content, mail_date, isRead, isFavorite)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection conn = Connect.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, from);
			stmt.setString(2, to);
			stmt.setString(3, cc);
			stmt.setString(4, subject);
			stmt.setString(5, content);
			stmt.setTimestamp(6, new java.sql.Timestamp(date.getTime()));
			stmt.setBoolean(7, isRead);
			stmt.setBoolean(8, isFavorite);
//			System.out.println(from+to+cc+subject);
			int rows = stmt.executeUpdate();

			if (rows > 0) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						this.id = rs.getInt(1); // Save generated ID
						return id;
					}
				}
			}

		} catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
		}

		return -1; // failed
	}
	
	

}
