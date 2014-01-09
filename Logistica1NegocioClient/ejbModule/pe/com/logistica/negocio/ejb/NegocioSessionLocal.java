package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;

import javax.ejb.Local;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;

@Local
public interface NegocioSessionLocal {

	public Direccion agregarDireccion(Direccion direccion)  throws SQLException, Exception;

	Contacto agregarContacto(Contacto contacto) throws SQLException, Exception;

}
