package test_revo_abe;

import java.util.Random;

import entity.mission;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import revoabe.MembershipTree;

public class TestRandomMembershipTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random rd = new Random(54321);
		byte[] nb = new byte[4];
		rd.nextBytes(nb);
		
		for (byte b:nb) {
			
			System.out.print(String.format("%x, ", b));
		}
		
		
		rd.nextBytes(nb);
		
		for (byte b:nb) {
			
			System.out.println(String.format("%x, ", b));
		}
		
		rd = new Random(54321);
		rd.nextBytes(nb);
		
		for (byte b:nb) {
			
			System.out.print(String.format("%x, ", b));
		}
		
		
		rd.nextBytes(nb);
		
		for (byte b:nb) {
			
			System.out.println(String.format("%x, ", b));
		}
		
		
		Pairing pairing = mission.getPairing();
		Element g1 = pairing.getG1().newRandomElement();
		MembershipTree mt = new MembershipTree(10,pairing.getG1().newElementFromBytes(g1.toBytes()).getImmutable(),pairing,12345);
		//mt.printAllNodesBFS();
		MembershipTree mt2 = new MembershipTree(10,pairing.getG1().newElementFromBytes(g1.toBytes()).getImmutable(),pairing,12345);
		//mt2.printAllNodesBFS();
		
		
	}

}
