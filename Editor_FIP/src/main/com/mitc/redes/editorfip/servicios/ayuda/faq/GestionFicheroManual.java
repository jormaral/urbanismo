package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.InputTextTag;
import com.icesoft.faces.component.PanelGridTag;
import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Manual;

/**
 * Servicio auxiliar que permite la gestion de archivos mediante el
 * componente ice:inputFile
 * 
 * @author fguerrero
 *
 */
@Local
public interface GestionFicheroManual {
	
	/**
	 * Metodo(evento) que se lanza al pulsar el boton "Subir" del componente.
	 * 
	 * @param ae parametro obligatorio para lanzar el evento.
	 */
	public void adjuntarArchivo(ActionEvent ae);
	
	/**
	 * Obtiene el valor de la propiedad que renderiza el panel de pop-up
	 * 
	 * @param adjuntarFicheroPopUp
	 */
	public void setAdjuntarFicheroPopUp(boolean adjuntarFicheroPopUp);
	
	/**
	 * Modifica el valor de la propiedad que renderiza el panel de pop-up
	 * 
	 * @return
	 */
	public boolean isAdjuntarFicheroPopUp();

}
