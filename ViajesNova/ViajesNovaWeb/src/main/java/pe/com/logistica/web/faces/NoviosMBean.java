/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ValidacionException;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.ParametroServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;
import pe.com.logistica.web.servicio.impl.ParametroServicioImpl;

/**
 * @author edwreb
 * 
 */
@ManagedBean(name = "noviosMBean")
@SessionScoped()
public class NoviosMBean extends BaseMBean {

	private final static Logger logger = Logger.getLogger(NoviosMBean.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2578137810881699743L;

	private ProgramaNovios programaNovios;
	private ProgramaNovios programaNoviosBusqueda;
	private Cliente clienteBusqueda;
	private ServicioNovios servicioNovios;

	private int tipoBusqueda;

	private boolean nuevoNovios;
	private boolean registroExito;

	private List<ProgramaNovios> listadoNovios;
	private List<ServicioNovios> listadoServicios;
	private List<Cliente> listadoClientes;
	private List<Cliente> listadoInvitados;

	private String generoCliente;

	// private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;
	private ParametroServicio parametroServicio;

	/**
	 * 
	 */
	public NoviosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			// soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
			parametroServicio = new ParametroServicioImpl(servletContext);
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void buscarNovios() {

	}

	public void registrarNovios() {
		this.setProgramaNovios(null);
		this.setNuevoNovios(true);
		this.setShowModal(false);
		this.setRegistroExito(false);
	}

	public void consultaClientes(String genero, long busqueda) {
		try {
			this.setClienteBusqueda(null);
			this.setGeneroCliente(genero);
			this.setTipoBusqueda((int) busqueda);

			this.setListadoClientes(this.negocioServicio
					.listarClientesNovios(genero));
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void consultaDestinos() {

	}

	public void buscarClientes() {

	}

	public void ejecutarMetodo(ActionEvent e) {

		try {
			if (this.isNuevoNovios()) {
				if (validarNuevoNovios()) {

					getProgramaNovios()
							.setListaInvitados(getListadoInvitados());
					getProgramaNovios().setListaServicios(getListadoServicios());
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getProgramaNovios()
							.setUsuarioCreacion(usuario.getUsuario());
					getProgramaNovios().setIpCreacion(
							obtenerRequest().getRemoteAddr());

					for (Cliente cliente : this.getProgramaNovios()
							.getListaInvitados()) {

						cliente.setUsuarioCreacion(usuario.getUsuario());
						cliente.setIpCreacion(obtenerRequest().getRemoteAddr());
					}

					Integer idnovios = negocioServicio
							.registrarNovios(getProgramaNovios());
					this.setRegistroExito(idnovios != null
							&& idnovios.intValue() != 0);

					getProgramaNovios().setCodigoEntero(idnovios);
					List<ProgramaNovios> resultadoConsulta = this.negocioServicio
							.consultarNovios(programaNovios);
					if (resultadoConsulta != null
							&& !resultadoConsulta.isEmpty()) {
						this.setProgramaNovios(resultadoConsulta.get(0));
					}

					this.setShowModal(true);
					this.setMensajeModal("Novios registrados satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
			}
		} catch (ValidacionException ex){
			this.setShowModal(true);
			this.setMensajeModal(ex.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			this.setShowModal(true);
			this.setMensajeModal(ex.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			logger.error(ex.getMessage(), ex);
		}

	}

	private boolean validarNuevoNovios() throws ValidacionException {
		boolean resultado = true;
		String idFormulario = "idFormNovios";
		if (this.getProgramaNovios().getNovia() == null
				|| this.getProgramaNovios().getNovia().getCodigoEntero() == null
				|| this.getProgramaNovios().getNovia().getCodigoEntero()
						.intValue() == 0
				|| StringUtils.isBlank(this.getProgramaNovios().getNovia()
						.getNombreCompleto())) {
			this.agregarMensaje(idFormulario + ":idFrNovia",
					"Seleccione a la novia", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getNovio() == null
				|| this.getProgramaNovios().getNovio().getCodigoEntero() == null
				|| this.getProgramaNovios().getNovio().getCodigoEntero()
						.intValue() == 0
				|| StringUtils.isBlank(this.getProgramaNovios().getNovio()
						.getNombreCompleto())) {
			this.agregarMensaje(idFormulario + ":idFrNovio",
					"Seleccione a la novio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getDestino() == null
				|| this.getProgramaNovios().getDestino().getCodigoEntero() == null
				|| this.getProgramaNovios().getDestino().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelDestino",
					"Seleccione el destino", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaBoda() == null) {
			this.agregarMensaje(idFormulario + ":idFecBoda",
					"Seleccione la fecha de la Boda", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaViaje() == null) {
			this.agregarMensaje(idFormulario + ":idFecViaje",
					"Seleccione la fecha de Viaje", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaShower() == null) {
			this.agregarMensaje(idFormulario + ":idFecShower",
					"Seleccione la fecha del Shower", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getMoneda() == null
				|| this.getProgramaNovios().getMoneda().getCodigoEntero()
						.intValue() == 0) {
			this.agregarMensaje(idFormulario + ":idSelMoneda",
					"Seleccione la moneda de la cuota inicial", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getCuotaInicial() == null
				|| this.getProgramaNovios().getCuotaInicial()
						.equals(BigDecimal.ZERO)) {
			this.agregarMensaje(idFormulario + ":idCuotaInicial",
					"Ingrese el monto de la cuota inicial", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaBoda().after(this.getProgramaNovios().getFechaViaje())){
			throw new ValidacionException("La fecha de la boda no puede ser mayor a la fecha del viaje");
		}
		if (this.getProgramaNovios().getFechaViaje().before(this.getProgramaNovios().getFechaShower())){
			throw new ValidacionException("La fecha del viaje no puede ser mayor a la fecha del Shower");
		}
		if (this.getProgramaNovios().getFechaShower().before(this.getProgramaNovios().getFechaBoda())){
			throw new ValidacionException("La fecha del Shower no puede ser mayor a la fecha de la boda");
		}
		return resultado;
	}

	public void seleccionarNovio() {
		if (this.getTipoBusqueda() == 1) {
			if ("F".equals(this.getGeneroCliente())) {
				this.getProgramaNoviosBusqueda().setNovia(
						obtenerClienteListado());
			} else {
				this.getProgramaNoviosBusqueda().setNovio(
						obtenerClienteListado());
			}
		} else {
			if ("F".equals(this.getGeneroCliente())) {
				this.getProgramaNovios().setNovia(obtenerClienteListado());
			} else {
				this.getProgramaNovios().setNovio(obtenerClienteListado());
			}
		}
	}

	public void imprimirFormatoNovios() {
		String rutaJasper = "/../resources/jasper/report2.jasper";

		try {
			HttpServletResponse response = obtenerResponse();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-disposition",
					"attachment;filename=registroNovios.pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");

			FacesContext facesContext = obtenerContexto();
			rutaJasper = obtenerRequest().getContextPath() + rutaJasper;
			InputStream jasperStream = facesContext.getExternalContext()
					.getResourceAsStream(rutaJasper);

			OutputStream stream = response.getOutputStream();

			jasperStream = facesContext.getExternalContext()
					.getResourceAsStream(rutaJasper);

			//imprimirPDF(enviarParametros(), stream, jasperStream);

			facesContext.responseComplete();

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Map<String, Object> enviarParametros() {
		Map<String, Object> parametros = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			parametros.put("P_CODIGONOVIOS", getProgramaNovios()
					.getCodigoNovios());
			parametros.put("P_NOVIA", getProgramaNovios().getNovia()
					.getNombreCompleto());
			parametros.put("P_NOVIO", getProgramaNovios().getNovio()
					.getNombreCompleto());
			parametros.put("P_FECHABODA",
					sdf.format(getProgramaNovios().getFechaBoda()));
			parametros.put("P_DESTINO", getProgramaNovios().getDestino()
					.getDescripcion());
			parametros.put("P_PAIS", getProgramaNovios().getDestino().getPais()
					.getDescripcion());
			parametros.put("P_FECHAIDA",
					sdf.format(getProgramaNovios().getFechaViaje()));
			parametros.put("P_FECHAINSCRIPCION",
					sdf.format(getProgramaNovios().getFechaCreacion()));
			parametros.put("P_CUOTAINICIAL", getProgramaNovios()
					.getCuotaInicial().toString());
			parametros.put("P_NROINVITADOS", String.valueOf(getProgramaNovios()
					.getCantidadInvitados()));
		} catch (Exception e) {
			parametros.put("P_CODIGONOVIOS", "");
			parametros.put("P_NOVIA", "");
			parametros.put("P_NOVIO", "");
			parametros.put("P_FECHABODA", "");
			parametros.put("P_DESTINO", "");
			parametros.put("P_PAIS", "");
			parametros.put("P_FECHAIDA", "");
			parametros.put("P_FECHAINSCRIPCION", "");
			parametros.put("P_CUOTAINICIAL", "");
			parametros.put("P_NROINVITADOS", "");
			logger.error(e.getMessage(), e);
		}

		return parametros;
	}

	public void agregarInvitado() {
		this.getListadoInvitados().add(new Cliente());
	}

	public void eliminarInvitado(Cliente invitado) {
		this.getListadoInvitados().remove(invitado);
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

	/*private void imprimirPDF(Map<String, Object> map,
			OutputStream outputStream, InputStream jasperStream)
			throws JRException {

		JasperPrint print = JasperFillManager.fillReport(jasperStream, map);

		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		printList.add(print);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setExporterInput(SimpleExporterInput
				.getInstance(printList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setCreatingBatchModeBookmarks(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}*/
	
	public void agregarServicio(){
		try {
			if (validarServicioAgregar()){
				HttpSession session = obtenerSession(false);
				Usuario usuario = (Usuario) session
						.getAttribute("usuarioSession");
				getServicioNovios()
						.setUsuarioCreacion(usuario.getUsuario());
				getServicioNovios().setIpCreacion(
						obtenerRequest().getRemoteAddr());
				
				this.getListadoServicios().add(this.negocioServicio.agregarServicioNovios(getServicioNovios()));
				this.setServicioNovios(null);
				
				calcularTotales();
			}
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
	
	public void eliminarServicio(ServicioNovios servicioNovios){
		try {
			this.getListadoServicios().remove(servicioNovios);
				
			calcularTotales();
		} catch (Exception e) {
			this.setShowModal(true);
			this.setMensajeModal(e.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			logger.error(e.getMessage(), e);
		}
	}

	private void calcularTotales() {
		BigDecimal montoSubtotal = BigDecimal.ZERO;
		BigDecimal montoIgv = BigDecimal.ZERO;
		BigDecimal porcenIgv = BigDecimal.ZERO;
		BigDecimal montoTotal = BigDecimal.ZERO;
		try {
			String valorIGV = "0";
			try {
				Parametro paramIGV = parametroServicio.consultarParametro(1);
				valorIGV = paramIGV.getValor();
			} catch (SQLException e) {
				valorIGV = "0";
				e.printStackTrace();
			}
			for (ServicioNovios servicioNovios : this.getListadoServicios()){
				montoTotal = montoTotal.add(servicioNovios.getMontoTotalTipoServicio());
			}
			porcenIgv = BigDecimal.valueOf(Double.valueOf(valorIGV));
			BigDecimal decimalIGVmas1 = porcenIgv.add(BigDecimal.ONE);
			montoSubtotal = montoTotal.divide(decimalIGVmas1, 2, RoundingMode.DOWN);
			montoIgv = montoSubtotal.multiply(porcenIgv);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			montoSubtotal = BigDecimal.ZERO;
			montoIgv = BigDecimal.ZERO;
			montoTotal = BigDecimal.ZERO;
		}
		this.getProgramaNovios().setMontoSinIgvServiciosPrograma(montoSubtotal);
		this.getProgramaNovios().setMontoIgvServiciosPrograma(montoIgv);
		this.getProgramaNovios().setPorcentajeIgv(porcenIgv);
		this.getProgramaNovios().setMontoTotalServiciosPrograma(montoTotal);
	}

	private boolean validarServicioAgregar() {
		boolean resultado = true;
		String idFormulario = "idFormNovios";
		if (this.getServicioNovios().getTipoServicio().getCodigoEntero() == null || this.getServicioNovios().getTipoServicio().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelTipoServicio",
					"Seleccione el tipo de servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (StringUtils.isBlank(this.getServicioNovios().getDescripcionServicio())){
			this.agregarMensaje(idFormulario + ":idDescServicio",
					"Ingrese la descripcion del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioNovios().getCantidad() == 0){
			this.agregarMensaje(idFormulario + ":idCantidad",
					"Ingrese la cantidad", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getServicioNovios().getPrecioUnitario() == null || this.getServicioNovios().getPrecioUnitario().doubleValue() == 0.0){
			this.agregarMensaje(idFormulario + ":idPrecUnitario",
					"Ingrese el precio unitario del servicio", "", FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		return resultado;
	}

	/**
	 * @return the programaNovios
	 */
	public ProgramaNovios getProgramaNovios() {
		if (programaNovios == null) {
			programaNovios = new ProgramaNovios();
		}
		return programaNovios;
	}

	/**
	 * @param programaNovios
	 *            the programaNovios to set
	 */
	public void setProgramaNovios(ProgramaNovios programaNovios) {
		this.programaNovios = programaNovios;
	}

	/**
	 * @return the listadoNovios
	 */
	public List<ProgramaNovios> getListadoNovios() {
		try {
			listadoNovios = this.negocioServicio
			.consultarNovios(getProgramaNoviosBusqueda());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return listadoNovios;
	}

	/**
	 * @param listadoNovios
	 *            the listadoNovios to set
	 */
	public void setListadoNovios(List<ProgramaNovios> listadoNovios) {
		this.listadoNovios = listadoNovios;
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
		if (listadoClientes == null) {
			listadoClientes = new ArrayList<Cliente>();
		}
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
	 * @return the programaNoviosBusqueda
	 */
	public ProgramaNovios getProgramaNoviosBusqueda() {
		if (programaNoviosBusqueda == null) {
			programaNoviosBusqueda = new ProgramaNovios();
		}
		return programaNoviosBusqueda;
	}

	/**
	 * @param programaNoviosBusqueda
	 *            the programaNoviosBusqueda to set
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
	 * @param generoCliente
	 *            the generoCliente to set
	 */
	public void setGeneroCliente(String generoCliente) {
		this.generoCliente = generoCliente;
	}

	/**
	 * @return the tipoBusqueda
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda
	 *            the tipoBusqueda to set
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	/**
	 * @return the listadoInvitados
	 */
	public List<Cliente> getListadoInvitados() {
		if (listadoInvitados == null) {
			listadoInvitados = new ArrayList<Cliente>();
		}
		return listadoInvitados;
	}

	/**
	 * @param listadoInvitados
	 *            the listadoInvitados to set
	 */
	public void setListadoInvitados(List<Cliente> listadoInvitados) {
		this.listadoInvitados = listadoInvitados;
	}

	/**
	 * @return the nuevoNovios
	 */
	public boolean isNuevoNovios() {
		return nuevoNovios;
	}

	/**
	 * @param nuevoNovios
	 *            the nuevoNovios to set
	 */
	public void setNuevoNovios(boolean nuevoNovios) {
		this.nuevoNovios = nuevoNovios;
	}

	/**
	 * @return the registroExito
	 */
	public boolean isRegistroExito() {
		return registroExito;
	}

	/**
	 * @param registroExito
	 *            the registroExito to set
	 */
	public void setRegistroExito(boolean registroExito) {
		this.registroExito = registroExito;
	}

	/**
	 * @return the listadoServicios
	 */
	public List<ServicioNovios> getListadoServicios() {
		if (listadoServicios == null){
			listadoServicios = new ArrayList<ServicioNovios>();
		}
		
		return listadoServicios;
	}

	/**
	 * @param listadoServicios the listadoServicios to set
	 */
	public void setListadoServicios(List<ServicioNovios> listadoServicios) {
		this.listadoServicios = listadoServicios;
	}

	/**
	 * @return the servicioNovios
	 */
	public ServicioNovios getServicioNovios() {
		if (servicioNovios == null){
			servicioNovios = new ServicioNovios();
		}
		return servicioNovios;
	}

	/**
	 * @param servicioNovios the servicioNovios to set
	 */
	public void setServicioNovios(ServicioNovios servicioNovios) {
		this.servicioNovios = servicioNovios;
	}

}
