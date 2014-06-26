/**
 * 
 */
package pe.com.logistica.negocio.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author edwreb
 *
 */
public class UtilEncripta {

	/**
	 * 
	 */
	public UtilEncripta() {
		// TODO Auto-generated constructor stub
	}

	public static String encriptaCadena (String cadena){
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		String encrip = s.encrypt(cadena);
		return encrip;
	}
	
	public static String desencriptaCadena (String cadena){
		StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
		s.setPassword("uniquekey");
		String encrip = s.decrypt(cadena);
		return encrip;
	}
}
