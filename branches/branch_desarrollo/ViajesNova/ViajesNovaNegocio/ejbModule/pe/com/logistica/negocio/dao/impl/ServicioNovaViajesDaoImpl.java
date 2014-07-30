/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.Util.UtilParse;
import pe.com.logistica.bean.negocio.CronogramaPago;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ServicioNovaViajesDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author edwreb
 *
 */
public class ServicioNovaViajesDaoImpl implements ServicioNovaViajesDao {

	/**
	 * 
	 */
	public ServicioNovaViajesDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ServicioNovaViajesDao#ingresarCabeceraServicio(pe.com.logistica.bean.negocio.ServicioAgencia)
	 */
	@Override
	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;
		String sql = "{ ? = call soporte.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			cs.setInt(i++, servicioAgencia.getCantidadServicios());
			cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getDestino().getNombre());
			cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return idservicio;
	}
	
	@Override
	public Integer ingresarCabeceraServicio(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		Integer idservicio = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			cs.setInt(i++, servicioAgencia.getCantidadServicios());
			cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getDestino().getNombre());
			cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return idservicio;
	}
	
	@Override
	public Integer ingresarCabeceraServicio2(ServicioAgencia servicioAgencia)
			throws SQLException {
		Integer idservicio = 0;
		Connection conn = null;
		CallableStatement cs = null;

		String sql = "{ ? = call soporte.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotal());
			cs.setInt(i++, servicioAgencia.getCantidadServicios());
			cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			cs.setString(i++, servicioAgencia.getDestino().getNombre());
			cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			cs.setInt(i++, servicioAgencia.getEstadoPago().getCodigoEntero().intValue());
			cs.setInt(i++, servicioAgencia.getNroCuotas());
			cs.setBigDecimal(i++, servicioAgencia.getTea());
			cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return idservicio;
	}
	
	@Override
	public Integer ingresarCabeceraServicio2(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		Integer idservicio = 0;
		CallableStatement cs = null;

		String sql = "{ ? = call soporte.fn_ingresarserviciocabecera(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, servicioAgencia.getCliente().getCodigoEntero().intValue());
			if (servicioAgencia.getCliente2().getCodigoEntero()!= null && servicioAgencia.getCliente2().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getCliente2().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getFechaServicio()!= null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaServicio()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getCantidadServicios() != 0){
				cs.setInt(i++, servicioAgencia.getCantidadServicios());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getMontoTotalServicios()!= null){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalFee() != null){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalFee());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getMontoTotalComision() != null){
				cs.setBigDecimal(i++, servicioAgencia.getMontoTotalComision());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getDestino().getCodigoEntero() != null && servicioAgencia.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getDestino().getNombre())){
				cs.setString(i++, servicioAgencia.getDestino().getNombre());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (servicioAgencia.getFormaPago().getCodigoEntero() != null && servicioAgencia.getFormaPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getFormaPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getEstadoPago().getCodigoEntero()!= null && servicioAgencia.getEstadoPago().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, servicioAgencia.getEstadoPago().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getNroCuotas()!=0){
				cs.setInt(i++, servicioAgencia.getNroCuotas());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (servicioAgencia.getTea()!=null && !BigDecimal.ZERO.equals(servicioAgencia.getTea())){
				cs.setBigDecimal(i++, servicioAgencia.getTea());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getValorCuota()!=null && !BigDecimal.ZERO.equals(servicioAgencia.getValorCuota())){
				cs.setBigDecimal(i++, servicioAgencia.getValorCuota());
			}
			else{
				cs.setNull(i++, Types.DECIMAL);
			}
			if (servicioAgencia.getFechaPrimerCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getFechaUltimaCuota()!=null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaUltimaCuota()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			if (servicioAgencia.getVendedor().getCodigoEntero()!=0){
				cs.setInt(i++, servicioAgencia.getVendedor().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getUsuarioCreacion())){
				cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getIpCreacion())){
				cs.setString(i++, servicioAgencia.getIpCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.execute();
			
			idservicio = cs.getInt(1);
		} catch (SQLException e) {
			idservicio = 0;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return idservicio;
	}
	
	@Override
	public boolean ingresarDetalleServicio(DetalleServicioAgencia detalleServicio, int idServicio)
			throws SQLException {
		boolean resultado=false; 
		Connection conn = null;
		CallableStatement cs = null;

		String sql = "{ ? = call soporte.fn_ingresarserviciodetalle(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, detalleServicio.getTipoServicio().getCodigoEntero().intValue());
			cs.setInt(i++, idServicio);
			cs.setString(i++, detalleServicio.getDescripcionServicio());
			cs.setInt(i++, detalleServicio.getDestino().getCodigoEntero().intValue());
			cs.setString(i++, detalleServicio.getDestino().getNombre());
			cs.setInt(i++, detalleServicio.getDias());
			cs.setInt(i++, detalleServicio.getNoches());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(detalleServicio.getFechaIda()));
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(detalleServicio.getFechaRegreso()));
			cs.setInt(i++, detalleServicio.getCantidad());
			cs.setBigDecimal(i++, detalleServicio.getPrecioUnitario());

			cs.setString(i++, detalleServicio.getUsuarioCreacion());
			cs.setString(i++, detalleServicio.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado=false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}
	
	@Override
	public boolean ingresarDetalleServicio(DetalleServicioAgencia detalleServicio, int idServicio, Connection conn)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;

		String sql = "{ ? = call negocio.fn_ingresarserviciodetalle(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, detalleServicio.getTipoServicio().getCodigoEntero().intValue());
			cs.setInt(i++, idServicio);
			cs.setString(i++, detalleServicio.getDescripcionServicio());
			if (detalleServicio.getDestino().getCodigoEntero() != null && detalleServicio.getDestino().getCodigoEntero().intValue()!=0){
				cs.setInt(i++, detalleServicio.getDestino().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(detalleServicio.getDestino().getNombre())){
				cs.setString(i++, detalleServicio.getDestino().getNombre());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (detalleServicio.getDias() != 0){
				cs.setInt(i++, detalleServicio.getDias());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (detalleServicio.getNoches() != 0){
				cs.setInt(i++, detalleServicio.getNoches());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(detalleServicio.getFechaIda()));
			if (detalleServicio.getFechaRegreso() != null){
				cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(detalleServicio.getFechaRegreso()));
			}
			else{
				cs.setNull(i++, Types.DATE);
			}
			cs.setInt(i++, detalleServicio.getCantidad());
			cs.setBigDecimal(i++, detalleServicio.getPrecioUnitario());

			cs.setString(i++, detalleServicio.getUsuarioCreacion());
			cs.setString(i++, detalleServicio.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return resultado;
	}

	
	@Override
	public boolean generarCronogramaPago(ServicioAgencia servicioAgencia, Connection conn)
			throws SQLException {
		boolean resultado = false;
		CallableStatement cs = null;

		String sql = "{ ? = call negocio.fn_generarcronogramapago(?,?,?,?,?,?,?)}";
		
		try {
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.setDate(i++, UtilJdbc.convertirUtilDateSQLDate(servicioAgencia.getFechaPrimerCuota()));
			cs.setBigDecimal(i++, servicioAgencia.getMontoTotalServicios());
			cs.setBigDecimal(i++, servicioAgencia.getTea());
			cs.setBigDecimal(i++, UtilParse.parseIntABigDecimal(servicioAgencia.getNroCuotas()));

			cs.setString(i++, servicioAgencia.getUsuarioCreacion());
			cs.setString(i++, servicioAgencia.getIpCreacion());
			cs.execute();
			
			resultado = cs.getBoolean(1);
		} catch (SQLException e) {
			resultado = false;
			throw new SQLException(e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}

		return resultado;
	}
	
	@Override
	public List<CronogramaPago> consultarCronogramaPago(ServicioAgencia servicioAgencia)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarcronogramapago(?)}";
		List<CronogramaPago> cronograma = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, servicioAgencia.getCodigoEntero().intValue());
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			CronogramaPago cuota = null;
			cronograma = new ArrayList<CronogramaPago>();
			while (rs.next()){
				cuota = new CronogramaPago();
				cuota.setNroCuota(UtilJdbc.obtenerNumero(rs, "nrocuota"));
				cuota.setFechaVencimiento(UtilJdbc.obtenerFecha(rs, "fechavencimiento"));
				cuota.setCapital(UtilJdbc.obtenerBigDecimal(rs, "capital"));
				cuota.setInteres(UtilJdbc.obtenerBigDecimal(rs, "interes"));
				cuota.setTotalCuota(UtilJdbc.obtenerBigDecimal(rs, "totalcuota"));
				cuota.getEstadoCuota().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadocuota"));
				if (cuota.getEstadoCuota().getCodigoEntero().intValue() == 1){
					cuota.getEstadoCuota().setNombre("PENDIENTE");
				}
				cronograma.add(cuota);
			}
		} catch (SQLException e) {
			cronograma = null;
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return cronograma;
	}
	
	@Override
	public List<ServicioAgencia> consultarServiciosVenta(ServicioAgencia servicioAgencia)
			throws SQLException {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarservicioventa(?,?,?)}";
		List<ServicioAgencia> listaVentaServicios = null;
		try {
			conn = UtilConexion.obtenerConexion();
			cs = conn.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.OTHER);
			if (servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero() != null && servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue() != 0){
				cs.setInt(i++, servicioAgencia.getCliente().getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getCliente().getDocumentoIdentidad().getNumeroDocumento())){
				cs.setString(i++, servicioAgencia.getCliente().getDocumentoIdentidad().getNumeroDocumento());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(servicioAgencia.getCliente().getNombres())){
				cs.setString(i++, servicioAgencia.getCliente().getNombres());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.execute();
			
			rs = (ResultSet)cs.getObject(1);
			ServicioAgencia servicioAgencia2 = null;
			listaVentaServicios = new ArrayList<ServicioAgencia>();
			while (rs.next()){
				servicioAgencia2 = new ServicioAgencia();
				servicioAgencia2.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "id"));
				servicioAgencia2.getCliente().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente1"));
				servicioAgencia2.getCliente().setNombres(UtilJdbc.obtenerCadena(rs, "nombres1"));
				servicioAgencia2.getCliente().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno1"));
				servicioAgencia2.getCliente().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno1"));
				servicioAgencia2.getCliente2().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idcliente2"));
				servicioAgencia2.getCliente2().setNombres(UtilJdbc.obtenerCadena(rs, "nombres2"));
				servicioAgencia2.getCliente2().setApellidoPaterno(UtilJdbc.obtenerCadena(rs, "apellidopaterno2"));
				servicioAgencia2.getCliente2().setApellidoMaterno(UtilJdbc.obtenerCadena(rs, "apellidomaterno2"));
				servicioAgencia2.setFechaServicio(UtilJdbc.obtenerFecha(rs, "fechaservicio"));
				servicioAgencia2.setMontoTotalServicios(UtilJdbc.obtenerBigDecimal(rs, "montototal"));
				servicioAgencia2.setCantidadServicios(UtilJdbc.obtenerNumero(rs, "cantidadservicios"));
				servicioAgencia2.getDestino().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "iddestino"));
				servicioAgencia2.getDestino().setNombre(UtilJdbc.obtenerCadena(rs, "descdestino"));
				servicioAgencia2.getFormaPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idformapago"));
				servicioAgencia2.getFormaPago().setNombre(UtilJdbc.obtenerCadena(rs, "nommediopago"));
				servicioAgencia2.getEstadoPago().setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idestadopago"));
				servicioAgencia2.getEstadoPago().setNombre(UtilJdbc.obtenerCadena(rs, "nomestpago"));
				listaVentaServicios.add(servicioAgencia2);
			}
		} catch (SQLException e) {
			servicioAgencia = null;
			throw new SQLException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cs != null) {
					cs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				try {
					if (conn != null) {
						conn.close();
					}
					throw new SQLException(e);
				} catch (SQLException e1) {
					throw new SQLException(e);
				}
			}
		}

		return listaVentaServicios;
	}
}
