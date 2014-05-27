/**
 * 
 */
package pe.com.logistica.bean.Util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Edwin
 *
 */
public class UtilParse {

	public static String parseCadena(String cadena){
		if (StringUtils.isBlank(cadena)){
			return "";
		}
		return cadena;
	}

}
