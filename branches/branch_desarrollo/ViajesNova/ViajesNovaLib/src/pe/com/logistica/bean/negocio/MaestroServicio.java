/**
 * 
 */
package pe.com.logistica.bean.negocio;

import java.math.BigDecimal;

import pe.com.logistica.bean.base.BaseNegocio;

/**
 * @author edwreb
 *
 */
public class MaestroServicio extends BaseNegocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 499125844585986995L;
	
	private String nombre;
	private String descripcion;
	private boolean requiereFee;
	private boolean pagaImpto;
	private boolean cargaComision;
	private boolean comisionPorcentaje;
	private BigDecimal valorComision;

	private boolean esImpuesto;
	private boolean esFee;
	/**
	 * 
	 */
	public MaestroServicio() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the requiereFee
	 */
	public boolean isRequiereFee() {
		return requiereFee;
	}

	/**
	 * @param requiereFee the requiereFee to set
	 */
	public void setRequiereFee(boolean requiereFee) {
		this.requiereFee = requiereFee;
	}

	/**
	 * @return the pagaImpto
	 */
	public boolean isPagaImpto() {
		return pagaImpto;
	}

	/**
	 * @param pagaImpto the pagaImpto to set
	 */
	public void setPagaImpto(boolean pagaImpto) {
		this.pagaImpto = pagaImpto;
	}

	/**
	 * @return the cargaComision
	 */
	public boolean isCargaComision() {
		return cargaComision;
	}

	/**
	 * @param cargaComision the cargaComision to set
	 */
	public void setCargaComision(boolean cargaComision) {
		this.cargaComision = cargaComision;
	}

	/**
	 * @return the comisionPorcentaje
	 */
	public boolean isComisionPorcentaje() {
		return comisionPorcentaje;
	}

	/**
	 * @param comisionPorcentaje the comisionPorcentaje to set
	 */
	public void setComisionPorcentaje(boolean comisionPorcentaje) {
		this.comisionPorcentaje = comisionPorcentaje;
	}

	/**
	 * @return the valorComision
	 */
	public BigDecimal getValorComision() {
		return valorComision;
	}

	/**
	 * @param valorComision the valorComision to set
	 */
	public void setValorComision(BigDecimal valorComision) {
		this.valorComision = valorComision;
	}

	/**
	 * @return the esImpuesto
	 */
	public boolean isEsImpuesto() {
		return esImpuesto;
	}

	/**
	 * @param esImpuesto the esImpuesto to set
	 */
	public void setEsImpuesto(boolean esImpuesto) {
		this.esImpuesto = esImpuesto;
	}

	/**
	 * @return the esFee
	 */
	public boolean isEsFee() {
		return esFee;
	}

	/**
	 * @param esFee the esFee to set
	 */
	public void setEsFee(boolean esFee) {
		this.esFee = esFee;
	}

}
