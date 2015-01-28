/**
 * 
 */
package pe.com.logistica.web.faces;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.PagoServicio;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorConsultaDataException;
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
public class ServicioAgenteMBean extends BaseMBean {

	private final static Logger logger = Logger
			.getLogger(ServicioAgenteMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 3451688997471435575L;

	private ServicioAgencia servicioAgencia;
	private ServicioAgencia servicioAgenciaBusqueda;
	private DetalleServicioAgencia detalleServicio;
	private Cliente clienteBusqueda;
	private Destino destinoBusqueda;
	private Destino origenBusqueda;
	private PagoServicio pagoServicio;
	
	private BigDecimal saldoServicio;

	private List<ServicioAgencia> listadoServicioAgencia;
	private List<DetalleServicioAgencia> listadoDetalleServicio;
	private List<DetalleServicioAgencia> listadoDetalleServicioTotal;
	private List<Cliente> listadoClientes;
	private List<SelectItem> listadoEmpresas;
	private List<ServicioProveedor> listaProveedores;
	private List<Destino> listaDestinosBusqueda;
	private List<Destino> listaOrigenesBusqueda;
	private List<PagoServicio> listaPagosServicios;

	public boolean nuevaVenta;
	public boolean editarVenta;
	private boolean servicioFee;
	private boolean busquedaRealizada;
	private boolean editarComision;
	private boolean vendedor;

	private ParametroServicio parametroServicio;
	private NegocioServicio negocioServicio;
	private SoporteServicio soporteServicio;

	/**
	 * 
	 */
	public ServicioAgenteMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			parametroServicio = new ParametroServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
			soporteServicio = new SoporteServicioImpl(servletContext);

		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}

		consultarTasaPredeterminada();
	}

	public void consultarClientes() {
		try {
			this.setClienteBusqueda(null);

			this.setListadoClientes(this.negocioServicio.listarCliente());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void consultarDestinos() {
		try {
			this.setListaDestinosBusqueda(null);
			this.setDestinoBusqueda(null);

			List<Destino> listaDestinos = this.soporteServicio.listarDestinos();

			this.setListaDestinosBusqueda(listaDestinos);
			this.setListaOrigenesBusqueda(listaDestinos);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarCliente() {
		try {
			this.setListadoClientes(this.negocioServicio
					.buscarCliente(getClienteBusqueda()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void seleccionarCliente() {
		this.getServicioAgencia().setCliente(obtenerClienteListado());
	}

	public void seleccionarDestino() {
		try {
			this.getDetalleServicio().setDestino(obtenerDestinoListado());

			this.getDetalleServicio()
					.getServicioProveedor()
					.setPorcentajeComision(
							this.negocioServicio.calculaPorcentajeComision(this
									.getDetalleServicio()));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e.printStackTrace();
		}
	}

	private Destino obtenerDestinoListado() {
		try {
			for (Destino destino : this.listaDestinosBusqueda) {
				if (destino.getCodigoEntero().intValue() == destino
						.getCodigoSeleccionado().intValue()) {
					return destino;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

	public void buscarServicioRegistrado() {
		try {
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
			if (Integer.valueOf(2).equals(usuario.getRol().getCodigoEntero()) || Integer.valueOf(4).equals(usuario.getRol().getCodigoEntero())) {
				getServicioAgenciaBusqueda().getVendedor().setCodigoEntero(usuario.getCodigoEntero());
			}
			
			listadoServicioAgencia = this.negocioServicio
					.listarVentaServicio(getServicioAgenciaBusqueda());

			this.setBusquedaRealizada(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void consultarServicioRegistrado(int idServicio) {
		try {
			this.setServicioAgencia(this.negocioServicio
					.consultarVentaServicio(idServicio));

			this.setNombreFormulario("Editar Registro Venta");
			this.setNuevaVenta(false);
			this.setEditarVenta(true);
			this.setListadoDetalleServicio(this.getServicioAgencia()
					.getListaDetalleServicio());
			this.setDetalleServicio(null);

			this.setListadoDetalleServicioTotal(this
					.getListadoDetalleServicio());

			borrarInvisibles();

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void borrarInvisibles() {
		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (!detalle.getTipoServicio().isVisible()) {
				this.getListadoDetalleServicio().remove(detalle);
			}
		}

	}

	public void registrarNuevaVenta() {
		this.setNombreFormulario("Nuevo Registro Venta");
		this.setNuevaVenta(true);
		this.setEditarVenta(false);
		this.setServicioAgencia(null);
		this.setDetalleServicio(null);
		this.setTransaccionExito(false);

		consultarTasaPredeterminada();
		this.setListadoEmpresas(null);
		this.setListadoDetalleServicio(null);
		this.setListadoDetalleServicioTotal(null);

		this.setVendedor(false);
		HttpSession session = obtenerSession(false);
		Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
		if (Integer.valueOf(2).equals(usuario.getRol().getCodigoEntero())) {
			this.getServicioAgencia().getVendedor()
					.setCodigoEntero(usuario.getCodigoEntero());
			this.getServicioAgencia().getVendedor()
					.setNombre(usuario.getNombreCompleto());
			this.setVendedor(true);
		}

		this.getServicioAgencia().setFechaServicio(new Date());
		
	}

	public void agregarServicio() {
		try {
			if (validarServicioVenta()) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getDetalleServicio().setUsuarioCreacion(usuario.getUsuario());
				getDetalleServicio().setIpCreacion(
						obtenerRequest().getRemoteAddr());

				getDetalleServicio().getServicioProveedor().setEditoComision(
						this.isEditarComision());

				DetalleServicioAgencia detalleServicioAgregar = negocioServicio
						.agregarServicioVenta(getDetalleServicio());

				detalleServicioAgregar = agregarServicioInvisible(detalleServicioAgregar);
				
				this.getListadoDetalleServicio().add(detalleServicioAgregar);
				this.getListadoDetalleServicioTotal().add(
						detalleServicioAgregar);

				this.setListadoDetalleServicio(negocioServicio
						.ordenarServiciosVenta(getListadoDetalleServicio()));

				this.setDetalleServicio(null);

				calcularTotales();

				this.setServicioFee(false);
				this.setListadoEmpresas(null);
			}
		} catch (ErrorRegistroDataException e){
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
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

	private DetalleServicioAgencia agregarServicioInvisible(
			DetalleServicioAgencia detalleServicio2)
			throws ErrorConsultaDataException, Exception {
		List<DetalleServicioAgencia> lista = negocioServicio
				.agregarServicioVentaInvisible(detalleServicio2);
		HttpSession session = obtenerSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		for (DetalleServicioAgencia detalleServicioAgencia : lista) {
			detalleServicioAgencia.setUsuarioCreacion(usuario.getUsuario());
			detalleServicioAgencia.setUsuarioModificacion(usuario.getUsuario());
			detalleServicioAgencia.setIpCreacion(obtenerRequest()
					.getRemoteAddr());
			detalleServicioAgencia.setIpModificacion(obtenerRequest()
					.getRemoteAddr());
		}
		detalleServicio2.setServiciosHijos(lista);

		return detalleServicio2;
	}

	private boolean validarRegistroServicioVenta() throws Exception {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (this.getServicioAgencia().getCliente() == null
				|| this.getServicioAgencia().getCliente().getCodigoEntero() == null
				|| this.getServicioAgencia().getCliente().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idFrCliente",
					"Seleccione el cliente del servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		/*
		 * if (this.getServicioAgencia().getDestino().getCodigoEntero() == null
		 * ||
		 * this.getServicioAgencia().getDestino().getCodigoEntero().intValue()
		 * == 0){ this.agregarMensaje(idFormulario + ":idSelDestino",
		 * "Seleccione el destino global del servicio", "",
		 * FacesMessage.SEVERITY_ERROR); resultado = false; }
		 */
		if (this.getServicioAgencia().getFormaPago().getCodigoEntero() == null
				|| this.getServicioAgencia().getFormaPago().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelForPago",
					"Seleccione el forma de Pago", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		} else {
			if (this.getServicioAgencia().getFormaPago().getCodigoEntero()
					.intValue() == 2) {
				if (this.getServicioAgencia().getTea() == null
						|| this.getServicioAgencia().getTea()
								.compareTo(BigDecimal.ZERO) == 0) {
					this.agregarMensaje(idFormulario + ":idTea",
							"Ingrese la tasa de interes", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getNroCuotas() == 0) {
					this.agregarMensaje(idFormulario + ":idNroCuotas",
							"Ingrese el número de cuotas", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
				if (this.getServicioAgencia().getFechaPrimerCuota() == null) {
					this.agregarMensaje(idFormulario + ":idFecPriVcto",
							"Ingrese la fecha de primer vencimiento", "",
							FacesMessage.SEVERITY_ERROR);
					resultado = false;
				}
			}
		}
		if (this.getServicioAgencia().getVendedor().getCodigoEntero() == null
				|| this.getServicioAgencia().getVendedor().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelVende",
					"Seleccione el Agente de Viajes", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioAgencia().getFechaServicio() == null) {
			this.agregarMensaje(idFormulario + ":idSelFecSer",
					"Ingrese la Fecha de Servicio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (resultado) {
			if (this.getListadoDetalleServicio().isEmpty()) {
				throw new ErrorRegistroDataException(
						"No se agregaron servicios a la venta");
			} else {

				validarServicios();

				validarFee();
				/*
				 * Parametro param =
				 * this.parametroServicio.consultarParametro(UtilWeb
				 * .obtenerEnteroPropertieMaestro( "codigoParametroFee",
				 * "aplicacionDatos")); boolean servicioFee = false; for
				 * (DetalleServicioAgencia detalleServicio :
				 * this.getServicioAgencia().getListaDetalleServicio()){ if
				 * (detalleServicio
				 * .getTipoServicio().getCodigoEntero().toString(
				 * ).equals(param.getValor())){ servicioFee = true; break; } }
				 * 
				 * if (!servicioFee){ throw new
				 * ErrorRegistroDataException("No se agrego el Fee de Venta"); }
				 */
			}
		}

		return resultado;
	}

	private void validarServicios() throws ErrorRegistroDataException,
			SQLException, Exception {

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {

			List<BaseVO> listaDependientes = this.negocioServicio
					.consultaServiciosDependientes(detalle.getTipoServicio()
							.getCodigoEntero());

			for (BaseVO baseVO : listaDependientes) {
				if (!estaEnListaServicios(baseVO)) {
					throw new ErrorRegistroDataException("No se agrego "
							+ baseVO.getNombre());
				}
			}

		}

	}

	private boolean estaEnListaServicios(BaseVO baseVO) {
		boolean resultado = false;

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (detalle.getTipoServicio().getCodigoEntero().intValue() == baseVO
					.getCodigoEntero().intValue()) {
				resultado = true;
			}
		}

		return resultado;
	}

	private void validarImpto() throws ErrorRegistroDataException {
		boolean tieneImpto = false;

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (detalle.getTipoServicio().isEsImpuesto()) {
				tieneImpto = true;
				break;
			}
		}

		if (!tieneImpto) {
			throw new ErrorRegistroDataException("No se agrego el Impuesto");
		}

	}

	private void validarFee() throws ErrorRegistroDataException {
		boolean tieneFee = false;

		for (DetalleServicioAgencia detalle : this.getListadoDetalleServicio()) {
			if (detalle.getTipoServicio().isEsFee()) {
				tieneFee = true;
				break;
			}
		}

		if (!tieneFee) {
			throw new ErrorRegistroDataException("No se agrego el Fee de Venta");
		}
	}

	private boolean validarServicioVenta() {
		boolean resultado = true;
		String idFormulario = "idFormVentaServi";
		if (!this.isServicioFee()) {
			if (this.getDetalleServicio().getTipoServicio().getCodigoEntero() == null
					|| this.getDetalleServicio().getTipoServicio()
							.getCodigoEntero().intValue() == 0) {
				this.agregarMensaje(idFormulario + ":idSelTipoServicio",
						"Seleccione el tipo de servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (StringUtils.isBlank(this.getDetalleServicio()
					.getDescripcionServicio())) {
				this.agregarMensaje(idFormulario + ":idDescServicio",
						"Ingrese la descripcion del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getCantidad() == 0) {
				this.agregarMensaje(idFormulario + ":idCantidad",
						"Ingrese la cantidad", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getPrecioUnitario() == null
					|| this.getDetalleServicio().getPrecioUnitario()
							.doubleValue() == 0.0) {
				this.agregarMensaje(idFormulario + ":idPrecUnitario",
						"Ingrese el precio base del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getFechaIda() == null) {
				this.agregarMensaje(idFormulario + ":idFecServicio",
						"Ingrese la fecha del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (UtilWeb.fecha1EsMayorIgualFecha2(this.getDetalleServicio()
					.getFechaIda(), new Date())) {
				this.agregarMensaje(
						idFormulario + ":idFecServicio",
						"La fecha del servicio no puede ser menor que la fecha de actual",
						"", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			} else if (this.getDetalleServicio().getFechaRegreso() != null
					&& this.getDetalleServicio().getFechaIda()
							.after(this.getDetalleServicio().getFechaRegreso())) {
				this.agregarMensaje(
						idFormulario + ":idFecServicio",
						"La fecha del servicio no puede ser mayor que la fecha de regreso",
						"", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getServicioProveedor().getProveedor()
					.getCodigoEntero() == null
					|| this.getDetalleServicio().getServicioProveedor()
							.getProveedor().getCodigoEntero().intValue() == 0) {
				this.agregarMensaje(idFormulario + ":idSelEmpServicio",
						"Seleccione el proveedor del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			/*
			 * if
			 * (this.getDetalleServicio().getConsolidador().getCodigoEntero()==
			 * null ||
			 * this.getDetalleServicio().getConsolidador().getCodigoEntero
			 * ().intValue()==0){ this.agregarMensaje(idFormulario +
			 * ":idSelconsolidador", "Seleccione el consolidador del servicio",
			 * "", FacesMessage.SEVERITY_ERROR); resultado = false; }
			 */
		} else {
			if (this.getDetalleServicio().getPrecioUnitario() == null) {
				this.agregarMensaje(idFormulario + ":idMonFee",
						"Ingrese el Monto Fee", "", FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
			if (this.getDetalleServicio().getFechaIda() == null) {
				this.agregarMensaje(idFormulario + ":idFecServicioFee",
						"Ingrese la fecha del servicio", "",
						FacesMessage.SEVERITY_ERROR);
				resultado = false;
			}
		}

		return resultado;
	}

	private void calcularTotales() {
		BigDecimal montoTotal = BigDecimal.ZERO;
		BigDecimal montoComision = BigDecimal.ZERO;
		BigDecimal montoFee = BigDecimal.ZERO;
		BigDecimal montoIgv = BigDecimal.ZERO;
		try {

			/*
			 * Parametro param =
			 * this.parametroServicio.consultarParametro(UtilWeb
			 * .obtenerEnteroPropertieMaestro( "codigoParametroFee",
			 * "aplicacionDatos"));
			 */

			for (DetalleServicioAgencia detalleServicio : this
					.getListadoDetalleServicioTotal()) {
				montoTotal = montoTotal.add(detalleServicio.getTotalServicio());
				montoComision = montoComision.add(detalleServicio
						.getMontoComision());
				for (DetalleServicioAgencia detalleServicio2 : detalleServicio
						.getServiciosHijos()) {
					montoIgv = montoIgv.add(detalleServicio2.getMontoIGV());
				}

				if (detalleServicio.getTipoServicio().getCodigoEntero() != null
						&& detalleServicio.getTipoServicio().isEsFee()) {
					montoFee = montoFee.add(detalleServicio.getTotalServicio());
				}

			}

			montoTotal = montoTotal.add(montoIgv);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			montoTotal = BigDecimal.ZERO;
		}

		this.getServicioAgencia().setMontoTotalServicios(montoTotal);
		this.getServicioAgencia().setMontoTotalComision(montoComision);
		this.getServicioAgencia().setMontoTotalFee(montoFee);
		this.getServicioAgencia().setMontoTotalIGV(montoIgv);
	}

	public void ejecutarMetodo() {
		try {
			if (validarRegistroServicioVenta()) {
				if (this.isNuevaVenta()) {
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioCreacion(
							usuario.getUsuario());
					getServicioAgencia().setIpCreacion(
							obtenerRequest().getRemoteAddr());

					this.getServicioAgencia().setListaDetalleServicio(
							getListadoDetalleServicioTotal());

					Integer idServicio = this.negocioServicio
							.registrarVentaServicio(getServicioAgencia());

					if (idServicio != null && idServicio.intValue() != 0) {
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia()
								.setCronogramaPago(
										this.negocioServicio
												.consultarCronogramaPago(getServicioAgencia()));
						this.setTransaccionExito(true);
					}

					this.setShowModal(true);
					this.setMensajeModal("Servicio Venta registrado satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				} else if (this.isEditarVenta()) {
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getServicioAgencia().setUsuarioCreacion(
							usuario.getUsuario());
					getServicioAgencia().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					getServicioAgencia().setUsuarioModificacion(
							usuario.getUsuario());
					getServicioAgencia().setIpModificacion(
							obtenerRequest().getRemoteAddr());

					for (DetalleServicioAgencia detalle : getListadoDetalleServicio()) {
						detalle.setUsuarioCreacion(usuario.getUsuario());
						detalle.setIpCreacion(obtenerRequest().getRemoteAddr());
						detalle.setUsuarioModificacion(usuario.getUsuario());
						detalle.setIpModificacion(obtenerRequest()
								.getRemoteAddr());
					}

					this.getServicioAgencia().setListaDetalleServicio(
							getListadoDetalleServicio());

					Integer idServicio = this.negocioServicio
							.actualizarVentaServicio(getServicioAgencia());

					if (idServicio != null && idServicio.intValue() != 0) {
						this.getServicioAgencia().setCodigoEntero(idServicio);
						this.getServicioAgencia()
								.setCronogramaPago(
										this.negocioServicio
												.consultarCronogramaPago(getServicioAgencia()));
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

	public void calcularCuota() {
		BigDecimal valorCuota = BigDecimal.ZERO;
		try {
			valorCuota = this.negocioServicio.calcularValorCuota(this
					.getServicioAgencia());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.getServicioAgencia().setValorCuota(valorCuota);
	}

	public void consultarTasaPredeterminada() {
		try {
			int idtasatea = 3;
			BigDecimal ttea = UtilParse
					.parseStringABigDecimal(parametroServicio
							.consultarParametro(idtasatea).getValor());
			this.getServicioAgencia().setTea(ttea);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void cambiarDestinoServicio(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				boolean destinoNacional = this.soporteServicio
						.esDestinoNacional(UtilWeb.convertirCadenaEntero(valor));

				System.out.println("es nacional" + destinoNacional);

			}
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void cambiarAerolinea(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				List<Destino> listaDestino = this.soporteServicio
						.listarDestinos();

				for (Destino destino : listaDestino) {
					if (destino.getCodigoEntero()
							.equals(Integer.valueOf(valor))) {
						this.getServicioAgencia().getDestino()
								.setDescripcion(destino.getDescripcion());
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

	public void cargarDatosValores(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			setListadoEmpresas(null);
			this.getDetalleServicio().getServicioProveedor().setProveedor(null);

			if (oe != null) {
				String valor = oe.toString();

				/*
				 * Parametro param =
				 * this.parametroServicio.consultarParametro(UtilWeb
				 * .obtenerEnteroPropertieMaestro( "codigoParametroFee",
				 * "aplicacionDatos"));
				 * this.setServicioFee(valor.equals(param.getValor()));
				 */

				MaestroServicio maestroServicio = this.negocioServicio
						.consultarMaestroServicio(UtilWeb
								.convertirCadenaEntero(valor));

				this.getDetalleServicio().setConfiguracionTipoServicio(
						this.soporteServicio
								.consultarConfiguracionServicio(UtilWeb
										.convertirCadenaEntero(valor)));

				this.setServicioFee(maestroServicio.isEsFee()
						|| maestroServicio.isEsImpuesto());

				if (!this.isServicioFee()) {
					listaProveedores = this.negocioServicio
							.proveedoresXServicio(UtilWeb
									.convertirCadenaEntero(valor));
					setListadoEmpresas(null);

					SelectItem si = null;
					for (ServicioProveedor servicioProveedor : listaProveedores) {
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

	public void seleccionarEmpresa(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getServicioProveedor().getProveedor()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}

	public void seleccionarAerolinea(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getAerolinea()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}

	public void eliminarServicio(DetalleServicioAgencia detalleServicio) {
		if (listadoDetalleServicio != null) {
			for (int i = 0; i < listadoDetalleServicio.size(); i++) {
				DetalleServicioAgencia detalle = listadoDetalleServicio.get(i);
				if (detalleServicio.getCodigoCadena().equals(
						detalle.getCodigoCadena())) {
					this.listadoDetalleServicio.remove(i);
				}
			}
			for (int i = 0; i < listadoDetalleServicioTotal.size(); i++) {
				DetalleServicioAgencia detalle = listadoDetalleServicioTotal
						.get(i);
				if (detalleServicio.getCodigoCadena().equals(
						detalle.getCodigoCadena())) {
					this.listadoDetalleServicioTotal.remove(i);
				}
			}
		}

		calcularTotales();
	}

	public void buscarDestino() {

	}

	public void buscarOrigen() {

	}

	public void seleccionarOrigen() {
		try {
			this.getDetalleServicio().setOrigen(obtenerOrigenListado());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e.printStackTrace();
		}
	}

	private Destino obtenerOrigenListado() {
		try {
			for (Destino destino : this.getListaOrigenesBusqueda()) {
				if (destino.getCodigoEntero().intValue() == destino
						.getCodigoSeleccionado().intValue()) {
					return destino;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void seleccionarHotel(ValueChangeEvent e) {
		Object oe = e.getNewValue();
		try {
			if (oe != null) {
				String valor = oe.toString();

				this.getDetalleServicio().getHotel()
						.setCodigoEntero(UtilWeb.convertirCadenaEntero(valor));

				this.getDetalleServicio()
						.getServicioProveedor()
						.setPorcentajeComision(
								this.negocioServicio
										.calculaPorcentajeComision(this
												.getDetalleServicio()));

			}
		} catch (Exception ex) {
			this.getDetalleServicio().getServicioProveedor()
					.setPorcentajeComision(BigDecimal.ZERO);
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void seleccionarOperadora(){
		
	}
	
	public void registrarNuevoPago(){
		this.setPagoServicio(null);
	}

	public void registrarPago() {
		try {
			this.getPagoServicio().getServicio().setCodigoEntero(this.getServicioAgencia().getCodigoEntero());
			
			HttpSession session = obtenerSession(false);
			Usuario usuario = (Usuario) session
					.getAttribute("usuarioSession");
			this.getPagoServicio().setUsuarioCreacion(
					usuario.getUsuario());
			this.getPagoServicio().setIpCreacion(
					obtenerRequest().getRemoteAddr());
			this.getPagoServicio().setUsuarioModificacion(
					usuario.getUsuario());
			this.getPagoServicio().setIpModificacion(
					obtenerRequest().getRemoteAddr());
			
			this.negocioServicio.registrarPago(getPagoServicio());

			this.setShowModal(true);
			this.setMensajeModal("Pago Registrado Satisfactoriamente");
			this.setTipoModal(TIPO_MODAL_EXITO);
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

	public void listener(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();

		this.getPagoServicio().setSustentoPago(item.getInputStream());
		this.getPagoServicio().setSustentoPagoByte(IOUtils.toByteArray(item.getInputStream()));
	}

	/**
	 * @return the servicioAgencia
	 */
	public ServicioAgencia getServicioAgencia() {
		if (servicioAgencia == null) {
			servicioAgencia = new ServicioAgencia();
		}
		return servicioAgencia;
	}

	/**
	 * @param servicioAgencia
	 *            the servicioAgencia to set
	 */
	public void setServicioAgencia(ServicioAgencia servicioAgencia) {
		this.servicioAgencia = servicioAgencia;
	}

	/**
	 * @return the listadoServicioAgencia
	 */
	public List<ServicioAgencia> getListadoServicioAgencia() {
		try {
			if (!busquedaRealizada) {
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session.getAttribute(USUARIO_SESSION);
				if (Integer.valueOf(2).equals(usuario.getRol().getCodigoEntero())) {
					getServicioAgenciaBusqueda().getVendedor().setCodigoEntero(usuario.getCodigoEntero());
				}
				
				listadoServicioAgencia = this.negocioServicio
						.listarVentaServicio(getServicioAgenciaBusqueda());
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
	 * @param listadoServicioAgencia
	 *            the listadoServicioAgencia to set
	 */
	public void setListadoServicioAgencia(
			List<ServicioAgencia> listadoServicioAgencia) {
		this.listadoServicioAgencia = listadoServicioAgencia;
	}

	/**
	 * @return the nuevaVenta
	 */
	public boolean isNuevaVenta() {
		return nuevaVenta;
	}

	/**
	 * @param nuevaVenta
	 *            the nuevaVenta to set
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
	 * @param editarVenta
	 *            the editarVenta to set
	 */
	public void setEditarVenta(boolean editarVenta) {
		this.editarVenta = editarVenta;
	}

	/**
	 * @return the detalleServicio
	 */
	public DetalleServicioAgencia getDetalleServicio() {
		if (detalleServicio == null) {
			detalleServicio = new DetalleServicioAgencia();
		}
		return detalleServicio;
	}

	/**
	 * @param detalleServicio
	 *            the detalleServicio to set
	 */
	public void setDetalleServicio(DetalleServicioAgencia detalleServicio) {
		this.detalleServicio = detalleServicio;
	}

	/**
	 * @return the listadoDetalleServicio
	 */
	public List<DetalleServicioAgencia> getListadoDetalleServicio() {
		if (listadoDetalleServicio == null) {
			listadoDetalleServicio = new ArrayList<DetalleServicioAgencia>();
		}
		return listadoDetalleServicio;
	}

	/**
	 * @param listadoDetalleServicio
	 *            the listadoDetalleServicio to set
	 */
	public void setListadoDetalleServicio(
			List<DetalleServicioAgencia> listadoDetalleServicio) {
		this.listadoDetalleServicio = listadoDetalleServicio;
	}

	/**
	 * @return the clienteBusqueda
	 */
	public Cliente getClienteBusqueda() {
		if (clienteBusqueda == null) {
			clienteBusqueda = new Cliente();
		}
		return clienteBusqueda;
	}

	/**
	 * @param clienteBusqueda
	 *            the clienteBusqueda to set
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
	 * @param listadoClientes
	 *            the listadoClientes to set
	 */
	public void setListadoClientes(List<Cliente> listadoClientes) {
		this.listadoClientes = listadoClientes;
	}

	/**
	 * @return the servicioAgenciaBusqueda
	 */
	public ServicioAgencia getServicioAgenciaBusqueda() {
		if (servicioAgenciaBusqueda == null) {
			servicioAgenciaBusqueda = new ServicioAgencia();
		}
		return servicioAgenciaBusqueda;
	}

	/**
	 * @param servicioAgenciaBusqueda
	 *            the servicioAgenciaBusqueda to set
	 */
	public void setServicioAgenciaBusqueda(
			ServicioAgencia servicioAgenciaBusqueda) {
		this.servicioAgenciaBusqueda = servicioAgenciaBusqueda;
	}

	/**
	 * @return the listadoEmpresas
	 */
	public List<SelectItem> getListadoEmpresas() {
		if (listadoEmpresas == null) {
			listadoEmpresas = new ArrayList<SelectItem>();
		}
		return listadoEmpresas;
	}

	/**
	 * @param listadoEmpresas
	 *            the listadoEmpresas to set
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
	 * @param listaProveedores
	 *            the listaProveedores to set
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
	 * @param servicioFee
	 *            the servicioFee to set
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
	 * @param busquedaRealizada
	 *            the busquedaRealizada to set
	 */
	public void setBusquedaRealizada(boolean busquedaRealizada) {
		this.busquedaRealizada = busquedaRealizada;
	}

	/**
	 * @return the listadoDetalleServicioTotal
	 */
	public List<DetalleServicioAgencia> getListadoDetalleServicioTotal() {
		if (listadoDetalleServicioTotal == null) {
			listadoDetalleServicioTotal = new ArrayList<DetalleServicioAgencia>();
		}
		return listadoDetalleServicioTotal;
	}

	/**
	 * @param listadoDetalleServicioTotal
	 *            the listadoDetalleServicioTotal to set
	 */
	public void setListadoDetalleServicioTotal(
			List<DetalleServicioAgencia> listadoDetalleServicioTotal) {
		this.listadoDetalleServicioTotal = listadoDetalleServicioTotal;
	}

	/**
	 * @return the destinoBusqueda
	 */
	public Destino getDestinoBusqueda() {
		if (destinoBusqueda == null) {
			destinoBusqueda = new Destino();
		}
		return destinoBusqueda;
	}

	/**
	 * @param destinoBusqueda
	 *            the destinoBusqueda to set
	 */
	public void setDestinoBusqueda(Destino destinoBusqueda) {
		this.destinoBusqueda = destinoBusqueda;
	}

	/**
	 * @return the listaDestinosBusqueda
	 */
	public List<Destino> getListaDestinosBusqueda() {
		return listaDestinosBusqueda;
	}

	/**
	 * @param listaDestinosBusqueda
	 *            the listaDestinosBusqueda to set
	 */
	public void setListaDestinosBusqueda(List<Destino> listaDestinosBusqueda) {
		this.listaDestinosBusqueda = listaDestinosBusqueda;
	}

	/**
	 * @return the editarComision
	 */
	public boolean isEditarComision() {
		return editarComision;
	}

	/**
	 * @param editarComision
	 *            the editarComision to set
	 */
	public void setEditarComision(boolean editarComision) {
		this.editarComision = editarComision;
	}

	/**
	 * @return the origenBusqueda
	 */
	public Destino getOrigenBusqueda() {
		if (origenBusqueda == null) {
			origenBusqueda = new Destino();
		}
		return origenBusqueda;
	}

	/**
	 * @param origenBusqueda
	 *            the origenBusqueda to set
	 */
	public void setOrigenBusqueda(Destino origenBusqueda) {
		this.origenBusqueda = origenBusqueda;
	}

	/**
	 * @return the listaOrigenesBusqueda
	 */
	public List<Destino> getListaOrigenesBusqueda() {
		return listaOrigenesBusqueda;
	}

	/**
	 * @param listaOrigenesBusqueda
	 *            the listaOrigenesBusqueda to set
	 */
	public void setListaOrigenesBusqueda(List<Destino> listaOrigenesBusqueda) {
		this.listaOrigenesBusqueda = listaOrigenesBusqueda;
	}

	/**
	 * @return the pagoServicio
	 */
	public PagoServicio getPagoServicio() {
		if (pagoServicio == null) {
			pagoServicio = new PagoServicio();
		}
		return pagoServicio;
	}

	/**
	 * @param pagoServicio
	 *            the pagoServicio to set
	 */
	public void setPagoServicio(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	/**
	 * @return the listaPagosServicios
	 */
	public List<PagoServicio> getListaPagosServicios() {
		try {
			listaPagosServicios = this.negocioServicio.listarPagosServicio(this.getServicioAgencia().getCodigoEntero());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaPagosServicios;
	}

	/**
	 * @param listaPagosServicios the listaPagosServicios to set
	 */
	public void setListaPagosServicios(List<PagoServicio> listaPagosServicios) {
		this.listaPagosServicios = listaPagosServicios;
	}

	/**
	 * @return the saldoServicio
	 */
	public BigDecimal getSaldoServicio() {
		
		try {
			saldoServicio = this.negocioServicio.consultarSaldoServicio(this.getServicioAgencia().getCodigoEntero());
		} catch (SQLException e) {
			saldoServicio = BigDecimal.ZERO;
			e.printStackTrace();
		} catch (Exception e) {
			saldoServicio = BigDecimal.ZERO;
			e.printStackTrace();
		}
		
		return saldoServicio;
	}

	/**
	 * @param saldoServicio the saldoServicio to set
	 */
	public void setSaldoServicio(BigDecimal saldoServicio) {
		this.saldoServicio = saldoServicio;
	}

	/**
	 * @return the vendedor
	 */
	public boolean isVendedor() {
		return vendedor;
	}

	/**
	 * @param vendedor the vendedor to set
	 */
	public void setVendedor(boolean vendedor) {
		this.vendedor = vendedor;
	}

}
