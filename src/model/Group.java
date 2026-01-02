package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Group {
    private int groupId;
    private String groupName;
    private List<User> users;    
    private List<ZMail> mails = new ArrayList<>();
    private int leaderId;
	private static final Logger logger = LogManager.getLogger(Group.class);


    public Group(int groupId, String groupName, int leaderId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.leaderId = leaderId;
        this.users = new ArrayList<>();
    }
    
    
	private boolean isUserAlreadyAssigned(int userId) {
		String sql = "SELECT COUNT(*) FROM group_users WHERE groupId = ? AND userId = ?";
		try (PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

			stmt.setInt(1, groupId);
			stmt.setInt(2, userId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (Exception e) {

        	System.out.println(Color.RED+"Enter the valid userId : "+Color.RESET);
        	logger.error(e.getMessage());
		}
		return false;
	}
    
    public void addUser(int groupId,User user) {
    	if(isUserAlreadyAssigned(user.getUserId())) {
    		System.out.println("This user already added");
    		return;
    	}
        users.add(user);

        String sql = "INSERT INTO group_users (groupId, userId) VALUES (?, ?)";
        try (
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();

        } catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid data : "+Color.RESET);
        	logger.error(e.getMessage());
        }
    }

    //  Load users from DB
    public void loadUsersFromDB() {
        users.clear();
        String sql = """
            SELECT u.userId, u.name, u.dateOfBirth, u.mailId, u.type
            FROM users u
            JOIN group_users gu ON u.userId = gu.userId
            WHERE gu.groupId = ?
        """;

        try (
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, groupId);
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
        	System.out.println(Color.RED+"User not found : "+Color.RESET);
        	logger.error(e.getMessage());
        }
    }

    //  Show users in group
    public void showUsers() {
        if (users.isEmpty()) loadUsersFromDB();
        for (User user : users) {
        	System.out.println(user.getUserDetails());;
        }
    }

    //  Getters
    public String getGroupName() {
        return groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getLeaderId() {
        return leaderId;
    }
    
    
//     ==================== load mail ====================


    public void addMail(int groupId,ZMail mail) {
        mails.add(mail);
        String sql = "INSERT INTO group_mails (groupId, mailId) VALUES (?, ?)";
        try (
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, groupId);
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
            JOIN group_mails gm ON z.id = gm.mailId
            WHERE gm.groupId = ?
        """;
        try (
             PreparedStatement stmt = Connect.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, groupId);
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
        	System.out.println(Color.RED+"Enter the valid user data : "+Color.RESET);
        	logger.error(e.getMessage());
        }
    }
    
    
    void showMails() {
    	if(mails.isEmpty())loadMailsFromDB();
    	for(ZMail mail:mails) {
    		System.out.println(mail.getMailDetails());
    	}
    }


//    public List<User> getUsers() {
//        return users;
//    }

//    public void addUser(User user) {
//        users.add(user);
//    }
//    
//    
//    public String getGroupName() {
//    	return groupName;
//    }
}