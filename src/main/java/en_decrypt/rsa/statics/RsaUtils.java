package en_decrypt.rsa.statics;

import java.io.FileInputStream;
import java.io.IOException;
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

import javax.crypto.Cipher;

import org.apache.commons.lang3.StringUtils;

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
	 * @param plainText 待签名字符串
	 * @param keyStorePath 签名私钥路径
	 * @param password 签名私钥密码
	 * @return 返回签名后的字符串
	 * @throws Exception
	 */
	public static String sign(String plainText, String keyStorePath, String keyAlais, String keyStorePassword, String keyPassword) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance("MD5");

		md5.update(plainText.getBytes("utf-8"));

		byte[] digestBytes = md5.digest();
		
		System.out.println( "origin: " + encoder.encode( digestBytes ) );

		/*
		 * 用私钥进行签名 RSA Cipher 负责完成加密或解密工作，基于 RSA
		 */
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		// ENCRYPT_MODE表示为加密模式
		cipher.init( Cipher.ENCRYPT_MODE, getPrivateKey( keyStorePath, keyAlais, keyStorePassword, keyPassword ) );

		// 加密
		byte[] rsaBytes = cipher.doFinal( digestBytes );

		// Base64 编码
		return encoder.encode(rsaBytes);

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
//		BASE64Encoder bse = new BASE64Encoder();
//
//		return bse.encode(pk.getEncoded());

	}

}
