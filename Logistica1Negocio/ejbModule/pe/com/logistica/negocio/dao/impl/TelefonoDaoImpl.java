/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.negocio.Telefono;
import pe.com.logistica.negocio.dao.TelefonoDao;

/**
 * @author Edwin
 *
 */
public class TelefonoDaoImpl implements TelefonoDao {

	/**
	 * 
	 */
	public TelefonoDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.TelefonoDao#registrarTelefono(pe.com.logistica.bean.negocio.Telefono)
	 */
	@Override
	public int registrarTelefono(Telefono telefono, Connection conexion) throws SQLException {
		int resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresartelefono(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			if (StringUtils.isNotBlank(telefono.getNumeroTelefono())){
				cs.setString(i++, telefono.getNumeroTelefono());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (telefono.getEmpresaOperadora().getCodigoEntero() != null){
				cs.setInt(i++, telefono.getEmpresaOperadora().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (StringUtils.isNotBlank(telefono.getUsuarioCreacion())){
				cs.setString(i++, telefono.getUsuarioCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(telefono.getIpCreacion())){
				cs.setString(i++, telefono.getIpCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			
			cs.execute();
			resultado = cs.getInt(1);
		} catch (SQLException e) {
			resultado = 0;
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
		return resultado;
	}
	
	@Override
	public void registrarTelefonoDireccion(int idTelefono, int idDireccion, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresartelefonodireccion(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idTelefono);
			cs.setInt(i++, idDireccion);
			
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
	public void registrarTelefonoPersona(int idTelefono, int idPersona, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresartelefonopersona(?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idTelefono);
			cs.setInt(i++, idPersona);
			
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
