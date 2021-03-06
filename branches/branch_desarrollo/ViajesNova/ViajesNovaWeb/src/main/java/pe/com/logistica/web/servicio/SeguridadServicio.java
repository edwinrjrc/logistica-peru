/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorEncriptacionException;

/**
 * @author Edwin
 *
 */
public interface SeguridadServicio {

	
	public boolean registrarUsuario(Usuario usuario) throws SQLException, ErrorEncriptacionException;
	public List<Usuario> listarUsuarios() throws SQLException;
	public List<BaseVO> listarRoles();
	public Usuario consultarUsuario(int id) throws SQLException;
	boolean actualizarUsuario(Usuario usuario) throws SQLException;
	Usuario inicioSesion(Usuario usuario) throws SQLException, Exception;
	boolean cambiarClaveUsuario(Usuario usuario) throws SQLException, Exception;
	boolean actualizarClaveUsuario(Usuario usuario) throws SQLException,
			Exception;
	List<Usuario> listarVendedores() throws SQLException;
}
