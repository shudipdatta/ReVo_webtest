package test;

import java.nio.charset.StandardCharsets;

import rsa.RSAEncryption;

public class TestRSA2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RSAEncryption rsa = new RSAEncryption("This the the dummy password user types in");
		RSAEncryption rsa2 = new RSAEncryption("This the the second dummy password user types in");
		String message = "This is a dummy password the ReVo-ABE generates";
		//System.out.println(rsa.encrypt(message));
		System.out.println(rsa.decryptString(rsa.encryptString(message)));
		System.out.println(rsa2.decryptString(rsa.encryptString(message)));
		System.out.println(new String(rsa.decrypt(rsa.encrypt(message.getBytes())),StandardCharsets.UTF_8));
		
	}

}
