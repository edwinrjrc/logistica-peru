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

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;
import pe.com.logistica.negocio.ejb.SoporteRemote;
import pe.com.logistica.web.servicio.SoporteServicio;

/**
 * @author Edwin
 *
 */
public class SoporteServicioImpl implements SoporteServicio {

	SoporteRemote ejbSession;
	final String ejbBeanName = "SoporteSession";
	/**
	 * @param servletContext 
	 * @throws NamingException 
	 * 
	 */
	public SoporteServicioImpl(ServletContext context) throws NamingException {
		Properties props = new Properties();
        /*props.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
        props.setProperty("java.naming.provider.url", "localhost:1099"); */
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        
		Context ctx = new InitialContext(props);
		String lookup = "java:jboss/exported/Logistica1EAR/Logistica1Negocio/SoporteSession!pe.com.logistica.negocio.ejb.SoporteRemote";
		final String ejbRemoto = SoporteRemote.class.getName();
		lookup = "java:jboss/exported/"+context.getInitParameter("appNegocioNameEar")+"/"+context.getInitParameter("appNegocioName")+"/"+ejbBeanName+"!"+ejbRemoto;
		
		ejbSession = (SoporteRemote) ctx.lookup(lookup);
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.SoporteServicio#listarMaestros()
	 */
	@Override
	public List<Maestro> listarMaestros() throws SQLException {
		return ejbSession.listarMaestros();
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.SoporteServicio#listarHijosMaestro(int)
	 */
	@Override
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException {
		return ejbSession.listarHijosMaestro(idmaestro);
	}
	
	@Override
	public Maestro consultarMaestro(int idmaestro) throws SQLException {
		return ejbSession.consultarMaestro(idmaestro);
	}
	
	@Override
	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException {
		return ejbSession.consultarHijoMaestro(hijo);
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.SoporteServicio#ingresarMaestro(pe.com.logistica.bean.negocio.Maestro)
	 */
	@Override
	public boolean ingresarMaestro(Maestro maestro) throws SQLException {
		return ejbSession.ingresarMaestro(maestro);

	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.web.servicio.SoporteServicio#ingresarHijoMaestro(pe.com.logistica.bean.negocio.Maestro)
	 */
	@Override
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException {
		return ejbSession.ingresarHijoMaestro(maestro);
	}

	@Override
	public boolean actualizarMaestro(Maestro maestro) throws SQLException {
		return ejbSession.actualizarMaestro(maestro);
	}

	@Override
	public List<BaseVO> listarCatalogoMaestro(int idmaestro) throws SQLException {
		return ejbSession.listarCatalogoMaestro(idmaestro);
	}

	@Override
	public List<BaseVO> listarCatalogoDepartamento() throws SQLException {
		return ejbSession.listarCatalogoDepartamento();
	}

	@Override
	public List<BaseVO> listarCatalogoProvincia(String idProvincia)
			throws SQLException {
		return ejbSession.listarCatalogoProvincia(idProvincia);
	}

	@Override
	public List<BaseVO> listarCatalogoDistrito(String idDepartamento,
			String idProvincia) throws SQLException {
		return ejbSession.listarCatalogoDistrito(idDepartamento, idProvincia);
	}

	@Override
	public List<BaseVO> listarContinentes() throws SQLException {
		return ejbSession.listarContinentes();
	}
	
	@Override
	public List<BaseVO> consultarPaises(int idcontinente) throws SQLException, Exception {
		return ejbSession.consultarPaisesContinente(idcontinente);
	}
	
	@Override
	public boolean ingresarPais(Pais pais) throws SQLException, Exception {
		return ejbSession.ingresarPais(pais);
	}
}
