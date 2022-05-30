package testDB;
import db.*;
import db.mysql.*;
import entity.user;
public class TestDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();

		user dummyUser=new user("DummyUser0","1234");
		dummyUser.addAttributes("Captain");
		dummyUser.addAttributes("District 1");
		dummyUser.addAttributes("Connector");
		dummyUser.setFirstname("Dummy");
		dummyUser.setLastname("User");
		
		connection.insertUser(dummyUser);
	}

}
