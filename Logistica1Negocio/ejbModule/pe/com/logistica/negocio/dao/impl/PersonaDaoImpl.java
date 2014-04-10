/**
 * 
 */
package pe.com.logistica.negocio.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	public int registrarPersona(Persona persona, Connection conexion) throws SQLException {
		int resultado = 0;
		CallableStatement cs = null;
		String sql = "{ ? = call negocio.fn_ingresarpersona(?,?,?,?,?,?,?,?,?,?) }";
		
		try {
			cs = conexion.prepareCall(sql);
			int i=1;
			cs.registerOutParameter(i++, Types.INTEGER);
			cs.setInt(i++, persona.getTipoPersona());
			if (StringUtils.isNotBlank(persona.getNombres())){
				cs.setString(i++, persona.getNombres());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(persona.getApellidoPaterno())){
				cs.setString(i++, persona.getApellidoPaterno());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(persona.getApellidoMaterno())){
				cs.setString(i++, persona.getApellidoMaterno());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (persona.getGenero().getCodigoEntero() != null && !Integer.valueOf(0).equals(persona.getGenero().getCodigoEntero())){
				cs.setInt(i++, persona.getGenero().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (persona.getEstadoCivil().getCodigoEntero() != null && !Integer.valueOf(0).equals(persona.getEstadoCivil().getCodigoEntero())){
				cs.setInt(i++, persona.getEstadoCivil().getCodigoEntero());
			}
			else{
				cs.setNull(i++, Types.INTEGER);
			}
			if (persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero() != null && !Integer.valueOf(0).equals(persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero())){
				cs.setInt(i++, persona.getDocumentoIdentidad().getTipoDocumento().getCodigoEntero());
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
			if (StringUtils.isNotBlank(persona.getUsuarioCreacion())){
				cs.setString(i++, persona.getUsuarioCreacion());
			}
			else{
				cs.setNull(i++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(persona.getIpCreacion())){
				cs.setString(i++, persona.getIpCreacion());
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
