/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.exception.ConnectionException;

/**
 * @author EDWREB
 *
 */
public interface ReportesServicio {

	public List<DetalleServicioAgencia> reporteGeneralVentas(ReporteVentas reporteVentas) throws SQLException;

	boolean registrarArchivoReporte(ReporteArchivo reporteArchivo)
			throws ConnectionException, SQLException;
	
}
