/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.reportes.ReporteVentas;
import pe.com.logistica.web.servicio.ReportesServicio;
import pe.com.logistica.web.servicio.impl.ReportesServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "reporteVentasMBean")
@SessionScoped()
public class ReporteVentasMBean extends BaseMBean {

	private final static Logger logger = Logger.getLogger(ReporteVentasMBean.class);

	private static final long serialVersionUID = -4496282062127472546L;
	
	private ReportesServicio reportesServicio;
	
	private List<DetalleServicioAgencia> reporteGeneralVentas;
	
	private ReporteVentas reporteVentas;
	
	public ReporteVentasMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			reportesServicio = new ReportesServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void consultaReporteGeneralVentas(){
		try {
			this.setReporteGeneralVentas(this.reportesServicio.reporteGeneralVentas(this.getReporteVentas()));
			
		} catch (SQLException e) {
			this.mostrarMensajeError(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * ==============================================================================================
	 */
	/**
	 * @return the reporteVentas
	 */
	public ReporteVentas getReporteVentas() {
		if (reporteVentas == null){
			reporteVentas = new ReporteVentas();
			Calendar cal = Calendar.getInstance();
			reporteVentas.setFechaHasta(cal.getTime());
			cal.add(Calendar.MONTH, -1);
			reporteVentas.setFechaDesde(cal.getTime());
		}
		return reporteVentas;
	}

	/**
	 * @param reporteVentas the reporteVentas to set
	 */
	public void setReporteVentas(ReporteVentas reporteVentas) {
		this.reporteVentas = reporteVentas;
	}

	/**
	 * @return the reporteGeneralVentas
	 */
	public List<DetalleServicioAgencia> getReporteGeneralVentas() {
		if (reporteGeneralVentas == null){
			reporteGeneralVentas = new ArrayList<DetalleServicioAgencia>();
		}
		return reporteGeneralVentas;
	}

	/**
	 * @param reporteGeneralVentas the reporteGeneralVentas to set
	 */
	public void setReporteGeneralVentas(List<DetalleServicioAgencia> reporteGeneralVentas) {
		this.reporteGeneralVentas = reporteGeneralVentas;
	}

}
