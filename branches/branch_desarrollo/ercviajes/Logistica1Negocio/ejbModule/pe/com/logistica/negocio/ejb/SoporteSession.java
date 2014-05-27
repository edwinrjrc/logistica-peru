package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.negocio.dao.CatalogoDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.impl.CatalogoDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;

/**
 * Session Bean implementation class SoporteSession
 */
@Stateless
public class SoporteSession implements SoporteRemote, SoporteLocal {

	MaestroDao maestroDao = null;
	CatalogoDao catalogoDao = null;
    /**
     * Default constructor. 
     */
    public SoporteSession() {
    	
    }

	@Override
	public List<Maestro> listarMaestros() throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.listarMaestros();
	}

	@Override
	public List<Maestro> listarHijosMaestro(int idmaestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.listarHijosMaestro(idmaestro);
	}

	@Override
	public boolean ingresarMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.ingresarMaestro(maestro);
	}
	
	@Override
	public boolean ingresarHijoMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.ingresarHijoMaestro(maestro);
	}

	@Override
	public Maestro consultarMaestro(int id) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.consultarMaestro(id);
	}
	
	@Override
	public Maestro consultarHijoMaestro(Maestro hijo) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.consultarHijoMaestro(hijo);
	}
	
	@Override
	public boolean actualizarMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.actualizarMaestro(maestro);
	}

	@Override
	public List<BaseVO> listarCatalogoMaestro(int maestro) throws SQLException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listarCatalogoMaestro(maestro);
	}
	
	@Override
	public List<BaseVO> listarCatalogoDepartamento() throws SQLException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaDepartamento();
	}
	
	@Override
	public List<BaseVO> listarCatalogoProvincia(String idDepartamento) throws SQLException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaProvincia(idDepartamento);
	}
	
	@Override
	public List<BaseVO> listarCatalogoDistrito(String idDepartamento, String idProvincia) throws SQLException {
		catalogoDao = new CatalogoDaoImpl();
		return catalogoDao.listaDistrito(idDepartamento, idProvincia);
	}
}
