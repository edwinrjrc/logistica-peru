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

	public int registrarTelefono(Telefono telefono, Connection conexion)
			throws SQLException;

	void registrarTelefonoDireccion(int idTelefono, int idDireccion,
			Connection conexion) throws SQLException;

	void registrarTelefonoPersona(int idTelefono, int idPersona,
			Connection conexion) throws SQLException;
}
