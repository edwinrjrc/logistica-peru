/**
 * 
 */
package pe.com.logistica.web.faces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.richfaces.component.UIDataTable;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pe.com.logistica.bean.cargaexcel.CeldaExcel;
import pe.com.logistica.bean.cargaexcel.ColumnasExcel;

/**
 * @author EDWREB
 *
 */
@ManagedBean(name = "cargaReporteProveedorMBean")
@SessionScoped()
public class CargaReporteProveedorMBean extends BaseMBean {

	private final static Logger logger = Logger.getLogger(ClienteMBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6607933550231690113L;

	
	private Integer filaInicial;
	private Integer columnaInicial;
	private Integer nroColumnas;
	
	private List<CeldaExcel> tablaExcelCargada;
	private ColumnasExcel columnasExcel;
	private UploadedFile archivo;
	
	
	public CargaReporteProveedorMBean() {
		// TODO Auto-generated constructor stub
	}

	
	public void listenerExcel(FileUploadEvent event){
		this.setArchivo(event.getUploadedFile());
	}
	
	public void cargarArchivoExcel (){
		try {
			HSSFWorkbook archivoExcel = new HSSFWorkbook(archivo.getInputStream());
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * ===================================================================================
	 */
	
	
	/**
	 * @return the filaInicial
	 */
	public Integer getFilaInicial() {
		return filaInicial;
	}

	/**
	 * @param filaInicial the filaInicial to set
	 */
	public void setFilaInicial(Integer filaInicial) {
		this.filaInicial = filaInicial;
	}

	/**
	 * @return the columnaInicial
	 */
	public Integer getColumnaInicial() {
		return columnaInicial;
	}

	/**
	 * @param columnaInicial the columnaInicial to set
	 */
	public void setColumnaInicial(Integer columnaInicial) {
		this.columnaInicial = columnaInicial;
	}

	/**
	 * @return the nroColumnas
	 */
	public Integer getNroColumnas() {
		return nroColumnas;
	}

	/**
	 * @param nroColumnas the nroColumnas to set
	 */
	public void setNroColumnas(Integer nroColumnas) {
		this.nroColumnas = nroColumnas;
	}


	/**
	 * @return the tablaExcelCargada
	 */
	public List<CeldaExcel> getTablaExcelCargada() {
		if (tablaExcelCargada == null){
			tablaExcelCargada = new ArrayList<CeldaExcel>();
		}
		return tablaExcelCargada;
	}


	/**
	 * @param tablaExcelCargada the tablaExcelCargada to set
	 */
	public void setTablaExcelCargada(List<CeldaExcel> tablaExcelCargada) {
		this.tablaExcelCargada = tablaExcelCargada;
	}


	/**
	 * @return the archivo
	 */
	public UploadedFile getArchivo() {
		return archivo;
	}


	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(UploadedFile archivo) {
		this.archivo = archivo;
	}


	/**
	 * @return the columnasExcel
	 */
	public ColumnasExcel getColumnasExcel() {
		if (columnasExcel == null){
			columnasExcel = new ColumnasExcel();
		}
		return columnasExcel;
	}


	/**
	 * @param columnasExcel the columnasExcel to set
	 */
	public void setColumnasExcel(ColumnasExcel columnasExcel) {
		this.columnasExcel = columnasExcel;
	}
}
