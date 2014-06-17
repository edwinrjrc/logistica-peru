/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;

/**
 * @author Edwin
 * 
 */
public interface SoporteServicio {

	public List<Maestro> listarMaestros() throws SQLException;

	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;

	public boolean ingresarMaestro(Maestro maestro) throws SQLException;

	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;

	public Maestro consultarMaestro(int idmaestro) throws SQLException;

	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException;
	
	public boolean actualizarMaestro(Maestro hijo) throws SQLException;

	public List<BaseVO> listarCatalogoMaestro(int idmaestro) throws SQLException;
	
	public List<BaseVO> listarCatalogoDepartamento() throws SQLException;
	
	public List<BaseVO> listarCatalogoProvincia(String idProvincia) throws SQLException;
	
	public List<BaseVO> listarCatalogoDistrito(String idDepartamento, String idProvincia) throws SQLException;
	
	public List<BaseVO> listarContinentes() throws SQLException;

	boolean ingresarPais(Pais pais) throws SQLException, Exception;

	List<BaseVO> consultarPaises(int idcontinente) throws SQLException,
			Exception;
}
