/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import pe.com.logistica.bean.base.Persona;
import pe.com.logistica.bean.negocio.Proveedor;
import pe.com.logistica.negocio.dao.PersonaDao;

/**
 * @author Edwin
 *
 */
public class PersonaDaoImpl implements PersonaDao {

	/**
	 * 
	 */
	public PersonaDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#listarProveedores()
	 */
	@Override
	public List<Proveedor> listarPersonas() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#registrarProveedor(pe.com.logistica.bean.negocio.Proveedor)
	 */
	@Override
	public void registrarPersona(Persona persona, Connection conexion) throws SQLException {

		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersona(?,?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.BOOLEAN);
			cs.setInt(i++, persona.getTipoPersona());
			cs.setString(i++, persona.getNombres());
			cs.setString(i++, persona.getRazonSocial());
			cs.setString(i++, persona.getApellidoPaterno());
			cs.setString(i++, persona.getApellidoMaterno());
			cs.setInt(i++, persona.getGenero().getCodigoEntero());
			cs.setInt(i++, persona.getEstadoCivil().getCodigoEntero());
			cs.setInt(i++, persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero());
			cs.setString(i++, persona.getDocumentoIdentidad().getNumeroDocumento());
			cs.setString(i++, persona.getUsuarioCreacion());
			cs.setString(i++, persona.getIpCreacion());
			
			
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

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#actualizarProveedor(pe.com.logistica.bean.negocio.Proveedor)
	 */
	@Override
	public void actualizarPersona(Proveedor proveedor) throws SQLException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see pe.com.logistica.negocio.dao.ProveedorDao#consultarProveedor(int)
	 */
	@Override
	public Proveedor consultarPersona(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
