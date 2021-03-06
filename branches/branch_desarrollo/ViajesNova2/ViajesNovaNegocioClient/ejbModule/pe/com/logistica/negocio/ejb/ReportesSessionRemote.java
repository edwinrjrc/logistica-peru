package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.cargaexcel.ReporteArchivo;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.negocio.exception.ConnectionException;

@Remote
public interface ReportesSessionRemote {

	public List<DetalleServicioAgencia> reporteGeneralVentas(ReporteVentas reporteVentas) throws SQLException;

	boolean registrarArchivoReporte(ReporteArchivo reporteArchivo)
			throws ConnectionException, SQLException;
}
