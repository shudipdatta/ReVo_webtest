package test;

import javax.crypto.spec.SecretKeySpec;

import revoabe.AES;

public class TestAES {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String seed = "I love my wifeddddddddddddddddddd";
		String data = "This suppose to be secret";
		SecretKeySpec k = AES.setKey(seed.getBytes());
		byte[] ciphertext = AES.encrypt(data.getBytes(), seed.getBytes());
		//System.out.println(new String(ciphertext));
		byte[] decrypted = AES.decrypt(ciphertext, seed.getBytes());
		System.out.println(new String(decrypted));
	}

}
