package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

@Local
public interface NegocioSessionLocal {

	public Direccion agregarDireccion(Direccion direccion)  throws SQLException, Exception;

	Contacto agregarContacto(Contacto contacto) throws SQLException, Exception;

	boolean registrarProveedor(Proveedor proveedor) throws ResultadoCeroDaoException, SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;
}
