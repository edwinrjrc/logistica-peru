/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Edwin
 * 
 */
public class UtilJdbc {

	public UtilJdbc() {
	}

	public static String obtenerCadena(ResultSet rs, String campo) throws SQLException {
		if (rs != null && StringUtils.isNotBlank(campo)) {
			return parseaCadena(rs.getString(campo));
		}
		return "";
	}
	
	public static int obtenerNumero(ResultSet rs, String campo) throws SQLException {
		if (rs != null && StringUtils.isNotBlank(campo)) {
			return rs.getInt(campo);
		}
		return 0;
	}

	public static String parseaCadena(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return cadena;
		}
		return "";
	}
	
	public static Integer parseCadenaEntero(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return Integer.valueOf(cadena);
		}
		return Integer.valueOf(0);
	}
}
