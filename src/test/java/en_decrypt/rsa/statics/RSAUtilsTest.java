package en_decrypt.rsa.statics;

import static org.junit.Assert.assertEquals;

import java.security.PrivateKey;

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
		
		String signature = RsaUtils.sign( PLAIN_TXT, KEYSTORE_PATH, KEY_ALIAS, KEYSTORE_PASSWORD, KEY_PASSWORD );
		
		System.out.println( signature );
		
	}
	
	/**
	 * 模拟发送发签名，接收方验证签名的过程。
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testVerifySignature() throws Exception{
		
		String hash1 = RsaUtils.hashToBase64( PLAIN_TXT );
		
		String signature = RsaUtils.sign( PLAIN_TXT, KEYSTORE_PATH, KEY_ALIAS, KEYSTORE_PASSWORD, KEY_PASSWORD );
		
		String hash2 = RsaUtils.deSign( signature, PUBLIC_KEY_PATH );
		
		assertEquals( hash1, hash2 );
		
		
	}
	
	

}
