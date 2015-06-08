package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;

@Local
public interface ReportesLocal {

	public List<DetalleServicioAgencia> reporteGeneralVentas(Date desde, Date hasta) throws SQLException;
}
