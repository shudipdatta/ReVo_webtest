package test_revo_abe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import revoabe.MembershipTree;
import revoabe.ReVo_ABE;

public class Test_setup {
	
	
	public static void main(String[] args) {
		Pairing pairing = PairingFactory.getPairing("./src/main/java/a.properties");
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		int nodecount = 10;
		ReVo_ABE testABE = new ReVo_ABE(pairing, nodecount);
		//test membership tree
		MembershipTree currTree = testABE.getPublicKey().getMembershipTree();
//		Element g = pairing.getG1().newRandomElement().getImmutable();
//		Element a1 = g.powZn(pairing.getZr().newRandomElement());
//		Element a2 = g.powZn(pairing.getZr().newRandomElement());
//		Element a3 = g.powZn(pairing.getZr().newRandomElement());
//		Element a4 = g.powZn(pairing.getZr().newRandomElement());
//		Element a5 = g.powZn(pairing.getZr().newRandomElement());
//		Element a6 = g.powZn(pairing.getZr().newRandomElement());
//		System.out.println(a1);
//		System.out.println(a2);
//		System.out.println(a3);
//		System.out.println(a4);
//		System.out.println(a5);
//		System.out.println(a6);
//		System.out.println(a1);
//		System.out.println(g.powZn(pairing.getZr().newRandomElement()));
//		System.out.println(g.powZn(pairing.getZr().newRandomElement()));
//		System.out.println(g.powZn(pairing.getZr().newRandomElement()));
		currTree.printAllNodesBFS();
		//currTree.testRandom(currTree.getRoot());
		//currTree.testLeaf();
	}
	
}
