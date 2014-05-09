/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Proveedor;

/**
 * @author Edwin
 * 
 */
public interface NegocioServicio {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	public boolean registrarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public Proveedor consultarProveedorCompleto(int codigoProveedor)
			throws SQLException, Exception;

	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException, Exception;
}
