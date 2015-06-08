package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.negocio.dao.ReporteVentasDao;
import pe.com.logistica.negocio.dao.impl.ReporteVentasDaoImpl;

/**
 * Session Bean implementation class Reportes
 */
@Stateless
public class Reportes implements ReportesRemote, ReportesLocal {

	@Override
	public List<DetalleServicioAgencia> reporteGeneralVentas(Date desde,
			Date hasta) throws SQLException {
		ReporteVentasDao  reporteVentasDao = new ReporteVentasDaoImpl();
		
		return reporteVentasDao.reporteGeneralVentas(desde, hasta)
				;
	}
}
