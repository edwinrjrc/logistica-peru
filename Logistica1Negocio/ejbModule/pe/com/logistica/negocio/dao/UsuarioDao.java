/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.util.List;

import pe.com.logistica.bean.negocio.Usuario;

/**
 * @author Edwin
 *
 */
public interface UsuarioDao {

	
	public boolean registrarUsuario(Usuario usuario);
	public List<Usuario> listarUsuarios();
	Usuario consultarUsuario(int id);
	boolean actualizarUsuario(Usuario usuario);
	
}
