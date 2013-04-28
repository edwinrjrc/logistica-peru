package pe.com.logistica.negocio.ejb;

import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.dao.CatalogoDao;
import pe.com.logistica.negocio.dao.UsuarioDao;
import pe.com.logistica.negocio.dao.impl.CatalogoDaoImpl;
import pe.com.logistica.negocio.dao.impl.UsuarioDaoImpl;

/**
 * Session Bean implementation class Seguridad
 */
@Stateless(name="SeguridadSession")
public class Seguridad implements SeguridadRemote, SeguridadLocal {

	UsuarioDao usuarioDao = null;
	CatalogoDao catalogoDao = null;
    /**
     * Default constructor. 
     */
    public Seguridad() {
    	
    }
    
    @Override
    public boolean registrarUsuario(Usuario usuario){
    	usuarioDao = new UsuarioDaoImpl();
    	return usuarioDao.registrarUsuario(usuario);
    }

	@Override
	public List<Usuario> listarUsuarios() {
		usuarioDao = new UsuarioDaoImpl();
		return usuarioDao.listarUsuarios();
	}

	@Override
	public List<BaseVO> listarRoles() {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listarRoles();
	}

	@Override
	public Usuario consultarUsuario(int id) {
		return usuarioDao.consultarUsuario(id);
	}

	@Override
	public boolean actualizarUsuario(Usuario usuario) {
		usuarioDao = new UsuarioDaoImpl();
    	return usuarioDao.actualizarUsuario(usuario);
	}
}
