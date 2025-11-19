package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
	private int userId;
	private String name;
	private Date dateOfBirth;
	private String mailId;
	private String type; // "private" or "public"
	private List<Filter> filters;
	private List<Group> groups;
	Save save;
	SentMails sharedMails;
	Inbox inbox;
	Favorite favorite;

	public User(int userId, String name, Date dob, String mailId, String type) {
		this.userId = userId;
		this.name = name;
		this.dateOfBirth = dob;
		this.mailId = mailId;
		this.type = type;
		this.filters = new ArrayList<>();
		this.groups = new ArrayList<>();
		inbox = new Inbox(userId);
		save = new Save(userId);
		sharedMails = new SentMails(userId);
		favorite = new Favorite(userId);
//        inbox = new
	}
	
	public int getUserId() {
		return userId;
	}
	

    public String getMailId() {
        return mailId;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void addGroup(Group group) {
        groups.add(group);
        
    }
    
    public void createAndAssignFilter(String filterName) {
        String insertFilter = "INSERT INTO filters (name) VALUES (?)";
        String assignFilter = "INSERT INTO filter_users (filterId, userId) VALUES (?, ?)";

        try (
             PreparedStatement insertStmt = Connect.getConnection().prepareStatement(insertFilter, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, filterName);
            insertStmt.executeUpdate();

            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                int filterId = rs.getInt(1);

                try (PreparedStatement assignStmt = Connect.getConnection().prepareStatement(assignFilter)) {
                    assignStmt.setInt(1, filterId);
                    assignStmt.setInt(2, userId);
                    assignStmt.executeUpdate();
                }

                Filter newFilter = new Filter(filterId, filterName);
                addFilter(newFilter); 
                System.out.println("Filter created and assigned: " + filterName);
            }

        } catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid Filter name : "+Color.RESET);
        }
    }

    
    
    public Group createAndAssignGroup(String groupName) {
        String insertGroup = "INSERT INTO user_groups (groupName, leaderId) VALUES (?, ?)";
        String assignGroup = "INSERT INTO group_users (groupId, userId) VALUES (?, ?)";

        try (
             PreparedStatement insertStmt = Connect.getConnection().prepareStatement(insertGroup, PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertStmt.setString(1, groupName);
            insertStmt.setInt(2, userId); // user is the leader
            insertStmt.executeUpdate();

            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                int groupId = rs.getInt(1);

                try (PreparedStatement assignStmt = Connect.getConnection().prepareStatement(assignGroup)) {
                    assignStmt.setInt(1, groupId);
                    assignStmt.setInt(2, userId);
                    assignStmt.executeUpdate();
                }

                Group newGroup = new Group(groupId, groupName, userId);
                addGroup(newGroup); 
                System.out.println("Group created and assigned: " + groupName);
                return newGroup;
            }

        } catch (Exception e) {
        	System.out.println(Color.RED+"Enter the valid Group name : "+Color.RESET);
        }
        return null;
    }


    //  Load filters from DB
    public void getFiltersFromDB() {
        String sql = """
            SELECT f.filterId, f.name AS filterName
            FROM filters f
            JOIN filter_users fu ON f.filterId = fu.filterId
            WHERE fu.userId = ?;
        """;

        try (
             PreparedStatement stm = Connect.getConnection().prepareStatement(sql)) {

            stm.setInt(1, userId);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                int filterId = result.getInt("filterId");
                String filterName = result.getString("filterName");
                filters.add(new Filter(filterId, filterName));
            }

        } catch (Exception e) {
        	System.out.println(Color.RED+"We can't get filter in DB : "+Color.RESET);
        }
    }

    //  Load groups from DB
    public void getGroupsFromDB() {
        String sql = """
            SELECT g.groupId, g.groupName, g.leaderId
            FROM user_groups g
            JOIN group_users gu ON g.groupId = gu.groupId
            WHERE gu.userId = ?;
        """;

        try (
             PreparedStatement stm = Connect.getConnection().prepareStatement(sql)) {

            stm.setInt(1, userId);
            ResultSet result = stm.executeQuery();

            while (result.next()) {
                int groupId = result.getInt("groupId");
                String groupName = result.getString("groupName");
                int leaderId = result.getInt("leaderId");
                groups.add(new Group(groupId, groupName, leaderId));
            }

        } catch (Exception e) {
        	System.out.println(Color.RED+"We can't get group in DB : "+Color.RESET);
        }
    }

    
    
    //  Show filters
    public int showFilter() {
        if (filters.isEmpty()) getFiltersFromDB();
        for (Filter f : filters) {
            System.out.println("filter id : "+f.getFilterId()+"\tfilter name : "+f.getName());
        }
        return filters.size();
    }
    
    

    // Show groups 
    public int showGroups() {
        if (groups.isEmpty()) getGroupsFromDB();
        for (Group g : groups) {
            System.out.println("group id : "+g.getGroupId()+"\tgroupName : "+g.getGroupName());
        }
        return groups.size();
    }
    
    
    String getUserDetails() {
    	return "userId "+userId+"\tuserName : "+name+"\tDate of birth : "+dateOfBirth+"\tmailId :"+mailId+"\ttype : "+type;
    }
    
//	return filter 
	
	Filter getFilter(int id) {
		if(filters.isEmpty())getFiltersFromDB();
		for (Filter f : filters) {
			if(f.getFilterId() == id) {

				return f;
			}
		}
		return null;
	}
	
	Group getGroup(int id) {
		if(filters.isEmpty())getFiltersFromDB();
		for (Group g : groups) {
			if(g.getGroupId() == id) {

				return g;
			}
		}
		return null;
	}
	
	
	public void showSavedMails() {
		save.showMails();
	}
	
	public void showFavoriteMails() {
		favorite.showMails();
	}
	
	public void showSharedMails() {
		sharedMails.showMails();
	}
	
	
	public Filter getFilter(String mail) {
		if(filters.isEmpty())getFiltersFromDB();
		for (Filter f : filters) {
			if(f.getUser(mail)!=null)
				return f;
		}
		return null;
	}
	
	
//	public String getMailId() {
//		return mailId;
//	}
//
//	public void addFilter(Filter filter) {
//		filters.add(filter);
//	}
//
//	public void addGroup(Group group) {
//		groups.add(group);
//	}
//
//	void showGroups() {
//		for (int i = 0; i < groups.size(); i++) {
//			System.out.println(groups.get(i));
//		}
//	}
//
//	void getfilter() {
//		try {
//			PreparedStatement stm = Connect.getConnection()
//					.prepareStatement("SELECT f.filterId, f.name AS filterName\n" + "        FROM filters f\n"
//							+ "        JOIN filter_users fu ON f.filterId = fu.filterId\n"
//							+ "        WHERE fu.userId = ?;");
//			stm.setInt(1, userId);
//			ResultSet result = stm.executeQuery();
//			while (result.next()) {
////				filters.add(new Filter(result.getInt(1), result.getString(result.getString(2))));
//			}
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//	}
//
//	void showFilter() {
//		if (filters.isEmpty())
//			getfilter();
//		for (int i = 0; i < filters.size(); i++) {
//			System.out.println(filters.get(i));
//		}
//	}
}
    
