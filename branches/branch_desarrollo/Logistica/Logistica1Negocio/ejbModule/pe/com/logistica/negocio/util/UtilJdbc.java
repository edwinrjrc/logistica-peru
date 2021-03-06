/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
	
	public static Date obtenerFecha(ResultSet rs, String campo) throws SQLException {
		if (rs != null && StringUtils.isNotBlank(campo)) {
			return rs.getDate(campo);
		}
		return null;
	}
	
	public static boolean convertirBooleanSiNo(ResultSet rs, String campo) throws SQLException{
		String dato = obtenerCadena(rs, campo);
		return ("S".equals(dato));
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
	
	public static int obtenerNumero(Integer numero){
		if (numero == null){
			return 0;
		}
		return numero.intValue();
	}
	
	public static String convertirMayuscula(String cadena){
		return StringUtils.upperCase(parseaCadena(cadena));
	}
	
	public static java.sql.Date convertirUtilDateSQLDate(java.util.Date fecha){
		return new java.sql.Date(fecha.getTime());
	}
}
