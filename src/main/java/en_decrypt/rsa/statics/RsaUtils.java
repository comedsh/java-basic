package en_decrypt.rsa.statics;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;

import en_decrypt.rsa.dynamic.RsaMain;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * Base 64 是为了将特殊字符进行重新编码，便于在网络上传输。
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: Sep 14, 2016 10:56:45 AM
 *
 */
public class RsaUtils {
	
	static BASE64Encoder encoder = new BASE64Encoder();
	
	static BASE64Decoder decoder = new BASE64Decoder();
	
	/**
	 * 私钥签名：签名方法如下：BASE64(RSA(MD5(src),privatekey))，其中 src 为需要签名的字符串，private key 是商户的CFCA证书私钥。
	 * 
	 * 签名方使用自己的私钥，将文件进行签名，然后将原文(明文)、公钥、签名和哈希值发送给接收方；
	 * 
	 * @param plainText 待签名字符串
	 * @param keyStorePath 签名私钥路径
	 * @param password 签名私钥密码
	 * @return 返回被 Base64 转码后的签名
	 * @throws Exception
	 */
	public static String sign(String plainText, String keyStorePath, String keyAlais, String keyStorePassword, String keyPassword) throws Exception {

		byte[] hashcode = hash( plainText );

		/*
		 * 用私钥进行签名 RSA Cipher 负责完成加密或解密工作，基于 RSA
		 */
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// ENCRYPT_MODE表示为加密模式
		cipher.init( Cipher.ENCRYPT_MODE, getPrivateKey( keyStorePath, keyAlais, keyStorePassword, keyPassword ) );

		// 加密
		byte[] encrypted = cipher.doFinal( hashcode );

		// Base64 编码
		return encoder.encode(encrypted);

	}
	
	/**
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: Sep 16, 2016 10:16:55 AM
	 * 
	 * @return the based64 md5 hash code 
	 *
	 */
	public static byte[] hash( String plainText ) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");

		md5.update(plainText.getBytes("utf-8"));

		return md5.digest();
		
	}
	
	
	/**
	 * 
	 * 接收方将得到的签名进行解密(D)，得到对应的 hash 值
	 * 
	 * @param signature -> base 64 encrypted signature
	 * @param publicKeyPath -> the path of the public key stored 
	 * 
	 * @return base 64 hash 
	 * 
	 * @author shang yang
	 * @version
	 * @createTime: Sep 16, 2016 10:41:15 AM
	 *
	 */
	public static String deSign( String signature, String publicKeyPath ) throws CertificateException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		
		PublicKey publicKey = getPublicKey( publicKeyPath );
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		cipher.init( Cipher.DECRYPT_MODE, publicKey );  
		
		// get the md5 hash value
		byte[] hash = cipher.doFinal( decoder.decodeBuffer( signature ) );
		
		return encoder.encode( hash );
		
	}
	
	
	/**
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: Sep 16, 2016 10:25:08 AM
	 * 
	 * @return the base64 m5 hash code
	 *
	 */
	public static String hashToBase64( String plainText ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		return encoder.encode( hash( plainText ) );
		
	}
	
	
	
	/**
	 * 读取私钥 返回PrivateKey
	 * 
	 * @param path 包含私钥的证书路径
	 * @param password 私钥证书密码
	 * @return 返回私钥PrivateKey
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	public static PrivateKey getPrivateKey(String path, String keyAlias, String keyStorePassword, String keyPassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {

		// 需要根据 KeyStore 的类型来确定 getInstance 的参数 JKS|PKCS12，我本地生成的测试所用的 keystore 是 JKS
		// keytool -list -keystore mykeystore
		// KeyStore ks = KeyStore.getInstance("PKCS12");

		KeyStore ks = KeyStore.getInstance("JKS");

		FileInputStream fis = new FileInputStream(path);

		ks.load( fis, StringUtils.isEmpty( keyStorePassword ) ? null : keyStorePassword.toCharArray() );

		fis.close();

		return (PrivateKey) ks.getKey( keyAlias, StringUtils.isEmpty( keyPassword ) ? null : keyPassword.toCharArray());
	}

	/**
	 * 读取公钥 .cer
	 * 
	 * @param path .cer文件的路径
	 * @return base64后的公钥串
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static PublicKey getPublicKey(String path) throws IOException, CertificateException {

		CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");

		FileInputStream bais = new FileInputStream(path);

		X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(bais);

		bais.close();

		PublicKey pk = Cert.getPublicKey();

		return pk;

	}

	/**  
     * 根据公钥 n、e 生成公钥，如何生成 n、e 串参考 {@link RsaMain#main(String[])}
     *  
     * @param modulus 公钥 n 串
     * @param publicExponent  公钥 e 串
     * @return 返回公钥PublicKey 
     * @throws Exception 
     */  
    public static PublicKey getPublickKey(String modulus, String publicExponent) throws Exception {  
    	
        KeySpec publicKeySpec = new RSAPublicKeySpec( new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        
        KeyFactory factory = KeyFactory.getInstance("RSA");
        
        PublicKey publicKey = factory.generatePublic(publicKeySpec);
        
        return publicKey;  
    }  	
	
}
