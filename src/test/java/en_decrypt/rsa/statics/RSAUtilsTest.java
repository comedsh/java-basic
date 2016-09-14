package en_decrypt.rsa.statics;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtilsTest {
	
	final static String PLAIN_TXT = "hello world";
	
	final static String CERT_ALIAS = "mytest";
	
	final static String KEYSTORE_PASSWORD = "comedsh006";
	
	final static String KEY_ALIAS = "mytest";
	
	final static String KEY_PASSWORD = "sysh0726"; // 证书的密码
	
	final static String KEYSTORE_PATH = "/Users/mac/workspace/mine/learning/basic/java_basic/src/test/java/en_decrypt/rsa/statics/key/mykeystore";
	
	final static String PUBLIC_KEY_PATH = "/Users/mac/workspace/mine/learning/basic/java_basic/src/test/java/en_decrypt/rsa/statics/key/mytest_pub.cer";
	
	BASE64Encoder encoder = new BASE64Encoder();	
	BASE64Decoder decoder = new BASE64Decoder();
	
	@Test
	public void testGetPrivateKey() throws Exception{

		PrivateKey privateKey = RsaUtils.getPrivateKey( KEYSTORE_PATH, KEY_ALIAS, KEYSTORE_PASSWORD, KEY_PASSWORD );
		
		System.out.println( privateKey );

	}
	
	@Test
	public void testGetPublicKey() throws Exception{
		
//		String key = RsaUtils.getPublicKey( PUBLIC_KEY_PATH );
//		
//		System.out.println(key);
		
	}
	
	@Test
	public void testSign() throws Exception{
		
		String signature = RsaUtils.sign(PLAIN_TXT, KEYSTORE_PATH, KEY_ALIAS, KEYSTORE_PASSWORD, KEY_PASSWORD);
		
		System.out.println( signature );
		
	}
	
	@Test
	public void testVerifySignature() throws Exception{
		
		PublicKey publicKey = RsaUtils.getPublicKey( PUBLIC_KEY_PATH );
	
		Cipher c4 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
		
		c4.init( Cipher.DECRYPT_MODE, publicKey );  
		
		String base64EncryptedSignature = RsaUtils.sign( PLAIN_TXT, KEYSTORE_PATH, KEY_ALIAS, KEYSTORE_PASSWORD, KEY_PASSWORD );
		
		byte[] encryptedSignature = decoder.decodeBuffer( base64EncryptedSignature );
		
		// decrypted signature
		byte[] signature = c4.doFinal( encryptedSignature );
		
		System.out.println("target: " +  encoder.encode( signature ) );
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		md5.update( PLAIN_TXT.getBytes("utf-8") );

		byte[] digestBytes = md5.digest();
		
		System.out.println( "origin: " + encoder.encode( digestBytes ) );		
		
	}
	
	

}
