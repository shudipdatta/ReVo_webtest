package test_revo_abe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import entity.EntityHelper;
import entity.Match;
import entity.mission;
import entity.user;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import revoabe.Ciphertext;
import revoabe.PrivateKey;
import revoabe.PublicKey;
import revoabe.ReVo_ABE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import db.DBConnection;
import db.DBConnectionFactory;


public class TestSerilizationAndEncrypt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Pairing pairing = PairingFactory.getPairing("./src/main/java/a.properties");
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		Date localTime = new Date();
				
		Timestamp startTime = new Timestamp(localTime.getTime());
		Timestamp endTime = new Timestamp(localTime.getTime()+10*24*3600*1000);
		System.out.println(startTime);
		System.out.println(endTime);
		
		DBConnection conn = DBConnectionFactory.getConnection();
		int missionCount = conn.getAllMissions().size();
		mission curMission = new mission("test serialization mission"+missionCount, 10,startTime, endTime);
		
		conn.insertMission(curMission);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mission serilizedMission = conn.searchMission(curMission.getMissionName());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] needToEncrypt = "It supposed to be secure".getBytes();
		List<Integer> RL = new ArrayList<Integer>();
		RL.add(7);
		PublicKey serilizedPK = serilizedMission.getPublicKey();
		Ciphertext ct = ReVo_ABE.encrypt(pairing, serilizedPK, needToEncrypt, "(a and b) or (b and c)", RL);
		
		List<String> attr_list= new ArrayList<String>();
		attr_list.add("a");
		attr_list.add("B");
		attr_list.add("C");
		attr_list.add("d");
		attr_list.add("e");
		int user_id = 3;
		PrivateKey prik = ReVo_ABE.keyGen(serilizedPK, serilizedMission.getMasterKey(), attr_list, user_id);
		
		String decoded = new String(ReVo_ABE.decrypt(pairing, serilizedPK, ct, prik));
		System.out.println("Try to decode with private key generated from serilized public and master keys");
		System.out.println(decoded);
		int userCount = conn.getAllUsers().size();
		String username = "d"+userCount;
		String password = "p"+userCount;
		String firstname = "first_d"+userCount;
		String lastname = "last_d"+userCount;
		List<String> attributes =Arrays.asList("a, b, c, d, e".split(","));
		List<String> interests =Arrays.asList("f, g, h, i, j".split(","));
		user us = new user(username, password, firstname, lastname, attributes, interests);
		conn.insertUser(us);
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		user serilizedUser = conn.searchUser(username);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int userCountForMission = conn.getUsersForMission(serilizedMission.getMissionName()).size();

		Match curMatch = new Match(serilizedMission,serilizedUser,userCountForMission+1,true);
		conn.insertMatch(curMatch);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Match serilizedMatch = conn.searchMatch(curMission.getMissionName(), us.getUsername());
		PublicKey ssPK = serilizedMatch.getMission().getPublicKey();
		PrivateKey serilizedPrivateKey = serilizedMatch.getPrivateKey();
		byte[] res = ReVo_ABE.decrypt(pairing, ssPK, ct,serilizedPrivateKey);
		decoded = new String(res);
		System.out.println("Try to decode with Serilized private key and serilized public key");
		System.out.println(decoded);
		
		byte[] publicKey = serilizedMatch.getMission().toPublicKeyByteArray();
		byte[] privateKey = serilizedMatch.toPrivateKeyByteArray();
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		try {
			bs.write(EntityHelper.int_to_bytes(publicKey.length));
			bs.write(publicKey);
			bs.write(EntityHelper.int_to_bytes(privateKey.length));
			bs.write(privateKey);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] sres = bs.toByteArray();
		PublicKey pkFromBytes = new PublicKey(publicKey, pairing);
		PrivateKey prikFromBytes = new PrivateKey(privateKey,pairing);
		byte[] res2 = ReVo_ABE.decrypt(pairing, pkFromBytes, ct,prikFromBytes);
		decoded = new String(res2);
		System.out.println("Try to decode with byte private key and byte public key");
		System.out.println(decoded);
		
		byte[] ctBytes = ct.toByteArray();
		Ciphertext ctFromBytes = new Ciphertext(ctBytes,pairing);
		
		if(ct.compareCiphertext(ctFromBytes)) {
			System.out.println("serilized ciphertext are the same");
		}
		else {
			System.out.println("They are different");
		}
		
		
		byte[] res3 = ReVo_ABE.decrypt(pairing, pkFromBytes, ctFromBytes,prikFromBytes);
		decoded = new String(res3);
		System.out.println("Try to decode byte ciphertext with byte private key and byte public key");
		System.out.println(decoded);
		
		
	}

}
