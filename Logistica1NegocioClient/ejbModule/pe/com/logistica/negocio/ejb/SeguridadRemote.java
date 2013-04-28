package pe.com.logistica.negocio.ejb;

import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;

@Remote
public interface SeguridadRemote {

	boolean registrarUsuario(Usuario usuario);
	List<Usuario> listarUsuarios();
	List<BaseVO> listarRoles();
	Usuario consultarUsuario(int id);
	boolean actualizarUsuario(Usuario usuario);
}
