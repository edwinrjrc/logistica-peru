/**
 * 
 */
package pe.com.logistica.negocio.util;


import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Edwin
 *
 */
public class UtilConexion {

	private final static String JNDI = "java:/jboss/jdbc/logisticaDS";
	/**
	 * 
	 */
	public UtilConexion() {
		// TODO Auto-generated constructor stub
	}
	
	public static Connection obtenerConexion(){
		
		try {
			Context ic = new InitialContext();
			DataSource dataSource = (DataSource) ic.lookup(JNDI);
			
			return dataSource.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static Connection obtenerConexion(String jndi){
		try {
			
			jndi = (StringUtils.isBlank(jndi)?JNDI:jndi);
			
			Context ic = new InitialContext();
			DataSource dataSource = (DataSource) ic.lookup(jndi);
			
			return dataSource.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
