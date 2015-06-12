package pe.com.logistica.negocio.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.cargaexcel.ColumnasExcel;
import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.dao.ArchivoReporteDao;
import pe.com.logistica.negocio.dao.ReporteVentasDao;
import pe.com.logistica.negocio.dao.impl.ArchivoReporteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ReporteVentasDaoImpl;
import pe.com.logistica.negocio.exception.ConnectionException;
import pe.com.logistica.negocio.util.UtilConexion;

/**
 * Session Bean implementation class Reportes
 */
@Stateless(name = "ReportesSession")
public class ReportesSession implements ReportesSessionRemote, ReportesSessionLocal {

	@Override
	public List<DetalleServicioAgencia> reporteGeneralVentas(ReporteVentas reporteVentas) throws SQLException {
		ReporteVentasDao reporteVentasDao = new ReporteVentasDaoImpl();

		return reporteVentasDao.reporteGeneralVentas(reporteVentas);
	}
	
	@Override
	public boolean registrarArchivoReporte (ReporteArchivo reporteArchivo) throws ConnectionException, SQLException{
		Connection conn = null;
		
		try {
			ArchivoReporteDao archivoReporteDao = new ArchivoReporteDaoImpl();
			conn = UtilConexion.obtenerConexion();
			
			int idArchivo = archivoReporteDao.registrarArchivoReporteCabecera(reporteArchivo, conn);
			
			reporteArchivo.getCabeceraArchivo().setIdArchivo(idArchivo);
			archivoReporteDao.registrarDetalleArchivoReporte(reporteArchivo.getCabeceraArchivo(), conn);
			for (ColumnasExcel columnasExcel : reporteArchivo.getDataExcel()){
				columnasExcel.setIdArchivo(idArchivo);
				archivoReporteDao.registrarDetalleArchivoReporte(columnasExcel, conn);
			}
			
			return true;
		} catch (SQLException e) {
			throw new ConnectionException(e);
		} finally{
			if (conn != null){
				conn.close();
			}
		}
	}
}
