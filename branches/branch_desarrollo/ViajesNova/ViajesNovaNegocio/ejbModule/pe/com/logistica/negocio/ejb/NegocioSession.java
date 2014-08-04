package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.base.CorreoElectronico;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CronogramaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.Maestro;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.bean.negocio.Ubigeo;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.dao.ContactoDao;
import pe.com.logistica.negocio.dao.DireccionDao;
import pe.com.logistica.negocio.dao.MaestroDao;
import pe.com.logistica.negocio.dao.PersonaDao;
import pe.com.logistica.negocio.dao.ProveedorDao;
import pe.com.logistica.negocio.dao.ServicioNegocioDao;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.dao.ServicioNoviosDao;
import pe.com.logistica.negocio.dao.TelefonoDao;
import pe.com.logistica.negocio.dao.UbigeoDao;
import pe.com.logistica.negocio.dao.impl.ClienteDaoImpl;
import pe.com.logistica.negocio.dao.impl.ContactoDaoImpl;
import pe.com.logistica.negocio.dao.impl.DireccionDaoImpl;
import pe.com.logistica.negocio.dao.impl.MaestroDaoImpl;
import pe.com.logistica.negocio.dao.impl.PersonaDaoImpl;
import pe.com.logistica.negocio.dao.impl.ProveedorDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNegocioDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNovaViajesDaoImpl;
import pe.com.logistica.negocio.dao.impl.ServicioNoviosDaoImpl;
import pe.com.logistica.negocio.dao.impl.TelefonoDaoImpl;
import pe.com.logistica.negocio.dao.impl.UbigeoDaoImpl;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilCorreo;
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
			if (proveedor.getListaServicioProveedor()!= null){
				for (ServicioProveedor servicio : proveedor.getListaServicioProveedor()) {
					boolean resultado = proveedorDao.ingresarServicioProveedor(idPersona, servicio, conexion);
					if (!resultado){
						throw new ResultadoCeroDaoException(
								"No se pudo completar el registro del servicios");
					}
				}
			}
			
			proveedorDao.registroProveedor(proveedor, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
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

			proveedor.setTipoPersona(2);

			Integer idPersona = personaDao.actualizarPersona(proveedor,
					conexion);
			if (idPersona == 0) {
				throw new ResultadoCeroDaoException(
						"No se pudo completar la actualización de la persona");
			}
			direccionDao.eliminarDireccionPersona(proveedor, conexion);
			if (proveedor.getListaDirecciones() != null) {
				int idDireccion = 0;
				for (Direccion direccion : proveedor.getListaDirecciones()) {
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					} else {
						direccionDao.eliminarTelefonoDireccion(direccion,
								conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()) {
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
			if (proveedor.getListaServicioProveedor()!= null){
				for (ServicioProveedor servicio : proveedor.getListaServicioProveedor()) {
					boolean resultado = proveedorDao.actualizarServicioProveedor(idPersona, servicio, conexion);
					if (!resultado){
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualizacion del servicio");
					}
				}
			}
			proveedorDao.actualizarProveedor(proveedor, conexion);

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
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
		proveedor.setListaServicioProveedor(proveedorDao.consultarServicioProveedor(codigoProveedor));

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
			throws SQLException {
		ProveedorDao proveedorDao = new ProveedorDaoImpl();
		List<Proveedor> listaProveedores = proveedorDao
				.buscarProveedor(proveedor);

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
	public boolean registrarCliente(Cliente cliente)
			throws ResultadoCeroDaoException, SQLException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

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

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public boolean actualizarCliente(Cliente cliente) throws SQLException,
			ResultadoCeroDaoException, Exception {
		PersonaDao personaDao = new PersonaDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

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
					idDireccion = direccionDao.actualizarDireccion(direccion,
							conexion);
					int idTelefono = 0;
					if (idDireccion == 0) {
						throw new ResultadoCeroDaoException(
								"No se pudo completar la actualización de la dirección");
					} else {
						direccionDao.eliminarTelefonoDireccion(direccion,
								conexion);
						List<Telefono> listTelefonos = direccion.getTelefonos();
						if (!listTelefonos.isEmpty()) {
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

			return true;
		} catch (ResultadoCeroDaoException e) {
			throw new ResultadoCeroDaoException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public List<Cliente> listarCliente() throws SQLException {
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
					UtilDatos.obtenerDireccionCompleta(cliente2.getDireccion(),
							hijoMaestro));

		}

		return listaClientes;
	}

	@Override
	public List<Cliente> buscarCliente(Cliente cliente) throws SQLException {
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
					UtilDatos.obtenerDireccionCompleta(cliente2.getDireccion(),
							hijoMaestro));

		}

		return listaClientes;
	}

	@Override
	public Cliente consultarCliente(int idcliente) throws SQLException,
			Exception {
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
	public Integer registrarNovios(ProgramaNovios programaNovios)
			throws ErrorRegistroDataException, SQLException, Exception {
		ServicioNoviosDao servicioNoviosDao = new ServicioNoviosDaoImpl();

		Connection conexion = null;
		try {
			conexion = UtilConexion.obtenerConexion();

			Integer idnovios = servicioNoviosDao.registrarNovios(
					programaNovios, conexion);

			if (programaNovios.getListaInvitados() != null
					&& !programaNovios.getListaInvitados().isEmpty()) {
				for (Cliente invitado : programaNovios.getListaInvitados()) {
					boolean exitoRegistro = servicioNoviosDao
							.registrarInvitado(invitado, idnovios, conexion);
					if (!exitoRegistro) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los invitados de los novios");
					}
				}
			}

			if (programaNovios.getListaServicios() != null
					&& !programaNovios.getListaServicios().isEmpty()) {
				for (ServicioNovios servicioNovios : programaNovios
						.getListaServicios()) {
					boolean exitoRegistro = servicioNoviosDao
							.ingresarServicioNovios(servicioNovios, idnovios,
									conexion);
					if (!exitoRegistro) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los servicios de los novios");
					}
				}
			}

			return idnovios;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public List<Cliente> listarClientesNovios(String genero)
			throws SQLException, Exception {
		ClienteDao clienteDao = new ClienteDaoImpl();
		return clienteDao.listarClientesNovios(genero);
	}

	@Override
	public List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception {
		ServicioNoviosDao servicioNovios = new ServicioNoviosDaoImpl();
		return servicioNovios.consultarNovios(programaNovios);
	}

	@Override
	public ServicioNovios agregarServicio(ServicioNovios servicioNovios)
			throws SQLException, Exception {
		Integer idTipoServicio = servicioNovios.getTipoServicio()
				.getCodigoEntero();
		MaestroDao maestroDao = new MaestroDaoImpl();
		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoMaestro(12);
		hijoMaestro.setCodigoEntero(idTipoServicio);
		servicioNovios.setTipoServicio((BaseVO) maestroDao
				.consultarHijoMaestro(hijoMaestro));

		return servicioNovios;
	}

	@Override
	public DetalleServicioAgencia agregarServicioVenta(
			DetalleServicioAgencia detalleServicio) throws SQLException,
			Exception {
		Integer idTipoServicio = detalleServicio.getTipoServicio()
				.getCodigoEntero();
		MaestroDao maestroDao = new MaestroDaoImpl();
		Maestro hijoMaestro = new Maestro();
		hijoMaestro.setCodigoMaestro(12);
		hijoMaestro.setCodigoEntero(idTipoServicio);
		
		Maestro consultaMaestro = maestroDao
				.consultarHijoMaestro(hijoMaestro);
		
		detalleServicio.setTipoServicio((BaseVO) consultaMaestro);
		BigDecimal comision = BigDecimal.ZERO;
		BigDecimal totalVenta = BigDecimal.ZERO;
		
		if (StringUtils.isBlank(detalleServicio.getDescripcionServicio())){
			detalleServicio.setDescripcionServicio(consultaMaestro.getDescripcion());
		}
		
		if (detalleServicio.getCantidad() == 0){
			detalleServicio.setCantidad(1);
		}
		
		if (detalleServicio.getPrecioUnitario()!= null){
			BigDecimal total = detalleServicio.getPrecioUnitario().multiply(UtilParse.parseIntABigDecimal(detalleServicio.getCantidad())); 
			totalVenta = totalVenta.add(total);
		}
		
		if (detalleServicio.getServicioProveedor().getPorcentajeComision()!=null){
			comision = detalleServicio.getServicioProveedor().getPorcentajeComision().multiply(totalVenta);
			comision = comision.divide(BigDecimal.valueOf(100.0));
		}
				
		if (detalleServicio.getServicioProveedor().getProveedor().getCodigoEntero() != null ){
			Proveedor proveedor = consultarProveedor(detalleServicio.getServicioProveedor().getProveedor().getCodigoEntero().intValue());
			detalleServicio.getServicioProveedor().setProveedor(proveedor);
		}
		
		detalleServicio.setMontoComision(comision);

		return detalleServicio;
	}

	@Override
	public List<DetalleServicioAgencia> ordenarServiciosVenta(
			List<DetalleServicioAgencia> listaServicio) throws SQLException,
			Exception {

		Collections.sort(listaServicio, new Comparator<DetalleServicioAgencia>(){
			@Override
			public int compare(DetalleServicioAgencia s1, DetalleServicioAgencia s2) {
				
				if (s1.getFechaIda().before(s2.getFechaIda())){
					return -1;
				}
				if (s1.getFechaIda().after(s2.getFechaIda())){
					return 1;
				}
				return 0;
			}
		}); 

		return listaServicio;
	}


	@Override
	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia)
			throws SQLException, Exception {
		BigDecimal valorCuota = BigDecimal.ZERO;

		ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();

		valorCuota = servicioNegocioDao.calcularCuota(servicioAgencia);

		return valorCuota;
	}

	@Override
	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();

		Connection conexion = null;
		Integer idServicio = 0;

		try {
			conexion = UtilConexion.obtenerConexion();
			
			if (servicioAgencia.getFechaServicio() == null){
				Date fechaSer = servicioAgencia.getListaDetalleServicio().get(0).getFechaIda();
				servicioAgencia.setFechaServicio(fechaSer);
			}

			idServicio = servicioNovaViajesDao.ingresarCabeceraServicio(
					servicioAgencia, conexion);

			servicioAgencia.setCodigoEntero(idServicio);
			if (servicioAgencia.getListaDetalleServicio() != null
					&& !servicioAgencia.getListaDetalleServicio().isEmpty()) {
				for (DetalleServicioAgencia detalleServicio : servicioAgencia
						.getListaDetalleServicio()) {
					boolean resultado = servicioNovaViajesDao
							.ingresarDetalleServicio(detalleServicio,
									idServicio, conexion);
					if (!resultado) {
						throw new ErrorRegistroDataException(
								"No se pudo registrar los servicios de la venta");
					}
				}
			}

			if (servicioAgencia.getFormaPago().getCodigoEntero().intValue() == 2) {
				boolean resultado = servicioNovaViajesDao
						.generarCronogramaPago(servicioAgencia, conexion);
				if (!resultado) {
					throw new ErrorRegistroDataException(
							"No se pudo generar el cronograma de pagos");
				}
			}

			return idServicio;
		} catch (ErrorRegistroDataException e) {
			throw new ErrorRegistroDataException(e.getMensajeError(), e);
		} finally {
			if (conexion != null) {
				conexion.close();
			}
		}
	}

	@Override
	public List<CronogramaPago> consultarCronograma(
			ServicioAgencia servicioAgencia) throws SQLException, Exception {
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultarCronogramaPago(servicioAgencia);
	}
	
	@Override
	public ServicioAgencia consultarServicioVenta(int idServicio) throws SQLException, Exception{
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		
		Connection conn = null;
		
		ServicioAgencia servicioAgencia;
		try {
			conn = UtilConexion.obtenerConexion();
			
			servicioAgencia = servicioNovaViajesDao.consultarServiciosVenta2(idServicio,conn);
			
			servicioAgencia.setListaDetalleServicio(servicioNovaViajesDao.consultaServicioDetalle(servicioAgencia.getCodigoEntero(), conn));
			
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		return servicioAgencia;
	}
	
	@Override
	public List<ServicioAgencia> listarServicioVenta(ServicioAgencia servicioAgencia) throws SQLException, Exception{
		ServicioNovaViajesDao servicioNovaViajesDao = new ServicioNovaViajesDaoImpl();
		return servicioNovaViajesDao.consultarServiciosVenta(servicioAgencia);
	}
	
	public void enviarCorreoMasivo(CorreoMasivo correoMasivo){
		UtilCorreo utilCorreo = new UtilCorreo();
		for (Cliente cliente : correoMasivo.getListaClientes()) {
			for(Contacto contacto : cliente.getListaContactos()){
				for (CorreoElectronico correo : contacto.getListaCorreos()){
					if (correoMasivo.getArchivoAdjunto() == null){
						utilCorreo.enviarCorreo(correo.getDireccion(), correoMasivo.getAsunto(), correoMasivo.getContenidoCorreo());
					}
					else{
						utilCorreo.enviarCorreo(correo.getDireccion(), correoMasivo.getAsunto(), correoMasivo.getContenidoCorreo(), correoMasivo.getArchivoAdjunto());
					}
				}
			}
		}
	}
	
	@Override
	public List<Cliente> consultarCliente2(Cliente cliente) throws SQLException, Exception{
		ClienteDao clienteDao = new ClienteDaoImpl();
		DireccionDao direccionDao = new DireccionDaoImpl();
		TelefonoDao telefonoDao = new TelefonoDaoImpl();
		ContactoDao contactoDao = new ContactoDaoImpl();
		
		Connection conn = null;
		
		
		List<Cliente> listarCliente;
		try {
			conn = UtilConexion.obtenerConexion();
			cliente.setTipoPersona(1);
			listarCliente = clienteDao.listarClientes(cliente, conn);
			int info=0;
			for (Cliente cliente2 : listarCliente) {
				info=1;
				cliente2.setInfoCliente(info);
				List<Direccion> listaDireccion = direccionDao.consultarDireccionPersona(cliente2.getCodigoEntero(), conn);
				if (listaDireccion != null && listaDireccion.size()>0){
					info++;
					cliente2.setInfoCliente(info);
					cliente2.setDireccion(listaDireccion.get(0));
					cliente2.getDireccion().setDireccion(this.obtenerDireccionCompleta(cliente2.getDireccion()));
					for (Direccion direccion : listaDireccion) {
						List<Telefono> listaTelefono = telefonoDao.consultarTelefonosDireccion(direccion.getCodigoEntero(), conn);
						if (listaTelefono != null && listaTelefono.size()>0){
							info++;
							cliente2.setInfoCliente(info);
						}
					}
				}
				
				List<Contacto> listaContacto = contactoDao.listarContactosXPersona(cliente2.getCodigoEntero(), conn);
				if (listaContacto != null && listaContacto.size()>0){
					info++;
					cliente2.setInfoCliente(info);
					for (Contacto contacto : listaContacto) {
						List<Telefono> listaTelefono = telefonoDao.consultarTelefonosXPersona(contacto.getCodigoEntero(), conn);
						if (listaTelefono != null && listaTelefono.size()>0){
							info++;
							cliente2.setInfoCliente(info);
							cliente2.setTelefonoMovil(listaTelefono.get(0));
						}
					}
				}	
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		
		
		return listarCliente;
	}
	
	@Override
	public List<ServicioProveedor> proveedoresXServicio(BaseVO servicio) throws SQLException, Exception{
		ServicioNegocioDao servicioNegocioDao = new ServicioNegocioDaoImpl();
		
		return servicioNegocioDao.proveedoresXServicio(servicio);
	}

}
