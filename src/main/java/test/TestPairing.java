package test;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import revoabe.ReVo_ABE;

public class TestPairing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String curvedir = ReVo_ABE.hardcodedCurveFileDir("a.properties");
		Pairing group = PairingFactory.getPairing(curvedir);
		Element g1 = group.getG1().newRandomElement();
		Element g2 = group.getG2().newRandomElement();
		Element egg = group.pairing(g1, g2);
		
		System.out.println(egg);
		Element eg = group.getGT().newElementFromBytes(egg.toBytes());
		System.out.println(eg);
		if(eg.equals(egg)) {
			System.out.print("they are the same");
		};
	}

}
