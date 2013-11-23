/**
 * 
 */
package pe.com.logistica.web.faces;


/**
 * @author Edwin
 *
 */

public class BaseMBean {

	private String nombreFormulario;
	private String mensajeModal;
	private String tipoModal;
	private String modalNombre;
	
	private boolean showModal;
	
	/**
	 * 
	 */
	public BaseMBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void aceptarBoton(){
		this.setShowModal(false);
		this.setTipoModal("");
		this.setMensajeModal("");
	}

	/**
	 * @return the nombreFormulario
	 */
	public String getNombreFormulario() {
		return nombreFormulario;
	}

	/**
	 * @param nombreFormulario the nombreFormulario to set
	 */
	public void setNombreFormulario(String nombreFormulario) {
		this.nombreFormulario = nombreFormulario;
	}

	/**
	 * @return the mensajeModal
	 */
	public String getMensajeModal() {
		return mensajeModal;
	}

	/**
	 * @param mensajeModal the mensajeModal to set
	 */
	public void setMensajeModal(String mensajeModal) {
		this.mensajeModal = mensajeModal;
	}

	/**
	 * @return the tipoModal
	 */
	public String getTipoModal() {
		return tipoModal;
	}

	/**
	 * @param tipoModal the tipoModal to set
	 */
	public void setTipoModal(String tipoModal) {
		this.tipoModal = tipoModal;
	}

	/**
	 * @return the modalNombre
	 */
	public String getModalNombre() {
		return modalNombre;
	}

	/**
	 * @param modalNombre the modalNombre to set
	 */
	public void setModalNombre(String modalNombre) {
		this.modalNombre = modalNombre;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	
	
}
