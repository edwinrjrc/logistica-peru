/**
 * 
 */
package pe.com.logistica.web.faces;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;

/**
 * @author Edwin
 *
 */
@ManagedBean(name = "servicioAgenteMBean")
@SessionScoped()
public class ServicioAgenteMBean extends BaseMBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3451688997471435575L;
	
	private ServicioAgencia servicioAgencia;
	private DetalleServicioAgencia detalleServicio;
	private Cliente clienteBusqueda;
	
	private List<ServicioAgencia> listadoServicioAgencia;
	private List<DetalleServicioAgencia> listadoDetalleServicio;
	private List<Cliente> listadoClientes;
	
	public boolean nuevaVenta;
	public boolean editarVenta;

	private ParametroServicio parametroServicio;
	private NegocioServicio negocioServicio;
	/**
	 * 
	 */
	public ServicioAgenteMBean() {
		try {
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			parametroServicio = new ParametroServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		try {
			int idtasatea = 3;
			BigDecimal ttea = UtilParse.parseStringABigDecimal(parametroServicio.consultarParametro(idtasatea).getValor()); 
			this.getServicioAgencia().setTea(ttea);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void consultarClientes(){
		try {
			this.setClienteBusqueda(null);

			this.setListadoClientes(this.negocioServicio.listarCliente());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buscarCliente(){
		

	}
	
	public void seleccionarCliente(){
		this.getServicioAgencia().setCliente(obtenerClienteListado());
	}
	
	private Cliente obtenerClienteListado() {
		try {
			for (Cliente clienteLocal : this.getListadoClientes()) {
				if (clienteLocal.getCodigoEntero().equals(
						clienteLocal.getCodigoSeleccionado())) {
					return clienteLocal;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void buscarServicioRegistrado(){
		
	}
	
	public void consultarServicioRegistrado(ServicioAgencia servicioAgencia){
		
	}
	
	public void registrarNuevaVenta(){
		this.setNombreFormulario("Nuevo Registro Venta");
		this.setNuevaVenta(true);
		this.setEditarVenta(false);
	}
	public void agregarServicio(){
		try {
			if (validarServicioVenta()){
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getDetalleServicio()
						.setUsuarioCreacion(usuario.getUsuario());
				getDetalleServicio().setIpCreacion(
						obtenerRequest().getRemoteAddr());
				this.getListadoDetalleServicio().add(negocioServicio.agregarServicioVenta(getDetalleServicio()));
				
				this.setListadoDetalleServicio(negocioServicio.ordenarServiciosVenta(getListadoDetalleServicio()));
				
				this.setDetalleServicio(null);
				
				calcularTotales();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean validarServicioVenta() {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (this.getDetalleServicio().getTipoServicio().getCodigoEntero() == null || this.getDetalleServicio().getTipoServicio().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelTipoServicio",
					"Seleccione el tipo de servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(this.getDetalleServicio().getDescripcionServicio())){
			this.agregarMensaje(idFormulario + ":idDescServicio",
					"Ingrese la descripcion del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getDetalleServicio().getCantidad() == 0){
			this.agregarMensaje(idFormulario + ":idCantidad",
					"Ingrese la cantidad", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getDetalleServicio().getPrecioUnitario() == null || this.getDetalleServicio().getPrecioUnitario().doubleValue() == 0.0){
			this.agregarMensaje(idFormulario + ":idPrecUnitario",
					"Ingrese el precio unitario del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getDetalleServicio().getFechaIda() == null){
			this.agregarMensaje(idFormulario + ":idPrecUnitario",
					"Ingrese la fecha del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		
		return resultado;
	}

	private void calcularTotales() {
		BigDecimal montoTotal = BigDecimal.ZERO;
		try {
			
			for (DetalleServicioAgencia detalleServicio : this.getListadoDetalleServicio()){
				montoTotal = montoTotal.add(detalleServicio.getTotalServicio());
			}

		} catch (Exception e){
			e.printStackTrace();
			montoTotal = BigDecimal.ZERO;
		}
		this.getServicioAgencia().setMontoTotalServicios(montoTotal);
		
	}

	public void ejecutarMetodo(){
		try {
			if (this.isNuevaVenta()){
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getServicioAgencia().setUsuarioCreacion(
						usuario.getUsuario());
				getServicioAgencia().setIpCreacion(
						obtenerRequest().getRemoteAddr());
				
				Integer idServicio = this.negocioServicio.registrarVentaServicio(getServicioAgencia());
				this.getServicioAgencia().setCodigoEntero(idServicio);
				this.getServicioAgencia().setCronogramaPago(this.negocioServicio.consultarCronogramaPago(getServicioAgencia()));
				
			}
		} catch (ErrorRegistroDataException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void calcularCuota(){
		BigDecimal valorCuota = BigDecimal.ZERO;
		try {
			valorCuota = this.negocioServicio.calcularValorCuota(this.getServicioAgencia());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getServicioAgencia().setValorCuota(valorCuota);
	}

	/**
	 * @return the servicioAgencia
	 */
	public ServicioAgencia getServicioAgencia() {
		if (servicioAgencia == null){
			servicioAgencia = new ServicioAgencia();
		}
		return servicioAgencia;
	}

	/**
	 * @param servicioAgencia the servicioAgencia to set
	 */
	public void setServicioAgencia(ServicioAgencia servicioAgencia) {
		this.servicioAgencia = servicioAgencia;
	}

	/**
	 * @return the listadoServicioAgencia
	 */
	public List<ServicioAgencia> getListadoServicioAgencia() {
		return listadoServicioAgencia;
	}

	/**
	 * @param listadoServicioAgencia the listadoServicioAgencia to set
	 */
	public void setListadoServicioAgencia(List<ServicioAgencia> listadoServicioAgencia) {
		this.listadoServicioAgencia = listadoServicioAgencia;
	}

	/**
	 * @return the nuevaVenta
	 */
	public boolean isNuevaVenta() {
		return nuevaVenta;
	}

	/**
	 * @param nuevaVenta the nuevaVenta to set
	 */
	public void setNuevaVenta(boolean nuevaVenta) {
		this.nuevaVenta = nuevaVenta;
	}

	/**
	 * @return the editarVenta
	 */
	public boolean isEditarVenta() {
		return editarVenta;
	}

	/**
	 * @param editarVenta the editarVenta to set
	 */
	public void setEditarVenta(boolean editarVenta) {
		this.editarVenta = editarVenta;
	}

	/**
	 * @return the detalleServicio
	 */
	public DetalleServicioAgencia getDetalleServicio() {
		if (detalleServicio == null){
			detalleServicio = new DetalleServicioAgencia();
		}
		return detalleServicio;
	}

	/**
	 * @param detalleServicio the detalleServicio to set
	 */
	public void setDetalleServicio(DetalleServicioAgencia detalleServicio) {
		this.detalleServicio = detalleServicio;
	}

	/**
	 * @return the listadoDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListadoDetalleServicio() {
		if (listadoDetalleServicio == null){
			listadoDetalleServicio = new ArrayList<DetalleServicioAgencia>();
		}
		return listadoDetalleServicio;
	}

	/**
	 * @param listadoDetalleServicio the listadoDetalleServicio to set
	 */
	public void setListadoDetalleServicio(List<DetalleServicioAgencia> listadoDetalleServicio) {
		this.listadoDetalleServicio = listadoDetalleServicio;
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
		return listadoClientes;
	}

	/**
	 * @param listadoClientes the listadoClientes to set
	 */
	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

}
