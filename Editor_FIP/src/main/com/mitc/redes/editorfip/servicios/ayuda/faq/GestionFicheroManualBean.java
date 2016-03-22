package com.mitc.redes.editorfip.servicios.ayuda.faq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesManager;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;


import com.icesoft.faces.component.InputTextTag;
import com.icesoft.faces.component.inputfile.FileInfo;
import com.icesoft.faces.component.inputfile.InputFile;
import com.icesoft.faces.context.Resource;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Faq;
import com.mitc.redes.editorfip.entidades.rpm.ayuda.Manual;
import com.mitc.redes.editorfip.servicios.ayuda.faq.GestionManuales;

@Stateless
@Name("gestionFicheroManual")
public class GestionFicheroManualBean implements GestionFicheroManual {
	
	@In (create = true)
	GestionManuales gestionManuales;
	
	private boolean adjuntarFicheroPopUp = false;	
	
	public void adjuntarArchivo(ActionEvent ae) {
		// Obtenemos los datos del componente y del fichero cargado
		InputFile inputFile = (InputFile) ae.getSource();
		gestionManuales.adjuntarArchivo(inputFile);
		adjuntarFicheroPopUp = false;
	}

	public void setAdjuntarFicheroPopUp(boolean adjuntarFicheroPopUp) {
		this.adjuntarFicheroPopUp = adjuntarFicheroPopUp;
	}

	public boolean isAdjuntarFicheroPopUp() {
		return adjuntarFicheroPopUp;
	}

}
