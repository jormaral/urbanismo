package com.mitc.redes.editorfip.servicios.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Determinacion;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documento;
import com.mitc.redes.editorfip.entidades.rpm.planeamiento.Documentodeterminacion;

@Name("documentodeterminacionHome")
public class DocumentodeterminacionHome
		extends
			EntityHome<Documentodeterminacion> {

	@In(create = true)
	DeterminacionHome determinacionHome;
	@In(create = true)
	DocumentoHome documentoHome;

	public void setDocumentodeterminacionIden(Integer id) {
		setId(id);
	}

	public Integer getDocumentodeterminacionIden() {
		return (Integer) getId();
	}

	@Override
	protected Documentodeterminacion createInstance() {
		Documentodeterminacion documentodeterminacion = new Documentodeterminacion();
		return documentodeterminacion;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Determinacion determinacion = determinacionHome.getDefinedInstance();
		if (determinacion != null) {
			getInstance().setDeterminacion(determinacion);
		}
		Documento documento = documentoHome.getDefinedInstance();
		if (documento != null) {
			getInstance().setDocumento(documento);
		}
	}

	public boolean isWired() {
		if (getInstance().getDeterminacion() == null)
			return false;
		if (getInstance().getDocumento() == null)
			return false;
		return true;
	}

	public Documentodeterminacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
