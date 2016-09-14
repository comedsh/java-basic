package en_decrypt.rsa.dynamic;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

public class RsaMain {

	public static void main(String[] args) throws Exception {
		
		HashMap<String, Object> map = RsaUtils.getKeys();
		
		//生成公钥和私钥
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
		
		//模
		String modulus = publicKey.getModulus().toString();
		
		//公钥指数
		String public_exponent = publicKey.getPublicExponent().toString();
		
		//私钥指数
		String private_exponent = privateKey.getPrivateExponent().toString();
		
		//明文
		String ming = "123456789";
		
		//使用模和指数生成公钥和私钥
		RSAPublicKey pubKey = RsaUtils.getPublicKey(modulus, public_exponent);
		
		RSAPrivateKey priKey = RsaUtils.getPrivateKey(modulus, private_exponent);
		
		//加密后的密文
		//String mi = RsaUtils.encryptByPublicKey(ming, publicKey); -> 这种方式也是可以的
		String mi = RsaUtils.encryptByPublicKey(ming, pubKey);
		
		System.err.println(mi);
		
		//解密后的明文
		//ming = RsaUtils.decryptByPrivateKey(mi, privateKey); -> 这种方式也是可以的
		ming = RsaUtils.decryptByPrivateKey(mi, priKey);
		
		System.err.println(ming);
		
	}
	
}
