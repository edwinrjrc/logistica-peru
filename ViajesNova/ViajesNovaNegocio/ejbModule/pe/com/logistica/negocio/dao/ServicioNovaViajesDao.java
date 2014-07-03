/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.CronogramaPago;
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

	boolean ingresarDetalleServicio(DetalleServicioAgencia detalleServicio,
			int idServicio) throws SQLException;

	Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	boolean ingresarDetalleServicio(DetalleServicioAgencia detalleServicio,
			int idServicio, Connection conn) throws SQLException;

	boolean generarCronogramaPago(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	List<CronogramaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException;
}