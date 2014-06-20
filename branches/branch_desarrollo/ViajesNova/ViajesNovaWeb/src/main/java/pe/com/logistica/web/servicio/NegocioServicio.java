/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

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

	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;
	
	List<Cliente> listarCliente() throws SQLException;

	public Cliente consultarClienteCompleto(int idcliente) throws SQLException, Exception;
	
	public String registrarNovios(ProgramaNovios programaNovios) throws SQLException, Exception; 
}
