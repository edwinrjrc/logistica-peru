/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.web.servicio.NegocioServicio;
import pe.com.logistica.web.servicio.impl.NegocioServicioImpl;

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
	
	private int tipoBusqueda;
	
	private boolean nuevoNovios;
	private boolean registroExito;
	
	private List<ProgramaNovios> listadoNovios;
	private List<Cliente> listadoClientes;
	private List<Cliente> listadoInvitados;
	
	private String generoCliente;
	
	//private SoporteServicio soporteServicio;
	private NegocioServicio negocioServicio;
	/**
	 * 
	 */
	public NoviosMBean() {
		try {
			ServletContext servletContext = (ServletContext) FacesContext
					.getCurrentInstance().getExternalContext().getContext();
			//soporteServicio = new SoporteServicioImpl(servletContext);
			negocioServicio = new NegocioServicioImpl(servletContext);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	public void buscarNovios(){
		
	}
	
	public void registrarNovios(){
		this.setProgramaNovios(null);
		this.setNuevoNovios(true);
		this.setShowModal(false);
		this.setRegistroExito(false);
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
	
	public void ejecutarMetodo(ActionEvent e){
		
		try {
			if (this.isNuevoNovios()){
				if (validarNuevoNovios()){
					
					getProgramaNovios().setListaInvitados(getListadoInvitados());
					HttpSession session = obtenerSession(false);
					Usuario usuario = (Usuario) session
							.getAttribute("usuarioSession");
					getProgramaNovios().setUsuarioCreacion(
							usuario.getUsuario());
					getProgramaNovios().setIpCreacion(
							obtenerRequest().getRemoteAddr());
					
					Integer idnovios = negocioServicio.registrarNovios(getProgramaNovios());
					this.setRegistroExito(idnovios!=null && idnovios.intValue()!=0);
					
					getProgramaNovios().setCodigoEntero(idnovios);
					List<ProgramaNovios> resultadoConsulta = this.negocioServicio.consultarNovios(programaNovios);
					if (resultadoConsulta!=null && !resultadoConsulta.isEmpty()){
						this.setProgramaNovios(resultadoConsulta.get(0));
					}
					
					this.setShowModal(true);
					this.setMensajeModal("Novios registrados satisfactoriamente");
					this.setTipoModal(TIPO_MODAL_EXITO);
				}
			}
		} catch (Exception e1) {
			this.setShowModal(true);
			this.setMensajeModal(e1.getMessage());
			this.setTipoModal(TIPO_MODAL_ERROR);
			e1.printStackTrace();
		}
		
	}
	
	private boolean validarNuevoNovios() {
		boolean resultado = true;
		String idFormulario = "idFormNovios";
		if (this.getProgramaNovios().getNovia() == null || this.getProgramaNovios().getNovia().getCodigoEntero()==null || this.getProgramaNovios().getNovia().getCodigoEntero().intValue()==0 || StringUtils.isBlank(this.getProgramaNovios().getNovia().getNombreCompleto())){
			this.agregarMensaje(idFormulario + ":idFrNovia",
					"Seleccione a la novia", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getNovio() == null || this.getProgramaNovios().getNovio().getCodigoEntero()==null || this.getProgramaNovios().getNovio().getCodigoEntero().intValue()==0 || StringUtils.isBlank(this.getProgramaNovios().getNovio().getNombreCompleto())){
			this.agregarMensaje(idFormulario + ":idFrNovio",
					"Seleccione a la novio", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getDestino() == null || this.getProgramaNovios().getDestino().getCodigoEntero()==null || this.getProgramaNovios().getDestino().getCodigoEntero().intValue()==0){
			this.agregarMensaje(idFormulario + ":idSelDestino",
					"Seleccione el destino", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaBoda() == null){
			this.agregarMensaje(idFormulario + ":idFecBoda",
					"Seleccione la fecha de la Boda", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaViaje() == null){
			this.agregarMensaje(idFormulario + ":idFecViaje",
					"Seleccione la fecha de Viaje", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getFechaShower() == null){
			this.agregarMensaje(idFormulario + ":idFecShower",
					"Seleccione la fecha del Shower", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getMoneda() == null || this.getProgramaNovios().getMoneda().getCodigoEntero().intValue() == 0){
			this.agregarMensaje(idFormulario + ":idSelMoneda",
					"Seleccione la moneda de la cuota inicial", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		if (this.getProgramaNovios().getCuotaInicial()== null || this.getProgramaNovios().getCuotaInicial().equals(BigDecimal.ZERO)){
			this.agregarMensaje(idFormulario + ":idCuotaInicial",
					"Ingrese el monto de la cuota inicial", "",
					FacesMessage.SEVERITY_ERROR);
			resultado = false;
		}
		return resultado;
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
	
	public void imprimirFormatoNovios(){
		String rutaJasper = "../resources/jasper/report2.jasper";
		
		try {
			HttpServletResponse response = obtenerResponse();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-disposition",
					"attachment;filename=registroNovios.pdf");
			response.setHeader("Content-Transfer-Encoding", "binary");

			FacesContext facesContext = obtenerContexto();
			InputStream jasperStream = facesContext.getExternalContext()
					.getResourceAsStream(rutaJasper);

			OutputStream stream = response.getOutputStream();

			jasperStream = obtenerContexto().getExternalContext()
					.getResourceAsStream(rutaJasper);
			
			imprimirPDF(enviarParametros(),stream,jasperStream);
			
			obtenerContexto().responseComplete();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private Map<String, Object> enviarParametros() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			parametros.put("P_CODIGONOVIOS", getProgramaNovios().getCodigoNovios());
			parametros.put("P_NOVIA", getProgramaNovios().getNovia().getNombreCompleto());
			parametros.put("P_NOVIO", getProgramaNovios().getNovio().getNombreCompleto());
			parametros.put("P_FECHABODA", sdf.format(getProgramaNovios().getFechaBoda()));
			parametros.put("P_DESTINO", getProgramaNovios().getDestino().getDescripcion());
			parametros.put("P_PAIS", getProgramaNovios().getDestino().getPais().getDescripcion());
			parametros.put("P_FECHAIDA", sdf.format(getProgramaNovios().getFechaViaje()));
			parametros.put("P_FECHAINSCRIPCION", sdf.format(getProgramaNovios().getFechaCreacion()));
			parametros.put("P_CUOTAINICIAL", getProgramaNovios().getCuotaInicial());
			parametros.put("P_NROINVITADOS", getProgramaNovios().getCantidadInvitados());
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
		}
		
		return parametros;
	}


	public void agregarInvitado(){
		this.getListadoInvitados().add(new Cliente());
	}
	public void eliminarInvitado(Cliente invitado){
		this.getListadoInvitados().remove(invitado);
	}
	
	private Cliente obtenerClienteListado(){
		try {
			for (Cliente clienteLocal : this.getListadoClientes()){
				if (clienteLocal.getCodigoEntero().equals(clienteLocal.getCodigoSeleccionado())){
					return clienteLocal;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void imprimirPDF(Map<String, Object> map, OutputStream outputStream,
			InputStream jasperStream) throws JRException {

		JasperPrint print = JasperFillManager.fillReport(jasperStream, map);

		List<JasperPrint> printList = new ArrayList<JasperPrint>();
		printList.add(print);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, printList);
		exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,
				outputStream);
		exporter.exportReport();
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


	/**
	 * @return the listadoInvitados
	 */
	public List<Cliente> getListadoInvitados() {
		if (listadoInvitados == null){
			listadoInvitados = new ArrayList<Cliente>();
		}
		return listadoInvitados;
	}


	/**
	 * @param listadoInvitados the listadoInvitados to set
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
	 * @param nuevoNovios the nuevoNovios to set
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
	 * @param registroExito the registroExito to set
	 */
	public void setRegistroExito(boolean registroExito) {
		this.registroExito = registroExito;
	}


}
