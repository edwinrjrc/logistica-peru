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
public interface ProveedorDao {

	void registroProveedor(Proveedor proveedor, Connection conexion)
			throws SQLException;
	
	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;
	
	Proveedor consultarProveedor(int idProveedor) throws SQLException;
	
	Contacto consultarContacto(int idPersona) throws SQLException;

	void actualizarProveedor(Proveedor proveedor, Connection conexion) throws SQLException;

	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;
}
