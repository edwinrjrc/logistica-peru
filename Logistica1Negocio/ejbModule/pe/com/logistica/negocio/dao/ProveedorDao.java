/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Proveedor;

/**
 * @author Edwin
 * 
 */
public interface ProveedorDao {

	public List<Proveedor> listarProveedores() throws SQLException;

	public void registrarProveedor(Proveedor proveedor) throws SQLException;

	public void actualizarProveedor(Proveedor proveedor) throws SQLException;

	public Proveedor consultarProveedor(int id) throws SQLException;
}
