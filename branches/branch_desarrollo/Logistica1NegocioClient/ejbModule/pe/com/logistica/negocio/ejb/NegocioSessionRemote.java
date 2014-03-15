package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Proveedor;

@Remote
public interface NegocioSessionRemote {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException, Exception;
	
	public Contacto agregarContacto(Contacto contacto) throws SQLException, Exception;
	
	void registrarProveedor(Proveedor proveedor) throws SQLException, Exception;
}
