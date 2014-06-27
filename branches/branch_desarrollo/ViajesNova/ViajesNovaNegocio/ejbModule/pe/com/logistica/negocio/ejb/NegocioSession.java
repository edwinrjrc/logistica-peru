package pe.com.logistica.negocio.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.PersonaDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.ServicioNoviosDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.dao.UbigeoDao;
import pe.com.logistica.negocio.dao.impl.ClienteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ContactoDaoImpl;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.PersonaDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNoviosDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.UbigeoDaoImpl;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilDatos;
import pe.com.logistica.negocio.util.UtilJdbc;

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
	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception {
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

		direccion.getUbigeo().setCodigoCadena(
				iddepartamento + idprovincia + iddistrito);

		direccion.setDireccion(obtenerDireccionCompleta(direccion));

		return direccion;
	}

	@Override
	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception {
		MaestroDao maestroDao = new MaestroDaoImpl();

		try {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoEntero(contacto.getArea().getCodigoEntero());
			hijoMaestro.setCodigoMaestro(4);
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			contacto.getArea().setNombre(hijoMaestro.getNombre());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contacto;
	}

	@Override
	public boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
			conexion.setAutoCommit(false);

			proveedor.setTipoPersona(2);
			int idPersona = personaDao.registrarPersona(proveedor, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar el registro de la persona");
			}
			proveedor.setCodigoEntero(idPersona);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao.registrarDireccion(direccion,
							conexion);
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro de la direccion");
					}
					direccion.setCodigoEntero(idDireccion);
					int idTelefono = 0;
					if (!direccion.getTelefonos().isEmpty()) {
						for (Telefono telefono : direccion.getTelefonos()) {
							telefono.getEmpresaOperadora().setCodigoEntero(0);
							idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoDireccion(idTelefono,
									idDireccion, conexion);
						}
					}

					direccionDao.registrarPersonaDireccion(
							proveedor.getCodigoEntero(),
							proveedor.getTipoPersona(), idDireccion, conexion);
				}
			}

			if (proveedor.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : proveedor.getListaContactos()) {
					contacto.setTipoPersona(3);
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);
					
					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			proveedorDao.registroProveedor(proveedor, conexion);

			conexion.commit();
			return true;
		} catch (ResultadoCeroDaoException e) {
			conexion.rollback();
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			conexion.rollback();
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, ResultadoCeroDaoException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
			conexion.setAutoCommit(false);

			proveedor.setTipoPersona(2);

			Integer idPersona = personaDao.actualizarPersona(proveedor, conexion); 
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualización de la persona");
			}
			direccionDao.eliminarDireccionPersona(proveedor, conexion);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao
							.actualizarDireccion(direccion, conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					}
					else {
						direccionDao.eliminarTelefonoDireccion(direccion, conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()){
							for (Telefono telefono : listTelefonos) {
								telefono.getEmpresaOperadora().setCodigoEntero(
										0);
								idTelefono = telefonoDao.registrarTelefono(
										telefono, conexion);
								if (idTelefono == 0) {
									throw new ResultadoCeroDaoException(
											"No se pudo completar el registro del teléfono de direccion");
								}
								telefonoDao.registrarTelefonoDireccion(
										idTelefono, idDireccion, conexion);
							}
						}
					}
					
					direccionDao.registrarPersonaDireccion(
							proveedor.getCodigoEntero(),
							proveedor.getTipoPersona(), idDireccion, conexion);
				}
			}

			contactoDao.eliminarContactoProveedor(proveedor, conexion);
			if (proveedor.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : proveedor.getListaContactos()) {
					contacto.setTipoPersona(3);
					
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del teléfono de contacto");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);
					
					contactoDao.eliminarCorreosContacto(contacto, conexion);
					
					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			proveedorDao.actualizarProveedor(proveedor, conexion);

			conexion.commit();
			return true;
		} catch (ResultadoCeroDaoException e) {
			conexion.rollback();
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			conexion.rollback();
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public List<Proveedor> listarProveedor(Proveedor proveedor)
			throws SQLException {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		List<Proveedor> listaProveedores = proveedorDao
				.listarProveedor(proveedor);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Proveedor proveedor2 : listaProveedores) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(proveedor2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			proveedor2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							proveedor2.getDireccion(), hijoMaestro));

		}

		return listaProveedores;
	}

	@Override
	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception {
		DireccionDao direccionDao = new DireccionDaoImpl();
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Proveedor proveedor = proveedorDao.consultarProveedor(codigoProveedor);
		List<Direccion> listaDirecciones = direccionDao
				.consultarDireccionProveedor(codigoProveedor);

		for (Direccion direccion : listaDirecciones) {
			direccion.setDireccion(obtenerDireccionCompleta(direccion));
		}
		proveedor.setListaDirecciones(listaDirecciones);
		proveedor.setListaContactos(contactoDao
				.consultarContactoProveedor(codigoProveedor));

		return proveedor;
	}

	private String obtenerDireccionCompleta(Direccion direccion)
			throws SQLException, Exception {
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

		return UtilJdbc.convertirMayuscula(direccionCompleta);
	}
	
	@Override
	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException{
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		List<Proveedor> listaProveedores = proveedorDao.buscarProveedor(proveedor);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Proveedor proveedor2 : listaProveedores) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(proveedor2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			proveedor2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							proveedor2.getDireccion(), hijoMaestro));

		}
		
		return listaProveedores;
	}

	@Override
	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
			conexion.setAutoCommit(false);

			cliente.setTipoPersona(1);
			int idPersona = personaDao.registrarPersona(cliente, conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar el registro de la persona");
			}
			cliente.setCodigoEntero(idPersona);
			if (cliente.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : cliente.getListaDirecciones()) {
					idDireccion = direccionDao.registrarDireccion(direccion,
							conexion);
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro de la direccion");
					}
					direccion.setCodigoEntero(idDireccion);
					int idTelefono = 0;
					if (!direccion.getTelefonos().isEmpty()) {
						for (Telefono telefono : direccion.getTelefonos()) {
							telefono.getEmpresaOperadora().setCodigoEntero(0);
							idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoDireccion(idTelefono,
									idDireccion, conexion);
						}
					}

					direccionDao.registrarPersonaDireccion(
							cliente.getCodigoEntero(),
							cliente.getTipoPersona(), idDireccion, conexion);
				}
			}

			if (cliente.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : cliente.getListaContactos()) {
					contacto.setTipoPersona(3);
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del telefono");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);
					
					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			ClienteDao clienteDao = new ClienteDaoImpl();
			clienteDao.registroCliente(cliente, conexion);

			conexion.commit();
			return true;
		} catch (ResultadoCeroDaoException e) {
			conexion.rollback();
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			conexion.rollback();
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public boolean actualizarCliente(Cliente cliente) throws SQLException, ResultadoCeroDaoException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
			conexion.setAutoCommit(false);

			cliente.setTipoPersona(1);

			Integer idPersona = personaDao.actualizarPersona(cliente, conexion); 
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualización de la persona");
			}
			direccionDao.eliminarDireccionPersona(cliente, conexion);
			if (cliente.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : cliente.getListaDirecciones()) {
					idDireccion = direccionDao
							.actualizarDireccion(direccion, conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					}
					else {
						direccionDao.eliminarTelefonoDireccion(direccion, conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()){
							for (Telefono telefono : listTelefonos) {
								telefono.getEmpresaOperadora().setCodigoEntero(
										0);
								idTelefono = telefonoDao.registrarTelefono(
										telefono, conexion);
								if (idTelefono == 0) {
									throw new ResultadoCeroDaoException(
											"No se pudo completar el registro del teléfono de direccion");
								}
								telefonoDao.registrarTelefonoDireccion(
										idTelefono, idDireccion, conexion);
							}
						}
					}
					
					direccionDao.registrarPersonaDireccion(
							cliente.getCodigoEntero(),
							cliente.getTipoPersona(), idDireccion, conexion);
				}
			}

			contactoDao.eliminarContactoProveedor(cliente, conexion);
			if (cliente.getListaContactos() != null) {
				int idContacto = 0;
				for (Contacto contacto : cliente.getListaContactos()) {
					contacto.setTipoPersona(3);
					
					idContacto = personaDao
							.registrarPersona(contacto, conexion);
					contacto.setCodigoEntero(idContacto);
					
					if (!contacto.getListaTelefonos().isEmpty()) {
						for (Telefono telefono : contacto.getListaTelefonos()) {
							int idTelefono = telefonoDao.registrarTelefono(
									telefono, conexion);
							if (idTelefono == 0) {
								throw new ResultadoCeroDaoException(
										"No se pudo completar el registro del teléfono de contacto");
							}
							telefonoDao.registrarTelefonoPersona(idTelefono,
									idContacto, conexion);
						}
					}
					contactoDao.registrarContactoProveedor(idPersona, contacto,
							conexion);
					
					contactoDao.eliminarCorreosContacto(contacto, conexion);
					
					contactoDao.ingresarCorreoElectronico(contacto, conexion);
				}
			}
			ClienteDao clienteDao = new ClienteDaoImpl();
			clienteDao.actualizarPersonaAdicional(cliente, conexion);

			conexion.commit();
			return true;
		} catch (ResultadoCeroDaoException e) {
			conexion.rollback();
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			conexion.rollback();
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}
	
	@Override
	public List<Cliente> listarCliente()
			throws SQLException{
		ClienteDao clienteDao = new ClienteDaoImpl();
		List<Cliente> listaClientes = clienteDao.consultarPersona(null);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Cliente cliente2 : listaClientes) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(cliente2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			cliente2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							cliente2.getDireccion(), hijoMaestro));

		}
		
		return listaClientes;
	}
	
	@Override
	public List<Cliente> buscarCliente(Cliente cliente)
			throws SQLException{
		ClienteDao clienteDao = new ClienteDaoImpl();
		List<Cliente> listaClientes = clienteDao.buscarPersona(cliente);

		MaestroDao maestroDao = new MaestroDaoImpl();

		for (Cliente cliente2 : listaClientes) {
			Maestro hijoMaestro = new Maestro();
			hijoMaestro.setCodigoMaestro(2);
			hijoMaestro.setCodigoEntero(cliente2.getDireccion().getVia()
					.getCodigoEntero());
			hijoMaestro = maestroDao.consultarHijoMaestro(hijoMaestro);
			cliente2.getDireccion().setDireccion(
					UtilDatos.obtenerDireccionCompleta(
							cliente2.getDireccion(), hijoMaestro));

		}
		
		return listaClientes;
	}

	@Override
	public Cliente consultarCliente(int idcliente) throws SQLException, Exception {
		DireccionDao direccionDao = new DireccionDaoImpl();
		ClienteDao clienteDao = new ClienteDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Cliente cliente = clienteDao.consultarCliente(idcliente);
		List<Direccion> listaDirecciones = direccionDao
				.consultarDireccionProveedor(idcliente);

		for (Direccion direccion : listaDirecciones) {
			direccion.setDireccion(obtenerDireccionCompleta(direccion));
		}
		cliente.setListaDirecciones(listaDirecciones);
		cliente.setListaContactos(contactoDao
				.consultarContactoProveedor(idcliente));

		return cliente;
	}
	
	@Override
	public Integer registrarNovios(ProgramaNovios programaNovios) throws SQLException, Exception{
		ServicioNoviosDao servicioNoviosDao = new ServicioNoviosDaoImpl();
		
		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();
						
			Integer idnovios = servicioNoviosDao.registrarNovios(programaNovios, conexion);
			
			if (programaNovios.getListaInvitados() != null && !programaNovios.getListaInvitados().isEmpty()){
				for (Cliente invitado : programaNovios.getListaInvitados()) {
					boolean exitoRegistro = servicioNoviosDao.registrarInvitado(invitado, idnovios, conexion);
					if (!exitoRegistro){
						throw new ErrorRegistroDataException("No se puedo registrar los invitados de los novios");
					}
				}
			}
			
			if (programaNovios.getListaServicios() != null && !programaNovios.getListaServicios().isEmpty()){
				for (ServicioNovios servicioNovios : programaNovios.getListaServicios()) {
					boolean exitoRegistro = servicioNoviosDao.ingresarServicioNovios(servicioNovios, idnovios, conexion);
					if (!exitoRegistro){
						throw new ErrorRegistroDataException("No se puedo registrar los servicios de los novios");
					}
				}
			}
			
			return idnovios;
		}
		catch (ErrorRegistroDataException e){
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		}
		catch (SQLException e){
			throw new SQLException(e);
		}
		finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}
	
	@Override
	public List<Cliente> listarClientesNovios(String genero) throws SQLException, Exception{
		ClienteDao clienteDao = new ClienteDaoImpl();
		return clienteDao.listarClientesNovios(genero);
	}
	
	@Override
	public List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios) throws SQLException, Exception{
		ServicioNoviosDao servicioNovios = new ServicioNoviosDaoImpl();
		return servicioNovios.consultarNovios(programaNovios);
	}
	
	@Override
	public ServicioNovios agregarServicio(ServicioNovios servicioNovios) throws SQLException, Exception{
		Integer idTipoServicio = servicioNovios.getTipoServicio().getCodigoEntero();
		MaestroDao maestroDao = new MaestroDaoImpl();
		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoMaestro(12);
		hijoMaestro.setCodigoEntero(idTipoServicio);
		servicioNovios.setTipoServicio((BaseVO)maestroDao.consultarHijoMaestro(hijoMaestro));
		
		return servicioNovios;
	}
}
