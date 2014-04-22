/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Proveedor;

/**
 * @author Edwin
 *
 */
public interface ProveedorDao {

	void registroProveedor(Proveedor proveedor, Connection conexion)
			throws SQLException;
	
	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;
}