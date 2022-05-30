package testDB;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;

public class TestUpdataTable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		mission curMission = connection.searchMission("dog house");	
		user curUser = connection.searchUser("d1");
		Match curMatch = connection.searchMatch("dog house", "d1");
		if(curMatch != null)
			curMatch.printMatch();
		else
			System.out.println("there is no match");
		
		curMission.setMissionName("updated2 dog house");
		connection.updateMission(curMission);
		curUser.setUserName("updated2 d1");
		connection.updateUser(curUser);
		curMatch.setUserTreeID(3);
		connection.updateMatch(curMatch);
		
		
	}

}
