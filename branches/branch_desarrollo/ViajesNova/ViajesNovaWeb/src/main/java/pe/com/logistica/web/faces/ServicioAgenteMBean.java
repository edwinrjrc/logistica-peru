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
import org.apache.log4j.Logger;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.Parametro;
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
	
	private final static Logger logger = Logger.getLogger(ServicioAgenteMBean.class);
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
	private boolean servicioFee;
	private boolean busquedaRealizada;

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
			logger.error(e.getMessage(), e);
		}
		
		consultarTasaPredeterminada();
	}
	
	public void consultarClientes(){
		try {
			this.setClienteBusqueda(null);

			this.setListadoClientes(this.negocioServicio.listarCliente());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void buscarCliente(){
		try {
			this.setListadoClientes(this.negocioServicio.buscarCliente(getClienteBusqueda()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public void buscarServicioRegistrado(){
		try {
			listadoServicioAgencia = this.negocioServicio.listarVentaServicio(getServicioAgenciaBusqueda());
			
			this.setBusquedaRealizada(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consultarServicioRegistrado(int idServicio){
		try {
			this.setServicioAgencia(this.negocioServicio.consultarVentaServicio(idServicio));
			
			this.setNombreFormulario("Editar Registro Venta");
			this.setNuevaVenta(false);
			this.setEditarVenta(true);
			this.setListadoDetalleServicio(this.getServicioAgencia().getListaDetalleServicio());
			this.setDetalleServicio(null);
			
			calcularTotales();
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
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
		this.setListadoDetalleServicio(null);
		
		HttpSession session = obtenerSession(false);
		Usuario usuario = (Usuario)session.getAttribute(USUARIO_SESSION);
		if (!Integer.valueOf(1).equals(usuario.getRol().getCodigoEntero())){
			this.getServicioAgencia().getVendedor().setCodigoEntero(usuario.getCodigoEntero());
			this.getServicioAgencia().getVendedor().setNombre(usuario.getNombreCompleto());	
		}
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
				
				this.setServicioFee(false);
				this.setListadoEmpresas(null);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
		}
	}
	
	private boolean validarRegistroServicioVenta() throws ErrorRegistroDataException, SQLException {
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
			this.agregarMensaje(idFormulario + ":idSelForPago",
					"Seleccione el forma de Pago", "", FacesMessage.SEVERITY_ERROR);
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
		if (this.getServicioAgencia().getVendedor().getCodigoEntero() == null || this.getServicioAgencia().getVendedor().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelVende",
					"Seleccione el Agente de Viajes", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioAgencia().getFechaServicio() == null){
			this.agregarMensaje(idFormulario + ":idSelFecSer",
					"Ingrese la Fecha de Servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (resultado){
			if (this.getListadoDetalleServicio().isEmpty()){
				throw new ErrorRegistroDataException("No se agregaron servicios a la venta");
			}
			else{
				
				validarServicios();
				/*
				Parametro param = this.parametroServicio.consultarParametro(UtilWeb.obtenerEnteroPropertieMaestro(
						"codigoParametroFee", "aplicacionDatos"));
				boolean servicioFee = false;
				for (DetalleServicioAgencia detalleServicio : this.getServicioAgencia().getListaDetalleServicio()){
					if (detalleServicio.getTipoServicio().getCodigoEntero().toString().equals(param.getValor())){
						servicioFee = true;
						break;
					}
				}
				
				if (!servicioFee){
					throw new ErrorRegistroDataException("No se agrego el Fee de Venta");
				}
				*/
			}
		}
		
		return resultado;
	}

	private void validarServicios() throws ErrorRegistroDataException {
		
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()){
			if (detalle.getTipoServicio().isRequiereFee()){
				validarFee();
			}
			if (detalle.getTipoServicio().isPagaImpto()){
				validarImpto();
			}
		}
		
	}

	private void validarImpto() throws ErrorRegistroDataException {
		boolean tieneImpto = false;
		
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()){
			if (detalle.getTipoServicio().isEsImpuesto()){
				tieneImpto = true;
				break;
			}
		}
		
		if (!tieneImpto){
			throw new ErrorRegistroDataException("No se agrego el Impuesto");
		}
		
	}

	private void validarFee() throws ErrorRegistroDataException {
		boolean tieneFee = false;
		
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()){
			if (detalle.getTipoServicio().isEsFee()){
				tieneFee = true;
				break;
			}
		}
		
		if (!tieneFee){
			throw new ErrorRegistroDataException("No se agrego el Fee de Venta");
		}
	}

	private boolean validarServicioVenta() {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (!this.isServicioFee()){
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
						"Ingrese el precio base del servicio", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getFechaIda() == null){
				this.agregarMensaje(idFormulario + ":idFecServicio",
						"Ingrese la fecha del servicio", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		else{
			if (this.getDetalleServicio().getPrecioUnitario() == null || this.getDetalleServicio().getPrecioUnitario().doubleValue() == 0.0){
				this.agregarMensaje(idFormulario + ":idMonFee",
						"Ingrese el Monto Fee", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getFechaIda() == null){
				this.agregarMensaje(idFormulario + ":idFecServicioFee",
						"Ingrese la fecha del servicio", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}
		
		return resultado;
	}

	private void calcularTotales() {
		BigDecimal montoTotal = BigDecimal.ZERO;
		BigDecimal montoComision = BigDecimal.ZERO;
		BigDecimal montoFee = BigDecimal.ZERO;
		try {
			
			Parametro param = this.parametroServicio.consultarParametro(UtilWeb.obtenerEnteroPropertieMaestro(
					"codigoParametroFee", "aplicacionDatos"));
			
			for (DetalleServicioAgencia detalleServicio : this.getListadoDetalleServicio()){
				montoTotal = montoTotal.add(detalleServicio.getTotalServicio());
				montoComision = montoComision.add(detalleServicio.getMontoComision());
				
				if (detalleServicio.getTipoServicio().getCodigoEntero().toString().equals(param.getValor())){
					montoFee = montoFee.add(detalleServicio.getTotalServicio());
				}
			}

		} catch (Exception e){
			logger.error(e.getMessage(), e);
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
					
					this.getServicioAgencia().setListaDetalleServicio(getListadoDetalleServicio());
					
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
				else if (this.isEditarVenta()){
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioModificacion(
							usuario.getUsuario());
					getServicioAgencia().setIpModificacion(
							obtenerRequest().getRemoteAddr());
					
					this.getServicioAgencia().setListaDetalleServicio(getListadoDetalleServicio());
					
					Integer idServicio = this.negocioServicio.registrarVentaServicio(getServicioAgencia());
					
					if (idServicio != null && idServicio.intValue() != 0){
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia().setCronogramaPago(this.negocioServicio.consultarCronogramaPago(getServicioAgencia()));
						this.setTransaccionExito(true);
					}
					
					this.setShowModal(true);
					this.setMensajeModal("Servicio Venta actualizado satisfactoriamente");
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
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			logger.error(e.getMessage(), e);
		}
	}
	
	public void calcularCuota(){
		BigDecimal valorCuota = BigDecimal.ZERO;
		try {
			valorCuota = this.negocioServicio.calcularValorCuota(this.getServicioAgencia());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.getServicioAgencia().setValorCuota(valorCuota);
	}
	
	public void consultarTasaPredeterminada(){
		try {
			int idtasatea = 3;
			BigDecimal ttea = UtilParse.parseStringABigDecimal(parametroServicio.consultarParametro(idtasatea).getValor()); 
			this.getServicioAgencia().setTea(ttea);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
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
						this.getServicioAgencia().getDestino().setDescripcion(destino.getDescripcion());
						break;
					}
				}
			}
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void cargarProveedores(ValueChangeEvent e){
		Object oe = e.getNewValue();
		try {
			setListadoEmpresas(null);
			this.getDetalleServicio().getServicioProveedor().setProveedor(null);
			
			if (oe != null){
				String valor = oe.toString();
				
				/*Parametro param = this.parametroServicio.consultarParametro(UtilWeb.obtenerEnteroPropertieMaestro(
						"codigoParametroFee", "aplicacionDatos"));
				this.setServicioFee(valor.equals(param.getValor()));*/
				
				MaestroServicio maestroServicio = this.negocioServicio.consultarMaestroServicio(UtilWeb.convertirCadenaEntero(valor));
				
				this.setServicioFee(maestroServicio.isEsFee() || maestroServicio.isEsImpuesto());
				
				if (!this.isServicioFee()){
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
			}
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
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
						break;
					}
				}
			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor().setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void eliminarServicio(DetalleServicioAgencia detalleServicio){
		this.getListadoDetalleServicio().remove(detalleServicio);
		
		calcularTotales();
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
			if (!busquedaRealizada){
				listadoServicioAgencia = this.negocioServicio.listarVentaServicio(getServicioAgenciaBusqueda());
			}
			
			this.setShowModal(false);
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
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

	/**
	 * @return the servicioFee
	 */
	public boolean isServicioFee() {
		return servicioFee;
	}

	/**
	 * @param servicioFee the servicioFee to set
	 */
	public void setServicioFee(boolean servicioFee) {
		this.servicioFee = servicioFee;
	}

	/**
	 * @return the busquedaRealizada
	 */
	public boolean isBusquedaRealizada() {
		return busquedaRealizada;
	}

	/**
	 * @param busquedaRealizada the busquedaRealizada to set
	 */
	public void setBusquedaRealizada(boolean busquedaRealizada) {
		this.busquedaRealizada = busquedaRealizada;
	}


}
