/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author edwreb
 *
 */
public interface ServicioNovaViajesDao {

	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia) throws SQLException;
}
