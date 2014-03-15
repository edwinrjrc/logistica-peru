/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

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
	public void registrarTelefono(Telefono telefono, Connection conexion) throws SQLException {
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresartelefono(?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setString(i++, telefono.getNumeroTelefono());
			cs.setInt(i++, telefono.getEmpresaOperadora().getCodigoEntero());
			cs.setString(i++, telefono.getUsuarioCreacion());
			cs.setString(i++, telefono.getIpCreacion());
			
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
