package pe.com.logistica.negocio.ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CorreoMasivo;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.negocio.exception.EnvioCorreoException;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

@Remote
public interface NegocioSessionRemote {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	boolean registrarProveedor(Proveedor proveedor)
			throws ResultadoCeroDaoException, SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException;

	public Proveedor consultarProveedor(int codigoProveedor)
			throws SQLException, Exception;

	boolean actualizarProveedor(Proveedor proveedor) throws SQLException,
			ResultadoCeroDaoException, Exception;
	
	List<Proveedor> buscarProveedor(Proveedor proveedor) throws SQLException;

	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws SQLException, ResultadoCeroDaoException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;

	public Cliente consultarCliente(int idcliente) throws SQLException, Exception;

	List<Cliente> listarCliente() throws SQLException;
	
	Integer registrarNovios(ProgramaNovios programaNovios) throws ErrorRegistroDataException, SQLException,
	Exception;
	
	public List<Cliente> listarClientesNovios(String genero) throws SQLException, Exception;
	
	public List<Cliente> consultarClientesNovios(Cliente cliente)
			throws SQLException, Exception;
	
	List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	ServicioNovios agregarServicio(ServicioNovios servicioNovios)
			throws SQLException, Exception;

	DetalleServicioAgencia agregarServicioVenta(
			DetalleServicioAgencia detalleServicio) throws SQLException,
			Exception;

	public List<DetalleServicioAgencia> ordenarServiciosVenta(List<DetalleServicioAgencia> listaServicio) throws SQLException, Exception;
	
	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia) throws SQLException, Exception;
	
	public Integer registrarVentaServicio(ServicioAgencia servicioAgencia) throws ErrorRegistroDataException, SQLException, Exception;
	
	public List<CuotaPago> consultarCronograma(ServicioAgencia servicioAgencia) throws SQLException, Exception;
	
	public List<ServicioAgencia> listarServicioVenta(ServicioAgencia servicioAgencia) throws SQLException, Exception;
	
	public ServicioAgencia consultarServicioVenta(int idServicio) throws SQLException, Exception;
	
	public List<Cliente> consultarCliente2(Cliente cliente) throws SQLException, Exception;
	
	List<ServicioProveedor> proveedoresXServicio(BaseVO servicio)
			throws SQLException, Exception;

	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios) throws SQLException, Exception;
	
	public boolean ingresarMaestroServicio(MaestroServicio servicio) throws ErrorRegistroDataException, SQLException, Exception;
	
	public boolean actualizarMaestroServicio(MaestroServicio servicio) throws SQLException, Exception;
	
	public List<MaestroServicio> listarMaestroServicio() throws SQLException, Exception;
	
	public List<MaestroServicio> listarMaestroServicioFee() throws SQLException, Exception;
	
	public List<MaestroServicio> listarMaestroServicioImpto() throws SQLException, Exception;
	
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio) throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios) throws SQLException, Exception;
	
	public Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;
	
	public List<CorreoClienteMasivo> listarClientesCorreo() throws SQLException, Exception;
	
	public boolean enviarCorreoMasivo(CorreoMasivo correoMasivo) throws EnvioCorreoException, Exception;
	
	public List<Cliente> listarClientesCumples() throws SQLException, Exception;
}
