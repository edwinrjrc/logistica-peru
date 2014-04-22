/**
 * 
 */
package pe.com.logistica.web.servicio.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.ejb.NegocioSessionRemote;
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
	public NegocioServicioImpl(ServletContext context) {
		try {
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
			
			System.out.println("ejb lookup:"+lookup);
			ejbSession = (NegocioSessionRemote) ctx.lookup(lookup);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
	