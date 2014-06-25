/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Usuario;

/**
 * @author Edwin
 *
 */
public interface UsuarioDao {

	
	public boolean registrarUsuario(Usuario usuario) throws SQLException;
	public List<Usuario> listarUsuarios() throws SQLException ;
	Usuario consultarUsuario(int id) throws SQLException;
	boolean actualizarUsuario(Usuario usuario) throws SQLException;
	boolean inicioSesion(Usuario usuario) throws SQLException;
	Usuario inicioSesion2(Usuario usuario) throws SQLException;
	boolean cambiarClaveUsuario(Usuario usuario) throws SQLException;
	
}
