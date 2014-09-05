/**
 * 
 */
package pe.com.logistica.web.servicio;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Contacto;
import pe.com.logistica.bean.negocio.CorreoClienteMasivo;
import pe.com.logistica.bean.negocio.CuotaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Direccion;
import pe.com.logistica.bean.negocio.MaestroServicio;
import pe.com.logistica.bean.negocio.ProgramaNovios;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioNovios;
import pe.com.logistica.bean.negocio.ServicioProveedor;
import pe.com.logistica.negocio.exception.ErrorRegistroDataException;
import pe.com.logistica.negocio.exception.ResultadoCeroDaoException;

/**
 * @author Edwin
 * 
 */
public interface NegocioServicio {

	public Direccion agregarDireccion(Direccion direccion) throws SQLException,
			Exception;

	public Contacto agregarContacto(Contacto contacto) throws SQLException,
			Exception;

	public boolean registrarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public boolean actualizarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	List<Proveedor> listarProveedor(Proveedor proveedor) throws SQLException,
			Exception;

	public Proveedor consultarProveedorCompleto(int codigoProveedor)
			throws SQLException, Exception;

	public List<Proveedor> buscarProveedor(Proveedor proveedor)
			throws SQLException, Exception;

	public boolean registrarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	public boolean actualizarCliente(Cliente cliente) throws ResultadoCeroDaoException, SQLException, Exception;

	List<Cliente> buscarCliente(Cliente cliente) throws SQLException;
	
	List<Cliente> listarCliente() throws SQLException;

	public Cliente consultarClienteCompleto(int idcliente) throws SQLException, Exception;
	
	public Integer registrarNovios(ProgramaNovios programaNovios) throws SQLException, Exception;

	List<Cliente> listarClientesNovios(String genero) throws SQLException,
			Exception;

	List<ProgramaNovios> consultarNovios(ProgramaNovios programaNovios)
			throws SQLException, Exception;

	ServicioNovios agregarServicioNovios(ServicioNovios servicioNovios)
			throws SQLException, Exception;

	DetalleServicioAgencia agregarServicioVenta(
			DetalleServicioAgencia detalleServicio) throws SQLException,
			Exception;

	public BigDecimal calcularValorCuota(ServicioAgencia servicioAgencia) throws SQLException, Exception;

	List<CuotaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	Integer registrarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<DetalleServicioAgencia> ordenarServiciosVenta(
			List<DetalleServicioAgencia> listaServicio) throws SQLException,
			Exception;
	
	List<Cliente> consultarCliente2(Cliente cliente) throws SQLException, Exception;

	List<ServicioProveedor> proveedoresXServicio(int idServicio)
			throws SQLException, Exception;

	List<ServicioAgencia> listarVentaServicio(ServicioAgencia servicioAgencia)
			throws SQLException, Exception;

	ServicioAgencia consultarVentaServicio(int idServicio) throws SQLException,
			Exception;

	List<Cliente> buscarClientesNovios(Cliente cliente) throws SQLException,
			Exception;

	public ProgramaNovios consultarProgramaNovios(int idProgramaNovios) throws SQLException,
	Exception;
	
	public boolean ingresarMaestroServicio(MaestroServicio servicio) throws ErrorRegistroDataException, SQLException, Exception;
	
	public boolean actualizarMaestroServicio(MaestroServicio servicio) throws SQLException, Exception;
	
	public List<MaestroServicio> listarMaestroServicio() throws SQLException, Exception;
	
	public MaestroServicio consultarMaestroServicio(int idMaestroServicio) throws SQLException, Exception;

	public Integer actualizarNovios(ProgramaNovios programaNovios) throws SQLException, Exception;

	Integer actualizarVentaServicio(ServicioAgencia servicioAgencia)
			throws ErrorRegistroDataException, SQLException, Exception;

	List<MaestroServicio> listarMaestroServicioFee() throws SQLException,
			Exception;

	List<MaestroServicio> listarMaestroServicioImpto() throws SQLException,
			Exception;

	List<CorreoClienteMasivo> listarClientesCorreo() throws SQLException, Exception;
}
