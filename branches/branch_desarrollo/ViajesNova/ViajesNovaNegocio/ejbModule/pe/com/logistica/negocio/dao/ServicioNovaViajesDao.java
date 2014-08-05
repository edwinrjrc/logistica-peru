/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.CuotaPago;
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

	List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException;

	Integer ingresarCabeceraServicio2(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;
	
	public ServicioAgencia consultarServiciosVenta2(int idServicio)
			throws SQLException;
	
	public List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio) throws SQLException;

	List<ServicioAgencia> consultarServiciosVenta(
			ServicioAgencia servicioAgencia) throws SQLException;

	List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio,
			Connection conn) throws SQLException;

	ServicioAgencia consultarServiciosVenta2(int idServicio, Connection conn)
			throws SQLException;
	
}
