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

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.ejb.SeguridadRemote;
import pe.com.logistica.web.servicio.SeguridadServicio;

/**
 * @author Edwin
 *
 */
public class SeguridadServicioImpl implements SeguridadServicio{
	
	SeguridadRemote ejbSession;
	/**
	 * @throws NamingException 
	 * 
	 */
	public SeguridadServicioImpl() throws NamingException {
		Properties props = new Properties();
        /*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
        props.setProperty("java.naming.provider.url", "localhost:1099"); */
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
		Context ctx = new InitialContext(props);
		//String lookup = "ejb:Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/SeguridadSession!pe.com.logistica.negocio.ejb.SeguridadRemote";
		ejbSession = (SeguridadRemote) ctx.lookup(lookup);
		
	}
	@Override
	public boolean registrarUsuario(Usuario usuario) throws SQLException {
		return ejbSession.registrarUsuario(usuario);
	}
	@Override
	public List<Usuario> listarUsuarios() throws SQLException {
		return ejbSession.listarUsuarios();
	}
	@Override
	public List<BaseVO> listarRoles() {
		return ejbSession.listarRoles();
	}
	@Override
	public Usuario consultarUsuario(int id) throws SQLException {
		return ejbSession.consultarUsuario(id);
	}
	@Override
	public boolean actualizarUsuario(Usuario usuario) throws SQLException {
		return ejbSession.actualizarUsuario(usuario);
	}
}
