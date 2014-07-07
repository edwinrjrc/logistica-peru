/**
 * 
 */
package pe.com.logistica.web.servicio.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CronogramaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.negocio.ejb.NegocioSessionRemote;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.web.servicio.NegocioServicio;

/**
 * @author Edwin
 *
 */
public class NegocioServicioImpl implements NegocioServicio {

	NegocioSessionRemote ejbSession;
	
	final String ejbBeanName = "NegocioSession";
	/**
	 * 
	 */
	public NegocioServicioImpl(ServletContext context) throws NamingException{
		
		Properties props = new Properties();
		/*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099"); */
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/NegocioSession!pe.com.logistica.negocio.ejb.NegocioSessionRemote";
		
		final String ejbRemoto = NegocioSessionRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (NegocioSessionRemote) ctx.lookup(lookup);
		
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.NegocioServicio#agregarDireccion(pe.com.logistica.bean.base.Direccion)
	 */
	@Override
	public Direccion agregarDireccion(Direccion direccion) throws SQLException, Exception {
		return ejbSession.agregarDireccion(direccion);
	}
	
	
	@Override
	public Contacto agregarContacto (Contacto contacto) throws SQLException, Exception {
		return ejbSession.agregarContacto(contacto);
	}

	@Override
	public boolean registrarProveedor(Proveedor proveedor) throws SQLException, Exception {
		return ejbSession.registrarProveedor(proveedor);
	}
	
	@Override
	public List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException, Exception {
		return ejbSession.listarProveedor(proveedor);
	}

	@Override
	public Proveedor consultarProveedorCompleto(int codigoProveedor) throws SQLException, Exception {
		return ejbSession.consultarProveedor(codigoProveedor);
	}

	@Override
	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception {
		return ejbSession.actualizarProveedor(proveedor);
	}

	@Override
	public List<pe.com.logistica.bean.negocio.Proveedor> buscarProveedor(
			Proveedor proveedor)
			throws SQLException, Exception {
		return ejbSession.buscarProveedor(proveedor);
	}

	@Override
	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception {
		return ejbSession.registrarCliente(cliente);
	}

	@Override
	public boolean actualizarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception {
		return ejbSession.actualizarCliente(cliente);
	}

	@Override
	public List<Cliente> buscarCliente(Cliente cliente) throws SQLException{
		return ejbSession.buscarCliente(cliente);
	}

	@Override
	public Cliente consultarClienteCompleto(int idcliente) throws SQLException, Exception {
		return ejbSession.consultarCliente(idcliente);
	}

	@Override
	public List<Cliente> listarCliente() throws SQLException {
		return ejbSession.listarCliente();
	}

	@Override
	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		return ejbSession.registrarNovios(programaNovios);
	}
	
	@Override
	public List<Cliente> listarClientesNovios(String genero)
			throws SQLException, Exception {
		return ejbSession.listarClientesNovios(genero);
	}
	
	@Override
	public List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		return ejbSession.consultarNovios(programaNovios);
	}
	
	@Override
	public ServicioNovios agregarServicioNovios(ServicioNovios servicioNovios)
			throws SQLException, Exception {
		return ejbSession.agregarServicio(servicioNovios);
	}
	
	@Override
	public DetalleServicioAgencia agregarServicioVenta(DetalleServicioAgencia detalleServicio)
			throws SQLException, Exception {
		return ejbSession.agregarServicioVenta(detalleServicio);
	}
	
	@Override
	public List<DetalleServicioAgencia> ordenarServiciosVenta(List<DetalleServicioAgencia> listaServicio)
			throws SQLException, Exception {
		return ejbSession.ordenarServiciosVenta(listaServicio);
	}

	@Override
	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia) throws SQLException, Exception {
		return ejbSession.calcularValorCuota(servicioAgencia);
	}

	@Override
	public List<CronogramaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia) throws SQLException, Exception {
		return ejbSession.consultarCronograma(servicioAgencia);
	}
	
	@Override
	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia) throws ErrorRegistroDataException, SQLException, Exception {
		return ejbSession.registrarVentaServicio(servicioAgencia);
	}
	
	@Override
	public List<ServicioAgencia> consultarVentaServicio(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		return ejbSession.consultarServicioVenta(servicioAgencia);
	}
}
	