/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author edwreb
 *
 */
public interface ServicioNovaViajesDao {

	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia) throws SQLException;

	Integer ingresarCabeceraServicio2(ServicioAgencia servicioAgencia)
			throws SQLException;

	Integer ingresarDetalleServicio(DetalleServicioAgencia detalleServicio,
			int idServicio) throws SQLException;
}
