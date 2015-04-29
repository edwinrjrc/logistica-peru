package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;
import pe.com.logistica.negocio.exception.ErrorEncriptacionException;

@Local
public interface SeguridadLocal {

	boolean registrarUsuario(Usuario usuario) throws SQLException, ErrorEncriptacionException;
	List<Usuario> listarUsuarios() throws SQLException;
	List<BaseVO> listarRoles();
	Usuario consultarUsuario(int id) throws SQLException;
	boolean actualizarUsuario(Usuario usuario) throws SQLException;
	Usuario inicioSesion(Usuario usuario) throws SQLException, Exception;
	boolean cambiarClaveUsuario(Usuario usuario) throws SQLException, Exception;
	boolean actualizarClaveUsuario(Usuario usuario) throws SQLException, Exception;
	public List<Usuario> listarVendedores() throws SQLException;
}
