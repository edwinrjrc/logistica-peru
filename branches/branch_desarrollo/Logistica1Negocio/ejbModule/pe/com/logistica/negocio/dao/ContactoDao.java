/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Contacto;

/**
 * @author Edwin
 *
 */
public interface ContactoDao {

	void registrarContactoProveedor(int idproveedor, Contacto contacto,
			Connection conexion) throws SQLException;

	List<Contacto> consultarContactoOProveedor(int idproveedor)
			throws SQLException;
}
