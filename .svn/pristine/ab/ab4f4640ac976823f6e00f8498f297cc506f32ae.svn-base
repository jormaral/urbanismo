package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentoshp;

@Name("documentoshpHome")
public class DocumentoshpHome extends EntityHome<Documentoshp> {

	@In(create = true)
	DocumentoHome documentoHome;

	public void setDocumentoshpIden(Integer id) {
		setId(id);
	}

	public Integer getDocumentoshpIden() {
		return (Integer) getId();
	}

	@Override
	protected Documentoshp createInstance() {
		Documentoshp documentoshp = new Documentoshp();
		return documentoshp;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Documento documento = documentoHome.getDefinedInstance();
		if (documento != null) {
			getInstance().setDocumento(documento);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Documentoshp getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
