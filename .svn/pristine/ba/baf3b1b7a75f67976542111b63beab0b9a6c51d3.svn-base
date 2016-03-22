package es.mitc.redes.urbanismoenred.utils.configuracion;

import java.util.List;

public interface IGenericServiceResult {

	/**
	 * Constante de error
	 */
	public static final long ERROR = -1L;
	
	
	public  List<String> getLog();

	public  Long getResult();

	public  void setResult(Long result);

	/*	 
	 * 	Join with other GenericServiceResult
	 */
	public  void merge(IGenericServiceResult res);

	/*
	 * 	Print into screen the log information
	 */
	public  void printLog();

	public String getLogText();

	public void addLog(String onelog);
	/*
	 * 
	 * 
	 */
	public  void loadFileLog(String file);

}