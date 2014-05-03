/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;

/**
 * @author Edwin
 *
 */
public interface ContactoDao {

	void registrarContactoProveedor(int idproveedor, Contacto contacto,
			Connection conexion) throws SQLException;

	List<Contacto> consultarContactoOProveedor(int idproveedor)
			throws SQLException;

	boolean eliminarTelefonoContacto(Contacto contacto, Connection conexion)
			throws SQLException;

	boolean eliminarContacto(Contacto contacto, Connection conexion)
			throws SQLException;

	boolean eliminarContactoProveedor(Proveedor proveedor, Connection conexion)
			throws SQLException;
}
