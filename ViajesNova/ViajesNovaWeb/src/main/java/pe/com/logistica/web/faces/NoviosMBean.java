/**
 * 
 */
package pe.com.logistica.web.faces;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.ProgramaNovios;

/**
 * @author edwreb
 *
 */
@ManagedBean(name = "noviosMBean")
@SessionScoped()
public class NoviosMBean extends BaseMBean {

	
	private ProgramaNovios programaNovios;
	private Cliente clienteBusqueda;
	
	private List<ProgramaNovios> listadoNovios;
	/**
	 * 
	 */
	public NoviosMBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void buscarNovios(){
		
	}
	
	public void registrarNovios(){
		
	}
	/**
	 * @return the programaNovios
	 */
	public ProgramaNovios getProgramaNovios() {
		return programaNovios;
	}
	/**
	 * @param programaNovios the programaNovios to set
	 */
	public void setProgramaNovios(ProgramaNovios programaNovios) {
		this.programaNovios = programaNovios;
	}
	/**
	 * @return the listadoNovios
	 */
	public List<ProgramaNovios> getListadoNovios() {
		return listadoNovios;
	}
	/**
	 * @param listadoNovios the listadoNovios to set
	 */
	public void setListadoNovios(List<ProgramaNovios> listadoNovios) {
		this.listadoNovios = listadoNovios;
	}


	/**
	 * @return the clienteBusqueda
	 */
	public Cliente getClienteBusqueda() {
		return clienteBusqueda;
	}


	/**
	 * @param clienteBusqueda the clienteBusqueda to set
	 */
	public void setClienteBusqueda(Cliente clienteBusqueda) {
		this.clienteBusqueda = clienteBusqueda;
	}

}
