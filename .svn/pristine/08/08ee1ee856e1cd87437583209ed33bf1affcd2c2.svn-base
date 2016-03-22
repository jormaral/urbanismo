package es.mitc.redes.urbanismoenred.utils.configuracion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericServiceResult implements Serializable, IGenericServiceResult {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> log = new ArrayList<String>();
	private Long result = 0L;
	
	

	
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#getLog()
	 */
	public List<String> getLog() {
		return log;
	}

	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#getResult()
	 */
	public Long getResult() {
		return result;
	}
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#setResult(java.lang.Long)
	 */
	public void setResult(Long result) {
		this.result = result;
	}
	
	
	/*	 
	 * 	Join with other GenericServiceResult
	 */
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#merge(es.mitc.redes.urbanismoenred.IGenericServiceResult)
	 */
	public void merge( IGenericServiceResult res ) {
		if (res != null) {
			
			log.addAll( res.getLog() );
			
		}
	}
	
	
	/*
	 * 	Print into screen the log information
	 */
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#printLog()
	 */
	public void printLog() {
		for (String s:log) System.out.println(s);
	}
	
	
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#getLogText()
	 */
	public String getLogText() {
		String result = "";
		for (String s:log) result += "\n" + s;
		return result;
	}	
	
	
	/*
	 * 
	 * 
	 */
	/* (non-Javadoc)
	 * @see es.mitc.redes.urbanismoenred.IGenericServiceResult#loadFileLog(java.lang.String)
	 */
	public void loadFileLog(String file) {

		// Return a list log
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String linea = reader.readLine();
			while (linea != null) {
				log.add(linea);
				linea = reader.readLine();
			} 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void addLog(String onelog) {
		
		log.add(onelog);
		
	}
	
	

	
	
}
