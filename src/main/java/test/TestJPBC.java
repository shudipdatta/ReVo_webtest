package test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.mission;
import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
//import java.lang.Math.*;
import revoabe.*;

public class TestJPBC {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pairing pairing = PairingFactory.getPairing("./src/main/java/a.properties");
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		int nodecount = 1000;
		mission testMission=new mission("Display Port", nodecount);
		ReVo_ABE testABE = new ReVo_ABE(pairing, nodecount);
		List<String> attr_list= new ArrayList<String>();
		attr_list.add("phd");
		attr_list.add("master");
		int user_id = 3;
		PrivateKey prik = testABE.keyGen(attr_list, user_id);
		System.out.println(prik);
		prik.printPrivateKey();

		
		
		MasterKey mk = testABE.getMasterKey();
		System.out.println(mk);
		mk.printMasterKey();
		
		PublicKey pk = testABE.getPublicKey();
		System.out.println(pk);
		pk.printPublicKey();
		
		byte myb[];
		myb=pairing.getG1().newRandomElement().toBytes();
		System.out.println(myb.length);
		

		Element e1 = pairing.getG1().newRandomElement();
		Element e2 = pairing.getG1().newElementFromBytes(e1.toBytes());
		if(e1.isEqual(e2)) {
			System.out.printf("e1 (%s)== e2 (%s)", e1.toString(), e2.toString());
		}else {
			System.out.println("e1 != e2");
		}

		testMission.setupKeysFromReVo(testABE);
		Timestamp tp = new Timestamp(System.currentTimeMillis()+100000000);
		testMission.setEndTime(tp);
		testMission.setStartTime(tp);
		DBConnection connection = DBConnectionFactory.getConnection();
		connection.insertMission(testMission);
		System.out.println(testMission.getG1().length);
		System.out.println(testMission.getG2().length);
		System.out.println(testMission.getG1a().length);
		System.out.println(testMission.getG2_beta().length);
		System.out.println(testMission.getE_gg_alpha().length);
		
		
		
	}

}
