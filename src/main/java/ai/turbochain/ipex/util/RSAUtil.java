package ai.turbochain.ipex.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class RSAUtil {
	public static byte[] encrypt(byte[] data,String certsPath) throws Exception {
		// 用证书的公钥加密
		CertificateFactory cff = CertificateFactory.getInstance("X.509");
		FileInputStream fis1 = new FileInputStream(certsPath); // 证书文件
		Certificate cf = cff.generateCertificate(fis1);
		PublicKey pk1 = cf.getPublicKey(); // 得到证书文件携带的公钥
		Cipher c1 = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // 定义算法：RSA
		c1.init(Cipher.ENCRYPT_MODE, pk1);

		return c1.doFinal(data); // 加密后的数据
	}

	public static String decrypt(byte[] data,String keystorePath) throws Exception {

		// 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
		FileInputStream fis2 = new FileInputStream(keystorePath);
		KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
		char[] kspwd = "123456".toCharArray(); // 证书库密码
		char[] keypwd = "123456".toCharArray(); // 证书密码
		ks.load(fis2, kspwd); // 加载证书
		PrivateKey pk2 = (PrivateKey) ks.getKey("tomcat", keypwd); // 获取证书私钥
		fis2.close();
		Cipher c2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		c2.init(Cipher.DECRYPT_MODE, pk2);

		return new String(c2.doFinal(data), "UTF8"); // 解密后的数据
	}

	public static String keySort(final Map<String, Object> map) {
		Set<String> keySet = map.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();
		for (String k : keyArray) {
			sb.append(k).append("=").append(map.get(k)).append("&");
		}

		return sb.toString();
	}
	
	
	  /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static byte[] generateSignature(final Map<String, Object> map,final String certsPath) throws Exception {
        
    	String str = keySort(map);
        
    	return encrypt(str.getBytes("utf-8"),certsPath);
    }
}
