/**
 * 
 */
package pe.com.logistica.web.faces;

import java.sql.SQLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;

/**
 * @author edwreb
 *
 */
@ManagedBean(name = "maeServiciosMBean")
@SessionScoped()
public class MaestroServiciosMBean extends BaseMBean {
	
	private final static Logger logger = Logger.getLogger(MaestroServiciosMBean.class);

	private MaestroServicio maestroServicio;
	
	private boolean nuevoMaestroServicio;
	private boolean editarMaestroServicio;
	
	private List<MaestroServicio> listaMaeServicio;
	
	private NegocioServicio negocioServicio;

	/**
	 * 
	 */
	private static final long serialVersionUID = -748394881867935320L;

	/**
	 * 
	 */
	public MaestroServiciosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			// soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void nuevoMaestro(){
		this.setNombreFormulario("Nuevo Servicio");
		this.setNuevoMaestroServicio(true);
		this.setEditarMaestroServicio(false);
	}
	
	public void ejecutarMetodo(){
		try {
			if (validarMaestroDestino()){
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario)session.getAttribute(USUARIO_SESSION);
				getMaestroServicio().setUsuarioCreacion(usuario.getUsuario());
				getMaestroServicio().setIpCreacion(obtenerRequest().getRemoteAddr());
				getMaestroServicio().setUsuarioModificacion(usuario.getUsuario());
				getMaestroServicio().setIpModificacion(obtenerRequest().getRemoteAddr());
				if (this.isNuevoMaestroServicio()){
					this.negocioServicio.ingresarMaestroServicio(getMaestroServicio());
					
					this.setShowModal(true);
					this.setMensajeModal("Servicio registrado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
				else{
					this.negocioServicio.actualizarMaestroServicio(getMaestroServicio());
					this.setShowModal(true);
					this.setMensajeModal("Servicio actualizado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
			}
		} catch (ErrorRegistroDataException e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (SQLException e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (Exception e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		}
		
	}
	
	private boolean validarMaestroDestino() {
		// TODO Auto-generated method stub
		return false;
	}

	public void consultarMaestroServicio(){
		this.setNombreFormulario("Editar Servicio");
		this.setNuevoMaestroServicio(false);
		this.setEditarMaestroServicio(true);
	}

	/**
	 * @return the maestroServicio
	 */
	public MaestroServicio getMaestroServicio() {
		return maestroServicio;
	}

	/**
	 * @param maestroServicio the maestroServicio to set
	 */
	public void setMaestroServicio(MaestroServicio maestroServicio) {
		this.maestroServicio = maestroServicio;
	}

	/**
	 * @return the nuevoMaestroServicio
	 */
	public boolean isNuevoMaestroServicio() {
		return nuevoMaestroServicio;
	}

	/**
	 * @param nuevoMaestroServicio the nuevoMaestroServicio to set
	 */
	public void setNuevoMaestroServicio(boolean nuevoMaestroServicio) {
		this.nuevoMaestroServicio = nuevoMaestroServicio;
	}

	/**
	 * @return the editarMaestroServicio
	 */
	public boolean isEditarMaestroServicio() {
		return editarMaestroServicio;
	}

	/**
	 * @param editarMaestroServicio the editarMaestroServicio to set
	 */
	public void setEditarMaestroServicio(boolean editarMaestroServicio) {
		this.editarMaestroServicio = editarMaestroServicio;
	}

	/**
	 * @return the listaMaeServicio
	 */
	public List<MaestroServicio> getListaMaeServicio() {
		return listaMaeServicio;
	}

	/**
	 * @param listaMaeServicio the listaMaeServicio to set
	 */
	public void setListaMaeServicio(List<MaestroServicio> listaMaeServicio) {
		this.listaMaeServicio = listaMaeServicio;
	}

}
