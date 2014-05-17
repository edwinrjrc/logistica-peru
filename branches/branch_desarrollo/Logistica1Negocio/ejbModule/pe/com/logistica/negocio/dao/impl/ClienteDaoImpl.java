/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Cliente;
import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.negocio.dao.ClienteDao;
import pe.com.logistica.negocio.util.UtilConexion;
import pe.com.logistica.negocio.util.UtilJdbc;

/**
 * @author Edwin
 * 
 */
public class ClienteDaoImpl implements ClienteDao {

	/**
	 * 
	 */
	public ClienteDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pe.com.logistica.negocio.dao.ClienteDao#consultarPersona(pe.com.logistica
	 * .bean.base.Persona)
	 */
	@Override
	public List<Cliente> consultarPersona(Persona persona) throws SQLException {
		List<Cliente> resultado = null;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String sql = "{ ? = call negocio.fn_consultarpersonas(?,?,?,?)}";

		try {
			conn = UtilConexion.obtenerConexion();
			conn.setAutoCommit(false);
			cs = conn.prepareCall(sql);
			int i = 1;
			cs.registerOutParameter(i++, Types.OTHER);
			cs.setInt(i++, 1);
			if (persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero() != null){
				cs.setInt(i++, persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero().intValue());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(persona.getDocumentoIdentidad().getNumeroDocumento())){
				cs.setString(i++, persona.getDocumentoIdentidad().getNumeroDocumento());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(persona.getNombres())){
				cs.setString(i++, persona.getNombres());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.execute();
			rs = (ResultSet)cs.getObject(1);

			resultado = new ArrayList<Cliente>();
			Cliente persona2 = null;
			while (rs.next()) {
				persona2 = new Cliente();
				persona2.setCodigoEntero(UtilJdbc.obtenerNumero(rs,
						"idproveedor"));
				persona2.getDocumentoIdentidad()
						.getTipoDocumento()
						.setCodigoEntero(
								UtilJdbc.obtenerNumero(rs, "idtipodocumento"));
				persona2.getDocumentoIdentidad()
						.getTipoDocumento()
						.setNombre(
								UtilJdbc.obtenerCadena(rs,
										"nombretipodocumento"));
				persona2.getDocumentoIdentidad().setNumeroDocumento(
						UtilJdbc.obtenerCadena(rs, "numerodocumento"));
				persona2.setNombres(UtilJdbc.obtenerCadena(rs, "nombres"));
				persona2.setApellidoPaterno(UtilJdbc.obtenerCadena(rs,
						"apellidopaterno"));
				persona2.setApellidoMaterno(UtilJdbc.obtenerCadena(rs,
						"apellidomaterno"));
				persona2.getDireccion().getVia()
						.setCodigoEntero(UtilJdbc.obtenerNumero(rs, "idvia"));
				persona2.getDireccion().getVia()
						.setNombre(UtilJdbc.obtenerCadena(rs, "nombretipovia"));
				persona2.getDireccion().setNombreVia(
						UtilJdbc.obtenerCadena(rs, "nombrevia"));
				persona2.getDireccion().setNumero(
						UtilJdbc.obtenerCadena(rs, "numero"));
				persona2.getDireccion().setInterior(
						UtilJdbc.obtenerCadena(rs, "interior"));
				persona2.getDireccion().setManzana(
						UtilJdbc.obtenerCadena(rs, "manzana"));
				persona2.getDireccion().setLote(
						UtilJdbc.obtenerCadena(rs, "lote"));
				Telefono teldireccion = new Telefono();
				teldireccion.setNumeroTelefono(UtilJdbc.obtenerCadena(rs,
						"teledireccion"));
				persona2.getDireccion().getTelefonos().add(teldireccion);
				resultado.add(persona2);
			}
			
			conn.commit();
		} catch (SQLException e) {
			resultado = null;
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

		return resultado;
	}
	
	@Override
	public void registroCliente(Cliente cliente, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersonaproveedor(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, cliente.getCodigoEntero());
			cs.setInt(i++, cliente.getRubro().getCodigoEntero());
			
			cs.execute();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}

	@Override
	public void actualizarPersonaAdicional(Cliente cliente, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_actualizarpersonaproveedor(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, cliente.getCodigoEntero());
			cs.setInt(i++, cliente.getRubro().getCodigoEntero());
			
			cs.execute();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			try {
				if (cs != null){
					cs.close();
				}
			} catch (SQLException e) {
				throw new SQLException(e);
			}
		}
	}
}
