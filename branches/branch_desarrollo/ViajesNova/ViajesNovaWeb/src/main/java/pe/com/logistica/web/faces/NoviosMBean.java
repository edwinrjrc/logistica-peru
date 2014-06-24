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

	/**
	 * 
	 */
	private static final long serialVersionUID = -2578137810881699743L;
	
	
	private ProgramaNovios programaNovios;
	private ProgramaNovios programaNoviosBusqueda;
	private Cliente clienteBusqueda;
	private Integer codigoCliente;
	
	private int tipoBusqueda;
	
	private List<ProgramaNovios> listadoNovios;
	private List<Cliente> listadoClientes;
	
	private String generoCliente;
	
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
	public void consultaClientes(String genero, long busqueda){
		try {
			this.setClienteBusqueda(null);
			this.setGeneroCliente(genero);
			this.setTipoBusqueda((int)busqueda);
			
			this.setListadoClientes(this.negocioServicio.listarClientesNovios(genero));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void consultaDestinos(){
		
	}
	
	public void buscarClientes(){
		
	}
	public void seleccionarNovio(){
		if (this.getTipoBusqueda() == 1){
			if ("F".equals(this.getGeneroCliente())){
				this.getProgramaNoviosBusqueda().setNovia(obtenerClienteListado());
			}
			else {
				this.getProgramaNoviosBusqueda().setNovio(obtenerClienteListado());
			}
		}
		else {
			if ("F".equals(this.getGeneroCliente())){
				this.getProgramaNovios().setNovia(obtenerClienteListado());
			}
			else {
				this.getProgramaNovios().setNovio(obtenerClienteListado());
			}
		}
	}
	
	private Cliente obtenerClienteListado(){
		try {
			for (Cliente clienteLocal : this.getListadoClientes()){
				if (clienteLocal.getCodigoEntero().equals(this.getCodigoCliente())){
					return clienteLocal;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * @return the programaNovios
	 */
	public ProgramaNovios getProgramaNovios() {
		if (programaNovios == null){
			programaNovios = new ProgramaNovios();
		}
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
		if (clienteBusqueda == null){
			clienteBusqueda = new Cliente();
		}
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


	/**
	 * @return the programaNoviosBusqueda
	 */
	public ProgramaNovios getProgramaNoviosBusqueda() {
		if (programaNoviosBusqueda == null){
			programaNoviosBusqueda = new ProgramaNovios();
		}
		return programaNoviosBusqueda;
	}


	/**
	 * @param programaNoviosBusqueda the programaNoviosBusqueda to set
	 */
	public void setProgramaNoviosBusqueda(ProgramaNovios programaNoviosBusqueda) {
		this.programaNoviosBusqueda = programaNoviosBusqueda;
	}


	/**
	 * @return the generoCliente
	 */
	public String getGeneroCliente() {
		return generoCliente;
	}


	/**
	 * @param generoCliente the generoCliente to set
	 */
	public void setGeneroCliente(String generoCliente) {
		this.generoCliente = generoCliente;
	}


	/**
	 * @return the codigoCliente
	 */
	public Integer getCodigoCliente() {
		return codigoCliente;
	}


	/**
	 * @param codigoCliente the codigoCliente to set
	 */
	public void setCodigoCliente(Integer codigoCliente) {
		this.codigoCliente = codigoCliente;
	}


	/**
	 * @return the tipoBusqueda
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}


	/**
	 * @param tipoBusqueda the tipoBusqueda to set
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}


}
