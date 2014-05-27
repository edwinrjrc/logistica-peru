package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;

@Remote
public interface SeguridadRemote {

	boolean registrarUsuario(Usuario usuario) throws SQLException;
	List<Usuario> listarUsuarios() throws SQLException;
	List<BaseVO> listarRoles();
	Usuario consultarUsuario(int id) throws SQLException;
	boolean actualizarUsuario(Usuario usuario) throws SQLException;
	Usuario inicioSesion(Usuario usuario) throws SQLException;
}
