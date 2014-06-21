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

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;

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
	private List<Cliente> listadoClientes;
	
	private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;
	/**
	 * 
	 */
	public NoviosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public void buscarNovios(){
		
	}
	
	public void registrarNovios(){
		
	}
	
	public void consultaClientes(){
		
	}
	public void consultaClientes(String genero){
		try {
			this.setListadoClientes(this.negocioServicio.listarClientesNovios(genero));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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


	/**
	 * @return the listadoClientes
	 */
	public List<Cliente> getListadoClientes() {
		if (listadoClientes == null){
			listadoClientes = new ArrayList<Cliente>();
		}
		return listadoClientes;
	}


	/**
	 * @param listadoClientes the listadoClientes to set
	 */
	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

}
