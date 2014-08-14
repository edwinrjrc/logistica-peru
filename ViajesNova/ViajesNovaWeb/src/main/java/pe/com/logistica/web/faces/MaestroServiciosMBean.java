/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pe.com.logistica.bean.negocio.MaestroServicio;

/**
 * @author edwreb
 *
 */
@ManagedBean(name = "maeServiciosMBean")
@SessionScoped()
public class MaestroServiciosMBean extends BaseMBean {

	private MaestroServicio maestroServicio;
	
	private boolean nuevoMaestroServicio;
	private boolean editarMaestroServicio;
	
	private List<MaestroServicio> listaMaeServicio;
	/**
	 * 
	 */
	private static final long serialVersionUID = -748394881867935320L;

	/**
	 * 
	 */
	public MaestroServiciosMBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void nuevoMaestro(){
		this.setNombreFormulario("Nuevo Servicio");
		this.setNuevoMaestroServicio(true);
		this.setEditarMaestroServicio(false);
	}
	
	public void ejecutarMetodo(){
		if (this.isNuevoMaestroServicio()){
			
		}
		else{
			
		}
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
