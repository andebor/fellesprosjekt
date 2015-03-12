package server;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Security {
	
	public static byte[] stringtoByte(String input) {
    	Decoder decoder = Base64.getDecoder();
    	return decoder.decode(input);
    }
	
    public static String bytetoString(byte[] input) {
    	Encoder  encoder = Base64.getEncoder();
    	return encoder.encodeToString(input);
    }
	
    public static byte[] generateSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        return salt;
    }
	
	
    public static byte[] hashPassword(char[] password, byte[] salt) throws GeneralSecurityException {
    	int ITERATION_COUNT = 100;
    	int KEY_SIZE = 160;
        return hashPassword(password, salt, ITERATION_COUNT, KEY_SIZE);
     }

     public static byte[] hashPassword(char[] password, byte[] salt, int iterationCount, int keySize) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
     }

     public static boolean matches(byte[] dbPwd, byte[] typedPwd) throws GeneralSecurityException {
    	 return Arrays.equals(dbPwd, typedPwd);
     }
}
