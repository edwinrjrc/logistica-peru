package pe.com.logistica.negocio.ejb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.PersonaDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.dao.UbigeoDao;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.PersonaDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.UbigeoDaoImpl;
import pe.com.logistica.negocio.util.UtilConexion;

/**
 * Session Bean implementation class NegocioSession
 */
@Stateless(name = "NegocioSession")
public class NegocioSession implements NegocioSessionRemote,
		NegocioSessionLocal {

	/**
	 * Default constructor.
	 */
	public NegocioSession() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Direccion agregarDireccion(Direccion direccion) throws SQLException, Exception {
		String iddepartamento = direccion.getUbigeo().getDepartamento()
				.getCodigoCadena();
		String idprovincia = direccion.getUbigeo().getProvincia()
				.getCodigoCadena();
		String iddistrito = direccion.getUbigeo().getDistrito()
				.getCodigoCadena();
		UbigeoDao ubigeoDao = new UbigeoDaoImpl();
		Ubigeo resultado = ubigeoDao.consultarUbigeo(iddepartamento + "0000");
		direccion.getUbigeo().setDepartamento(resultado.getDepartamento());
		direccion.getUbigeo().getDepartamento()
				.setNombre(resultado.getNombre());

		resultado = ubigeoDao.consultarUbigeo(iddepartamento + idprovincia
				+ "00");
		direccion.getUbigeo().setProvincia(resultado.getProvincia());
		direccion.getUbigeo().getProvincia().setNombre(resultado.getNombre());

		resultado = ubigeoDao.consultarUbigeo(iddepartamento + idprovincia
				+ iddistrito);
		direccion.getUbigeo().setDistrito(resultado.getDistrito());
		direccion.getUbigeo().getDistrito().setNombre(resultado.getNombre());

		MaestroDao maestroDao = new MaestroDaoImpl();
		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoMaestro(2);
		hijoMaestro.setCodigoEntero(direccion.getVia().getCodigoEntero());
		hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
		String direccionCompleta = "" + hijoMaestro.getAbreviatura() + " "
				+ direccion.getNombreVia();
		if (StringUtils.isNotBlank(direccion.getNumero())) {
			direccionCompleta = direccionCompleta + " Nro "
					+ direccion.getNumero();
		} else {
			direccionCompleta = direccionCompleta + " Mz. "
					+ direccion.getManzana();
			direccionCompleta = direccionCompleta + " Lt. "
					+ direccion.getLote();
		}
		if (StringUtils.isNotBlank(direccion.getInterior())) {
			direccionCompleta = direccionCompleta + " Int "
					+ direccion.getInterior();
		}
		direccion.setDireccion(direccionCompleta);

		return direccion;
	}

	@Override
	public Contacto agregarContacto(Contacto contacto) throws SQLException, Exception {
		MaestroDao maestroDao = new MaestroDaoImpl();

		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoEntero(contacto.getArea().getCodigoEntero());
		hijoMaestro.setCodigoMaestro(4);
		hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
		contacto.getArea().setNombre(hijoMaestro.getNombre());

		return contacto;
	}

	@Override
	public void registrarProveedor(Proveedor proveedor) throws SQLException, Exception{
		PersonaDao proveedorDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
			conexion.setAutoCommit(false);
			
			proveedorDao.registrarPersona(proveedor, conexion);
			if (proveedor.getListaDirecciones() != null){
				for (Direccion direccion : proveedor.getListaDirecciones()){
					direccionDao.registrarDireccion(direccion, conexion);
					for (Telefono telefono : direccion.getTelefonos()){
						telefonoDao.registrarTelefono(telefono, conexion);
					}
				}
			}
			
			if (proveedor.getListaContactos() != null){
				for (Contacto contacto : proveedor.getListaContactos()){
					proveedorDao.registrarPersona(contacto, conexion);
				}
			}
			
			
			conexion.commit();
		} catch (SQLException e) {
			conexion.rollback();
			throw new SQLException(e);
		}
	}
}
