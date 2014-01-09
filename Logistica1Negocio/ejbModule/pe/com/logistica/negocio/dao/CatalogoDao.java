/**
 * 
 */
package pe.com.logistica.negocio.dao;

import java.util.List;

import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public interface CatalogoDao {

	public List<BaseVO> listarRoles();
	
	public List<BaseVO> listarCatalogoMaestro(int maestro);
	
	public List<BaseVO> listaDepartamento();
	
	public List<BaseVO> listaProvincia(String idDepartamento);
	
	public List<BaseVO> listaDistrito(String idDepartamento, String idProvincia);
}
