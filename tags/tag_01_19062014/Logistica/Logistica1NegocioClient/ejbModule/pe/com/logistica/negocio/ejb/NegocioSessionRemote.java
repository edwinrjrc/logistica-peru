package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

@Remote
public interface NegocioSessionRemote {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;

	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception;

	boolean actualizarProveedor(Proveedor proveedor) throws SQLException,
			ResultadoCeroDaoException, Exception;
	
	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;

	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws SQLException, ResultadoCeroDaoException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;

	public Cliente consultarCliente(int idcliente) throws SQLException, Exception;

	List<Cliente> listarCliente() throws SQLException;
}
