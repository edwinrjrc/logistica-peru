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
		String c = "C:\\aplicaciones\\aplicacionConfiguracion.properties";
		String d = "D:\\aplicaciones\\aplicacionConfiguracion.properties";
		File fc = new File(c);
		
		Properties prop = new Properties();
		InputStream input = null;
		String jndiProperties = "";
		
		try {
			if (fc.exists()){
				input = new FileInputStream(fc);
				prop.load(input);
			}
			else{
				fc = new File(d);
				if (fc.exists()){
					input = new FileInputStream(fc);
					prop.load(input);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		jndiProperties = prop.getProperty("jndi_ds");
		
		return jndiProperties;
	}

}
