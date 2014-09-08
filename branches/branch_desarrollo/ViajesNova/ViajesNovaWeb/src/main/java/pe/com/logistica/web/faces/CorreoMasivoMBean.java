/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "correoMasivoMBean" )
@SessionScoped()
public class CorreoMasivoMBean extends BaseMBean {
	
	private final static Logger logger = Logger.getLogger(CorreoMasivoMBean.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8169173720644254644L;
	
	private CorreoMasivo correoMasivo;
	
	private List<CorreoClienteMasivo> listaClientesCorreo;
	
	private List<UploadedFile> archivos;
	
	private NegocioServicio negocioServicio;

	/**
	 * 
	 */
	public CorreoMasivoMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void nuevoEnvio(){
		
	}
	
	public void cargarArchivo(){
		
	}
	
	public void listener(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();
        
        this.getArchivos().add(item);
    }

	/**
	 * @return the correoMasivo
	 */
	public CorreoMasivo getCorreoMasivo() {
		if (correoMasivo == null){
			correoMasivo = new CorreoMasivo();
		}
		return correoMasivo;
	}

	/**
	 * @param correoMasivo the correoMasivo to set
	 */
	public void setCorreoMasivo(CorreoMasivo correoMasivo) {
		this.correoMasivo = correoMasivo;
	}

	/**
	 * @return the listaClientesCorreo
	 */
	public List<CorreoClienteMasivo> getListaClientesCorreo() {
		try {
			listaClientesCorreo = negocioServicio.listarClientesCorreo();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaClientesCorreo;
	}

	/**
	 * @param listaClientesCorreo the listaClientesCorreo to set
	 */
	public void setListaClientesCorreo(List<CorreoClienteMasivo> listaClientesCorreo) {
		this.listaClientesCorreo = listaClientesCorreo;
	}

	/**
	 * @return the archivos
	 */
	public List<UploadedFile> getArchivos() {
		if (archivos == null){
			archivos = new ArrayList<UploadedFile>();
		}
		return archivos;
	}

	/**
	 * @param archivos the archivos to set
	 */
	public void setArchivos(List<UploadedFile> archivos) {
		this.archivos = archivos;
	}

}
