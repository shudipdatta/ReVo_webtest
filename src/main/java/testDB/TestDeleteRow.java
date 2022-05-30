package testDB;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;

public class TestDeleteRow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		user us = connection.searchUser("Pengpeng");
		connection.deleteUser(us);
		mission ms = connection.searchMission("Display Port");
		connection.deleteMission(ms);
		
		Match mc = connection.searchMatch(ms.getMissionName(), us.getUsername());
	}

}
