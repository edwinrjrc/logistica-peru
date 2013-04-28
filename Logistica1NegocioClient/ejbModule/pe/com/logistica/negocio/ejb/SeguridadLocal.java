package pe.com.logistica.negocio.ejb;

import java.util.List;

import javax.ejb.Local;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Usuario;

@Local
public interface SeguridadLocal {

	boolean registrarUsuario(Usuario usuario);
	List<Usuario> listarUsuarios();
	List<BaseVO> listarRoles();
	Usuario consultarUsuario(int id);
	boolean actualizarUsuario(Usuario usuario);
}
