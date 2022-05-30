package testDB;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Match;
import entity.mission;
import entity.user;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import revoabe.MasterKey;
import revoabe.PublicKey;
import revoabe.ReVo_ABE;

public class TestInsertRow {

	public static void main(String[] args) {
		DBConnection connection = DBConnectionFactory.getConnection();
		String username = "d1";
		String password = "p1";
		String firstname = "first_d1";
		String lastname = "last_d1";
		List<String> attributes =Arrays.asList("a1, a2, a3, a4, a5".split(","));
		List<String> interests =Arrays.asList("a6, a7, a8, a9, a0".split(","));
		user us = new user(username, password, firstname, lastname, attributes, interests);
		
		connection.insertUser(us);
		
		
		
		mission ms = new mission("dog house", 10);
		connection.insertMission(ms);
		
		
		ms = connection.searchMission(ms.getMissionName());
		us = connection.searchUser(us.getUsername());
		
		Match mc = new Match(ms, us, 1,true);
		connection.insertMatch(mc);
		
	}
	
	public static ReVo_ABE getABE(int capacity) {
		File curveFile=new File(System.getProperty("user.dir")+"\\src\\main\\java\\a.properties");
		System.out.println("File Directory = " + curveFile.getAbsolutePath());
		if(!curveFile.exists()) {
			curveFile = new File(managerAPI.ConstantForServer.projectDir+"/src/main/java/a.properties");
		}
		System.out.println("File Directory = " + curveFile.getAbsolutePath());
		Pairing pairing = PairingFactory.getPairing(curveFile.getAbsolutePath());
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		ReVo_ABE testABE = new ReVo_ABE(pairing, capacity);
		return testABE;
	}

}
