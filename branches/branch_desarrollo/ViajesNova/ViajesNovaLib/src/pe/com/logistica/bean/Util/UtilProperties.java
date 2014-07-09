/**
 * 
 */
package pe.com.logistica.bean.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author edwreb
 *
 */
public class UtilProperties {
	
	public static Properties cargaArchivo(String archivoProperties){
		String c = "C:\\aplicaciones\\"+archivoProperties;
		String d = "D:\\aplicaciones\\"+archivoProperties;
		File fc = new File(c);
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			if (fc.exists()){
				input = new FileInputStream(fc);
				prop.load(input);
			}
			else{
				fc = new File(d);
				if (fc.exists()){
					input = new FileInputStream(fc);
					prop.load(input);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return prop;
	}
	

}
