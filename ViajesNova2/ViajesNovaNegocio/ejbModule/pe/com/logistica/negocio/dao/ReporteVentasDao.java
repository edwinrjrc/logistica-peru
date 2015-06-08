/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;

/**
 * @author Edwin
 *
 */
public interface ReporteVentasDao {

	public List<DetalleServicioAgencia> reporteGeneralVentas(Date desde, Date hasta) throws SQLException;
}
