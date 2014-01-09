package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;

@Remote
public interface SoporteRemote {

	public List<Maestro> listarMaestros() throws SQLException;
	
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException;
	
	public boolean ingresarMaestro(Maestro maestro) throws SQLException;
	
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException;
	
	public Maestro consultarMaestro(int id) throws SQLException;
	
	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException;
	
	public boolean actualizarMaestro(Maestro maestro) throws SQLException;
	
	public List<BaseVO> listarCatalogoMaestro(int maestro) throws SQLException;

	public List<BaseVO> listarCatalogoDepartamento() throws SQLException;
	
	public List<BaseVO> listarCatalogoProvincia(String idDepartamento)
			throws SQLException;
	public List<BaseVO> listarCatalogoDistrito(String idDepartamento,
			String idProvincia) throws SQLException;
}
