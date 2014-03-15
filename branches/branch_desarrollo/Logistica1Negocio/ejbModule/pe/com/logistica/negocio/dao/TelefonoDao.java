/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pe.com.logistica.bean.negocio.Telefono;

/**
 * @author Edwin
 *
 */
public interface TelefonoDao {

	public void registrarTelefono(Telefono telefono, Connection conexion)
			throws SQLException;
}
