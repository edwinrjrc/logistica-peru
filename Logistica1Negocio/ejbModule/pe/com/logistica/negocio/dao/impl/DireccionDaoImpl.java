/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.Direccion;
import pe.com.logistica.negocio.dao.DireccionDao;

/**
 * @author Edwin
 *
 */
public class DireccionDaoImpl implements DireccionDao {

	/**
	 * 
	 */
	public DireccionDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int registrarDireccion(Direccion direccion, Connection conexion) throws SQLException {
		int resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresardireccion(?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, direccion.getVia().getCodigoEntero());
			if (StringUtils.isNotBlank(direccion.getNombreVia())){
				cs.setString(i++, direccion.getNombreVia());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getNumero())){
				cs.setString(i++, direccion.getNumero());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getInterior())){
				cs.setString(i++, direccion.getInterior());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getManzana())){
				cs.setString(i++, direccion.getManzana());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getLote())){
				cs.setString(i++, direccion.getLote());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			cs.setString(i++, direccion.isPrincipal()?"S":"N");
			if (StringUtils.isNotBlank(direccion.getUbigeo().getCodigoCadena())){
				cs.setString(i++, direccion.getUbigeo().getCodigoCadena());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getUsuarioCreacion())){
				cs.setString(i++, direccion.getUsuarioCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(direccion.getIpCreacion())){
				cs.setString(i++, direccion.getIpCreacion());
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
	public void registrarPersonaDireccion(int idPersona, int idTipoPersona, int idDireccion, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersonadireccion(?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, idPersona);
			cs.setInt(i++, idTipoPersona);
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

}
