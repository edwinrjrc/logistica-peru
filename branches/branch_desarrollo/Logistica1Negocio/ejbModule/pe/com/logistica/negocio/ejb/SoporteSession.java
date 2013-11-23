package pe.com.logistica.negocio.ejb;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;

/**
 * Session Bean implementation class SoporteSession
 */
@Stateless
public class SoporteSession implements SoporteRemote, SoporteLocal {

	MaestroDao maestroDao = null;
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
	public void ingresarMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		maestroDao.ingresarMaestro(maestro);
	}

	@Override
	public void ingresarHijoMaestro(Maestro maestro) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		maestroDao.ingresarHijoMaestro(maestro);
	}

	@Override
	public Maestro consultarMaestro(int id) throws SQLException {
		maestroDao = new MaestroDaoImpl();
		return maestroDao.consultarMaestro(id);
	}
}