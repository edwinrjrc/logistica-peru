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
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.SoporteServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;
import pe.com.logistica.web.servicio.impl.SoporteServicioImpl;
import pe.com.logistica.web.util.UtilWeb;

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
	private ServicioAgencia servicioAgenciaBusqueda;
	private DetalleServicioAgencia detalleServicio;
	private Cliente clienteBusqueda;
	
	private List<ServicioAgencia> listadoServicioAgencia;
	private List<DetalleServicioAgencia> listadoDetalleServicio;
	private List<Cliente> listadoClientes;
	private List<SelectItem> listadoEmpresas;
	private List<ServicioProveedor> listaProveedores;
	
	public boolean nuevaVenta;
	public boolean editarVenta;

	private ParametroServicio parametroServicio;
	private NegocioServicio negocioServicio;
	private SoporteServicio soporteServicio;
	/**
	 * 
	 */
	public ServicioAgenteMBean() {
		try {
			ServletContext servletContext = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
			parametroServicio = new ParametroServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
			soporteServicio = new SoporteServicioImpl(servletContext);
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		consultarTasaPredeterminada();
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
		try {
			this.setListadoClientes(this.negocioServicio.buscarCliente(getClienteBusqueda()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
		this.setServicioAgencia(null);
		this.setDetalleServicio(null);
		this.setTransaccionExito(false);
		
		consultarTasaPredeterminada();
		this.setListadoEmpresas(null);
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
				
				this.getServicioAgencia().setListaDetalleServicio(getListadoDetalleServicio());
				
				this.setDetalleServicio(null);
				
				calcularTotales();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean validarRegistroServicioVenta() {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (this.getServicioAgencia().getCliente() == null || this.getServicioAgencia().getCliente().getCodigoEntero() == null || this.getServicioAgencia().getCliente().getCodigoEntero().intValue()==0){
			this.agregarMensaje(idFormulario + ":idFrCliente",
					"Seleccione el cliente del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioAgencia().getDestino().getCodigoEntero() == null || this.getServicioAgencia().getDestino().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelDestino",
					"Seleccione el destino global del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioAgencia().getFormaPago().getCodigoEntero() == null || this.getServicioAgencia().getFormaPago().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelDestino",
					"Seleccione el destino global del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		else {
			if (this.getServicioAgencia().getFormaPago().getCodigoEntero().intValue() == 2){
				if (this.getServicioAgencia().getTea() == null || this.getServicioAgencia().getTea().compareTo(BigDecimal.ZERO)==0){
					this.agregarMensaje(idFormulario + ":idTea",
							"Ingrese la tasa de interes", "", FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getNroCuotas() == 0){
					this.agregarMensaje(idFormulario + ":idNroCuotas",
							"Ingrese el número de cuotas", "", FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getFechaPrimerCuota() == null){
					this.agregarMensaje(idFormulario + ":idFecPriVcto",
							"Ingrese la fecha de primer vencimiento", "", FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
			}
		}
		return resultado;
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
			this.agregarMensaje(idFormulario + ":idFecServicio",
					"Ingrese la fecha del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		
		return resultado;
	}

	private void calcularTotales() {
		BigDecimal montoTotal = BigDecimal.ZERO;
		BigDecimal montoComision = BigDecimal.ZERO;
		BigDecimal montoFee = BigDecimal.ZERO;
		try {
			
			for (DetalleServicioAgencia detalleServicio : this.getListadoDetalleServicio()){
				montoTotal = montoTotal.add(detalleServicio.getTotalServicio());
				montoComision = montoComision.add(detalleServicio.getMontoComision());
				montoFee = montoFee.add(detalleServicio.getMontoFee());
			}

		} catch (Exception e){
			e.printStackTrace();
			montoTotal = BigDecimal.ZERO;
		}
		this.getServicioAgencia().setMontoTotalServicios(montoTotal);
		this.getServicioAgencia().setMontoTotalComision(montoComision);
		this.getServicioAgencia().setMontoTotalFee(montoFee);
		
	}

	public void ejecutarMetodo(){
		try {
			if (validarRegistroServicioVenta()){
				if (this.isNuevaVenta()){
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioCreacion(
							usuario.getUsuario());
					getServicioAgencia().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					
					Integer idServicio = this.negocioServicio.registrarVentaServicio(getServicioAgencia());
					
					if (idServicio != null && idServicio.intValue() != 0){
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia().setCronogramaPago(this.negocioServicio.consultarCronogramaPago(getServicioAgencia()));
						this.setTransaccionExito(true);
					}
					
					this.setShowModal(true);
					this.setMensajeModal("Servicio Venta registrado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
			}
			
		} catch (ErrorRegistroDataException e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (SQLException e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
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
	
	public void consultarTasaPredeterminada(){
		try {
			int idtasatea = 3;
			BigDecimal ttea = UtilParse.parseStringABigDecimal(parametroServicio.consultarParametro(idtasatea).getValor()); 
			this.getServicioAgencia().setTea(ttea);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void cambiarDestinoServicio(ValueChangeEvent e){
		Object oe = e.getNewValue();
		try {
			if (oe != null){
				String valor = oe.toString();
				
				List<Destino> listaDestino = this.soporteServicio.listarDestinos();
				
				for(Destino destino : listaDestino){
					if (destino.getCodigoEntero().equals(Integer.valueOf(valor))){
						this.getServicioAgencia().getDestino().setNombre(destino.getDescripcion());
						break;
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void cargarProveedores(ValueChangeEvent e){
		Object oe = e.getNewValue();
		try {
			if (oe != null){
				String valor = oe.toString();
				
				listaProveedores = this.negocioServicio.proveedoresXServicio(UtilWeb.convertirCadenaEntero(valor));
				setListadoEmpresas(null);
				
				SelectItem si = null;
				for(ServicioProveedor servicioProveedor : listaProveedores){
					si = new SelectItem();
					si.setValue(servicioProveedor.getCodigoEntero());
					si.setLabel(servicioProveedor.getNombreProveedor());
					getListadoEmpresas().add(si);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void seleccionarEmpresa(ValueChangeEvent e){
		Object oe = e.getNewValue();
		try {
			if (oe != null){
				String valor = oe.toString();
				
				for (ServicioProveedor servicioProveedor: this.listaProveedores){
					if (servicioProveedor.getCodigoEntero().intValue() == UtilWeb.convertirCadenaEntero(valor)){
						this.getDetalleServicio().getServicioProveedor().setPorcentajeComision(servicioProveedor.getPorcentajeComision());
						this.getDetalleServicio().getServicioProveedor().setPorcentajeFee(servicioProveedor.getPorcentajeFee());
						break;
					}
				}
			}
		} catch (Exception e1) {
			this.getDetalleServicio().getServicioProveedor().setPorcentajeComision(BigDecimal.ZERO);
			this.getDetalleServicio().getServicioProveedor().setPorcentajeFee(BigDecimal.ZERO);
		}
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
		try {
			listadoServicioAgencia = this.negocioServicio.consultarVentaServicio(getServicioAgenciaBusqueda());
			
			this.setShowModal(false);
		} catch (SQLException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
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

	/**
	 * @return the servicioAgenciaBusqueda
	 */
	public ServicioAgencia getServicioAgenciaBusqueda() {
		if (servicioAgenciaBusqueda == null){
			servicioAgenciaBusqueda = new ServicioAgencia();
		}
		return servicioAgenciaBusqueda;
	}

	/**
	 * @param servicioAgenciaBusqueda the servicioAgenciaBusqueda to set
	 */
	public void setServicioAgenciaBusqueda(ServicioAgencia servicioAgenciaBusqueda) {
		this.servicioAgenciaBusqueda = servicioAgenciaBusqueda;
	}

	/**
	 * @return the listadoEmpresas
	 */
	public List<SelectItem> getListadoEmpresas() {
		if (listadoEmpresas == null){
			listadoEmpresas = new ArrayList<SelectItem>();
		}
		return listadoEmpresas;
	}

	/**
	 * @param listadoEmpresas the listadoEmpresas to set
	 */
	public void setListadoEmpresas(List<SelectItem> listadoEmpresas) {
		this.listadoEmpresas = listadoEmpresas;
	}

	/**
	 * @return the listaProveedores
	 */
	public List<ServicioProveedor> getListaProveedores() {
		return listaProveedores;
	}

	/**
	 * @param listaProveedores the listaProveedores to set
	 */
	public void setListaProveedores(List<ServicioProveedor> listaProveedores) {
		this.listaProveedores = listaProveedores;
	}

}
