package com.mitc.redes.editorfip.servicios.gestionfip.obtencionfip;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.faces.component.html.HtmlOutputText;

@Local
public interface LogImportacion {

	public void inicalizarFichero();

	public void anadirLinea(String linea);

	public HtmlOutputText getCampoDinamico();

	public void setCampoDinamico(HtmlOutputText campoDinamico);

	public void actualizarLog();

	@Remove
	public void destroy();

}