package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Destino;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Pais;

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
	
	public List<BaseVO> listarContinentes() throws SQLException;
	
	public List<BaseVO> consultarPaisesContinente(int idcontinente) throws SQLException, Exception;

	boolean ingresarPais(Pais pais) throws SQLException, Exception;
	
	public boolean ingresarDestino(Destino destino) throws SQLException, Exception;
	
	public boolean actualizarDestino(Destino destino) throws SQLException, Exception;
	
	List<Destino> listarDestinos() throws SQLException, Exception;
}
