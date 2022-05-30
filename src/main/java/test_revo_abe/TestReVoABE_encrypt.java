package test_revo_abe;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import revoabe.Ciphertext;
import revoabe.MasterKey;
import revoabe.PrivateKey;
import revoabe.PublicKey;
import revoabe.ReVo_ABE;

public class TestReVoABE_encrypt {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// Initialize pairing mode
		Pairing pairing = PairingFactory.getPairing("./src/main/java/a.properties");
		PairingFactory.getInstance().setUsePBCWhenPossible(true);
		// Configure a mission
		int nodecount = 10;
		long missionCode = 12345;
		// Create a ReVo_ABE instance
		ReVo_ABE testABE = new ReVo_ABE(pairing, nodecount,missionCode);
		// Create an attribute list for a user
		List<String> attr_list= new ArrayList<String>();
		attr_list.add("a");
		attr_list.add("B");
		attr_list.add("C");
		attr_list.add("d");
		// Generate the private key for a user
		// Setup private key and public key for the user
		int user_id6 = 6;
		PrivateKey prik6 = testABE.keyGen(attr_list, user_id6);
		MasterKey mk = testABE.getMasterKey();
		
		PublicKey pk = testABE.getPublicKey();
		List<Integer> RL = new ArrayList<Integer>();

		// message to be encrypted
		String msg = "This suppose to be secret,";
		// encrypt the message
		Ciphertext ctxt = testABE.encrypt(pk, msg.getBytes(), "a", RL);
		// decrypt the message
		System.out.println(new String(testABE.decrypt(pk, ctxt, prik6)));
	}
	
	
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(n);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	}

}
