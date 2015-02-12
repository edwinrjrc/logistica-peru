/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import pe.com.logistica.bean.base.BaseNegocio;
import pe.com.logistica.bean.base.BaseVO;

/**
 * @author Edwin
 *
 */
public class PagoServicio extends BaseNegocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9143805785741012964L;
	
	private BaseVO servicio;
	private Date fechaPago;
	private BigDecimal montoPago;
	private InputStream sustentoPago;
	private byte[] sustentoPagoByte;
	/**
	 * 
	 */
	public PagoServicio() {
		// TODO Auto-generated constructor stub
	}

	public BaseVO getServicio() {
		if (servicio == null){
			servicio = new BaseVO();
		}
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(BaseVO servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the fechaPago
	 */
	public Date getFechaPago() {
		if (fechaPago == null){
			fechaPago = new Date();
		}
		return fechaPago;
	}

	/**
	 * @param fechaPago the fechaPago to set
	 */
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	/**
	 * @return the montoPago
	 */
	public BigDecimal getMontoPago() {
		return montoPago;
	}

	/**
	 * @param montoPago the montoPago to set
	 */
	public void setMontoPago(BigDecimal montoPago) {
		this.montoPago = montoPago;
	}

	/**
	 * @return the sustentoPago
	 */
	public InputStream getSustentoPago() {
		return sustentoPago;
	}

	/**
	 * @param sustentoPago the sustentoPago to set
	 */
	public void setSustentoPago(InputStream sustentoPago) {
		this.sustentoPago = sustentoPago;
	}

	/**
	 * @return the sustentoPagoByte
	 */
	public byte[] getSustentoPagoByte() {
		return sustentoPagoByte;
	}

	/**
	 * @param sustentoPagoByte the sustentoPagoByte to set
	 */
	public void setSustentoPagoByte(byte[] sustentoPagoByte) {
		this.sustentoPagoByte = sustentoPagoByte;
	}

}
