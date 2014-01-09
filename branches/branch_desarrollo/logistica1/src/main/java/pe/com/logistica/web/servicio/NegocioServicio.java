/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;

/**
 * @author Edwin
 * 
 */
public interface NegocioServicio {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException, Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException, Exception;
}
