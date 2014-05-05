/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Proveedor;

/**
 * @author Edwin
 * 
 */
public interface PersonaDao {

	public List<Proveedor> listarPersonas() throws SQLException;

	int registrarPersona(Persona persona, Connection conexion)
			throws SQLException;

	int actualizarPersona(Persona persona, Connection conexion)
			throws SQLException;
}
