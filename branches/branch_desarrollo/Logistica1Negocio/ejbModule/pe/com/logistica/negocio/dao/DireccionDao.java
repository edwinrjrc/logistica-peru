/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;

import pe.com.logistica.bean.base.Direccion;

/**
 * @author Edwin
 *
 */
public interface DireccionDao {

	public int registrarDireccion(Direccion direccion, Connection conexion) throws SQLException;

	void registrarPersonaDireccion(int idPersona, int idTipoPersona,
			int idDireccion, Connection conexion) throws SQLException;
}
