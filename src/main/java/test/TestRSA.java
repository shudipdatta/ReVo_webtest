package test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Random;

import it.unisa.dia.gas.plaf.jpbc.util.Arrays;

public class TestRSA {
	 public static BigInteger lcm(BigInteger s, BigInteger s1)
	    {
	        // convert string 'a' and 'b' into BigInteger

	  
	        // calculate multiplication of two bigintegers
	        BigInteger mul = s.multiply(s1);
	  
	        // calculate gcd of two bigintegers
	        BigInteger gcd = s.gcd(s1);
	  
	        // calculate lcm using formula: lcm * gcd = x * y
	        BigInteger lcm = mul.divide(gcd);
	        return lcm;
	    }
	
	 public static long[] getSeeds(String password) {
		 long ret[] = new long[2];
		 MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("SHA3-256");
				byte[] encodedhash = digest.digest(
					password.getBytes(StandardCharsets.UTF_8));
				byte[] b1 = new byte[16];
				byte[] b2 = new byte[16];
				//System.out.println(encodedhash.length);
				b1 = Arrays.copyOfRange(encodedhash, 0, 15);
				b2 = Arrays.copyOfRange(encodedhash, 16, 31);
				BigInteger modulus = new BigInteger("2147483647");
				BigInteger seedp = new BigInteger(b1).mod(modulus);
				BigInteger seedq = new BigInteger(b2).mod(modulus);
				ret[0] = seedp.longValue();
				ret[1] = seedq.longValue();
				return ret;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
	 }
		
		
	public static void main(String[] args) throws NoSuchProviderException {
		// TODO Auto-generated method stub
		
		String password = "It is a dummy user password";
		
		long seeds[] = getSeeds(password);
		
		Random rp = new Random(seeds[0]);
		Random rq = new Random(seeds[1]);
		BigInteger e = BigInteger.valueOf(65537);
		
		BigInteger p = BigInteger.probablePrime(1024, rp);
		BigInteger q = BigInteger.probablePrime(1024, rq);
		BigInteger n = p.multiply(q);
		BigInteger lambdaN = lcm(p.subtract(BigInteger.ONE),q.subtract(BigInteger.ONE));
		BigInteger d =e.modInverse(lambdaN);
		System.out.println(p);
		String message = "I'm boy";
		BigInteger m = new BigInteger(message.getBytes());
		System.out.println(m);
		
		BigInteger cipherText = m.modPow(e, n);
		BigInteger decrypted = cipherText.modPow(d, n);
		
		System.out.println(new String(decrypted.toByteArray(),StandardCharsets.UTF_8));
		
		
		
	}

}
