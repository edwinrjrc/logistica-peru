/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;

/**
 * @author Edwin
 *
 */
public interface SeguridadServicio {

	
	public boolean registrarUsuario(Usuario usuario);
	public List<Usuario> listarUsuarios();
	public List<BaseVO> listarRoles();
	public Usuario consultarUsuario(int id);
	boolean actualizarUsuario(Usuario usuario);
}
