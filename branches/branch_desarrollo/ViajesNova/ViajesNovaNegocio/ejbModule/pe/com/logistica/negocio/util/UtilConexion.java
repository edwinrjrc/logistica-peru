/**
 * 
 */
package pe.com.logistica.negocio.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilProperties;

/**
 * @author Edwin
 *
 */
public class UtilConexion {

	private static String JNDI = "java:/jboss/jdbc/novaviajesDS";
	
	public static Connection obtenerConexion(){
		
		try {
			Context ic = new InitialContext();
			DataSource dataSource = null;
			
			String jndiProperties = getJndiProperties();
			if (StringUtils.isNotBlank(jndiProperties)){
				dataSource = (DataSource) ic.lookup(jndiProperties);
			}
			else{
				dataSource = (DataSource) ic.lookup(JNDI);
			}
			
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


	/**
	 * @return the jndiProperties
	 */
	public static String getJndiProperties() {
		Properties prop = UtilProperties.cargaArchivo("aplicacionConfiguracion.properties");
		
		String jndiProperties = prop.getProperty("jndi_ds");
		
		return jndiProperties;
	}

}
