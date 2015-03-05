/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;

/**
 * @author edwreb
 *
 */
public interface ServicioNovaViajesDao {
	
	Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia) throws SQLException;
	
	Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	boolean ingresarDetalleServicio(DetalleServicioAgencia detalleServicio,
			int idServicio) throws SQLException;

	Integer ingresarDetalleServicio(DetalleServicioAgencia detalleServicio,
			int idServicio, Connection conn) throws SQLException;

	boolean generarCronogramaPago(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException;

	public ServicioAgencia consultarServiciosVenta2(int idServicio)
			throws SQLException;
	
	public List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio) throws SQLException;

	List<ServicioAgencia> consultarServiciosVenta(
			ServicioAgencia servicioAgencia) throws SQLException;

	List<DetalleServicioAgencia> consultaServicioDetalle(int idServicio,
			Connection conn) throws SQLException;

	ServicioAgencia consultarServiciosVenta2(int idServicio, Connection conn)
			throws SQLException;

	Integer actualizarCabeceraServicio(ServicioAgencia servicioAgencia)
			throws SQLException;

	Integer eliminarDetalleServicio(ServicioAgencia servicioAgencia)
			throws SQLException;

	boolean eliminarDetalleServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	Integer actualizarCabeceraServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	boolean eliminarCronogramaServicio(ServicioAgencia servicioAgencia)
			throws SQLException;

	boolean eliminarCronogramaServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;

	List<DetalleServicioAgencia> consultaServicioDetalleHijos(int idServicio,
			int idSerPadre, Connection conn) throws SQLException;
	
	void registrarSaldosServicio(ServicioAgencia servicioAgencia,
			Connection conn) throws SQLException;
	
	void registrarPagoServicio(PagoServicio pago) throws SQLException;
	
	List<PagoServicio> listarPagosServicio(Integer idServicio) throws SQLException;
	
	BigDecimal consultarSaldoServicio(Integer idServicio) throws SQLException;

	void actualizarServicioVenta(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;
}
