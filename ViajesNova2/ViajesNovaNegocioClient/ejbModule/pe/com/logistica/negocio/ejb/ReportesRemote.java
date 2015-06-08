package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;

@Remote
public interface ReportesRemote {

	public List<DetalleServicioAgencia> reporteGeneralVentas(Date desde, Date hasta) throws SQLException;
}
