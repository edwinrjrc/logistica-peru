/**
 * 
 */
package pe.com.logistica.negocio.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import pe.com.logistica.bean.base.BaseVO;
import pe.com.logistica.bean.negocio.Comprobante;
import pe.com.logistica.bean.negocio.DetalleComprobante;
import pe.com.logistica.bean.negocio.DetalleServicioAgencia;
import pe.com.logistica.bean.negocio.Parametro;
import pe.com.logistica.bean.negocio.ServicioAgencia;
import pe.com.logistica.negocio.dao.ParametroDao;
import pe.com.logistica.negocio.dao.impl.ParametroDaoImpl;

/**
 * @author Edwin
 *
 */
public class UtilEjb {

	/**
	 * 
	 */
	public UtilEjb() {
		// TODO Auto-generated constructor stub
	}

	
	public static String obtenerCadenaPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return resourceMaestros.getString(llave);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int obtenerEnteroPropertieMaestro(String llave, String maestroPropertie){
		try {
			ResourceBundle resourceMaestros = ResourceBundle.getBundle(maestroPropertie);
			
			return convertirCadenaEntero(resourceMaestros.getString(llave));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static String obtenerCadenaBlanco(String cadena){
		if (StringUtils.isNotBlank(cadena)){
			return StringUtils.trimToEmpty(cadena);
		}
		return "";
	}
	
	public static int convertirCadenaEntero(String cadena){
		try {
			if (StringUtils.isNotBlank(cadena)){
				return Integer.parseInt(cadena);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static BigDecimal convertirCadenaDecimal(String numero){
		if (StringUtils.isNotBlank(numero)){
			return BigDecimal.valueOf(Double.valueOf(numero));
		}
		return BigDecimal.ZERO;
	}
	
	public static List<Comprobante> obtenerNumeroComprobante(List<DetalleServicioAgencia> listaDetalle){
		List<Comprobante> lista = new ArrayList<Comprobante>();
		for (int i=0; i<listaDetalle.size(); i++){
			DetalleServicioAgencia bean = listaDetalle.get(i);
			Comprobante comprobante = new Comprobante();
			comprobante.setNumeroComprobante(bean.getNroComprobante());
			comprobante.setTipoComprobante(bean.getTipoComprobante());
			lista.add(comprobante);
			comprobante = null;
			for (int j=i; j<listaDetalle.size(); j++){
				DetalleServicioAgencia bean2 = listaDetalle.get(j);
				if (!bean.getNroComprobante().equals(bean2.getNroComprobante())){
					comprobante = new Comprobante();
					comprobante.setNumeroComprobante(bean2.getNroComprobante());
					comprobante.setTipoComprobante(bean2.getTipoComprobante());
					lista.add(comprobante);
					comprobante = null;
				}
			}
			break;
		}
		
		return lista;
	}
	
	public static Comprobante obtenerNumeroComprobante(String numeroComprobante, ServicioAgencia servicioAgencia){
		Comprobante comp = null;
		try {
			comp = new Comprobante();
			comp.setNumeroComprobante(numeroComprobante);
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal totalIGV = BigDecimal.ZERO;
			ParametroDao parametroDao = new ParametroDaoImpl();
			Parametro param =
					  parametroDao.consultarParametro(UtilEjb
					  .obtenerEnteroPropertieMaestro( "codigoParametroIGV",
					  "aplicacionDatosEjb"));
			BaseVO tipoComprobante = null;
			DetalleComprobante detalle = null;
			for (int i=0; i<servicioAgencia.getListaDetalleServicio().size(); i++){
				DetalleServicioAgencia bean = servicioAgencia.getListaDetalleServicio().get(i);
				if (bean.getNroComprobante().equals(numeroComprobante)) {
					detalle = new DetalleComprobante();
					total = total.add(bean.getTotalServicio());
					tipoComprobante = bean.getTipoComprobante();
					detalle.setIdServicioDetalle(bean.getCodigoEntero());
					detalle.setCantidad(bean.getCantidad());
					detalle.setPrecioUnitario(bean.getPrecioUnitario());
					detalle.setTotalDetalle(bean.getTotalServicio());
					detalle.setConcepto(bean.getDescripcionServicio());
					detalle.setUsuarioCreacion(servicioAgencia.getUsuarioCreacion());
					detalle.setIpCreacion(servicioAgencia.getIpCreacion());
					comp.getDetalleComprobante().add(detalle);
					if (bean.getServiciosHijos() != null){
						for (int x=0; x<bean.getServiciosHijos().size(); x++){
							DetalleServicioAgencia hijo = bean.getServiciosHijos().get(x);
							if (hijo.getTipoServicio().getCodigoEntero().intValue() == UtilEjb.convertirCadenaEntero(param.getValor())){
								totalIGV = totalIGV.add(hijo.getPrecioUnitario());
							}
						}
					}
				}
			}
			comp.setTitular(servicioAgencia.getCliente());
			comp.setFechaComprobante(servicioAgencia.getFechaServicio());
			comp.setTipoComprobante(tipoComprobante);
			comp.setTotalComprobante(total);
			comp.setTotalIGV(totalIGV);
			comp.setUsuarioCreacion(servicioAgencia.getUsuarioCreacion());
			comp.setIpCreacion(servicioAgencia.getIpCreacion());
			comp.setIdServicio(servicioAgencia.getCodigoEntero());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comp;
	}
}
