package testDB;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;

public class TestSearchDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		
		Match curMatch = connection.searchMatch("dog house", "d1");
		if(curMatch != null)
			curMatch.printMatch();
		else
			System.out.println("there is no match");
	}

}
