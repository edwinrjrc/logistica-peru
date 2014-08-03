/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Edwin
 *
 */
public class UtilEjb {

	/**
	 * 
	 */
	public UtilEjb() {
		// TODO Auto-generated constructor stub
	}

	
	public static String obtenerCadenaPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return resourceMaestros.getString(llave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int obtenerEnteroPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return convertirCadenaEntero(resourceMaestros.getString(llave));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaBlanco(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return StringUtils.trimToEmpty(cadena);
		}
		return "";
	}
	
	public static int convertirCadenaEntero(String cadena){
		try {
			if (StringUtils.isNotBlank(cadena)){
				return Integer.parseInt(cadena);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
